package com.backend.sistemarestaurante.modules.pagos.service;

import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.dto.PedidoRequest;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import com.backend.sistemarestaurante.modules.pedidos.repository.PedidoRepository;
import com.backend.sistemarestaurante.modules.pagos.dto.SesionPagoResponse;
import com.backend.sistemarestaurante.modules.platos.Plato;
import com.backend.sistemarestaurante.modules.platos.PlatoRepository;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import com.backend.sistemarestaurante.modules.usuarios.UsuarioRepository;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StripeService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlatoRepository platoRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @Value("${stripe.currency}")
    private String currency;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public SesionPagoResponse crearSesionPago(PedidoRequest pedidoRequest, String userEmail) {
        try {
            // 1. Buscar usuario
            Usuario usuario = usuarioRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // 2. Validar y crear pedido
            Pedido pedido = crearPedidoDesdeRequest(pedidoRequest, usuario);

            // 3. Crear línea de items para Stripe
            List<SessionCreateParams.LineItem> lineItems = crearLineItemsStripe(pedidoRequest.getDetallePedidoRequestList());

            // 4. Crear sesión en Stripe
            Session session = crearSessionStripe(lineItems, pedido.getId(), userEmail);

            // 5. Actualizar pedido con session ID
            pedido.setStripeSessionId(session.getId());
            pedidoRepository.save(pedido);

            return new SesionPagoResponse(session.getUrl(), session.getId(), pedido.getId());

        } catch (StripeException e) {
            throw new RuntimeException("Error creando sesión de pago: " + e.getMessage(), e);
        }
    }

    private Pedido crearPedidoDesdeRequest(PedidoRequest request, Usuario usuario) {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTipoServicio(request.getTipoServicio());
        pedido.setDireccionEntrega(request.getDireccionEntrega());
        pedido.setTelefonoContacto(request.getTelefonoContacto());
        pedido.setNotas(request.getNotas());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedidoEnum(EstadoPedidoEnum.BORRADOR);

        // Agregar detalles del pedido
        List<String> platosNoEncontrados = new ArrayList<>();

        for (var detalleRequest : request.getDetallePedidoRequestList()) {
            Plato plato = platoRepository.findById(detalleRequest.getPlatoId())
                    .orElse(null);

            if (plato != null && plato.getDisponible()) {
                pedido.agregarDetalle(plato, detalleRequest.getCantidad(), detalleRequest.getNotas());
            } else {
                platosNoEncontrados.add("Plato ID: " + detalleRequest.getPlatoId());
            }
        }

        if (!platosNoEncontrados.isEmpty()) {
            throw new ResourceNotFoundException("Platos no disponibles: " + String.join(", ", platosNoEncontrados));
        }

        // Calcular totales
        pedido.calcularTotales();
        return pedidoRepository.save(pedido);
    }

    private List<SessionCreateParams.LineItem> crearLineItemsStripe(List<com.backend.sistemarestaurante.modules.pedidos.dto.DetallePedidoRequest> detalles) {
        return detalles.stream()
                .map(detalle -> {
                    Plato plato = platoRepository.findById(detalle.getPlatoId())
                            .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado: " + detalle.getPlatoId()));

                    // Convertir a centavos (Stripe requiere amount en la unidad más pequeña)
                    long amountInCents = plato.getPrecio().multiply(new java.math.BigDecimal("100")).longValue();

                    return SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency(currency.toLowerCase())
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(plato.getNombre())
                                                            .setDescription(plato.getDescripcion())
                                                            .build()
                                            )
                                            .setUnitAmount(amountInCents)
                                            .build()
                            )
                            .setQuantity((long) detalle.getCantidad())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Session crearSessionStripe(List<SessionCreateParams.LineItem> lineItems, Long pedidoId, String userEmail)
            throws StripeException {

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
                .putMetadata("pedido_id", pedidoId.toString())
                .putMetadata("user_email", userEmail)
                .setCustomerEmail(userEmail)
                .build();

        return Session.create(params);
    }

    // En tu StripeService - agrega este método
    public SesionPagoResponse crearSesionPagoParaPedidoExistente(Pedido pedido, String customerEmail) {
        try {
            System.out.println("💰 Creando sesión de Stripe para pedido existente: " + pedido.getId());

            // Validar que el pedido está en estado BORRADOR
            if (pedido.getEstadoPedidoEnum() != EstadoPedidoEnum.BORRADOR) {
                throw new RuntimeException("El pedido no está disponible para pago. Estado actual: " + pedido.getEstadoPedidoEnum());
            }

            // Crear la sesión de Checkout de Stripe
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/restaurante-frontend/pago-exitoso.html?pedidoId=" + pedido.getId())
                    .setCancelUrl("http://localhost:5173/restaurante-frontend/pago-cancelado.html?pedidoId=" + pedido.getId())
                    .setCustomerEmail(customerEmail)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("cop")
                                                    .setUnitAmount(pedido.getTotal().multiply(new java.math.BigDecimal("100")).longValue()) // Convertir a centavos Convertir a
                                    // centavos
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Pedido #" + pedido.getId())
                                                                    .setDescription("Pago del pedido en el restaurante")
                                                                    .build())
                                                    .build())
                                    .build())
                    .putMetadata("pedido_id", pedido.getId().toString())
                    .build();

            Session session = Session.create(params);

            // Actualizar el pedido existente con el sessionId de Stripe
            pedido.setStripeSessionId(session.getId());
            pedidoRepository.save(pedido);

            System.out.println("Sesión de Stripe creada para pedido existente: " + pedido.getId());
            System.out.println("Session ID: " + session.getId());
            System.out.println("Checkout URL: " + session.getUrl());

            return new SesionPagoResponse(session.getId(), session.getUrl(), pedido.getId());

        } catch (StripeException e) {
            System.err.println("Error creando sesión de Stripe: " + e.getMessage());
            throw new RuntimeException("Error creando sesión de Stripe: " + e.getMessage());
        }
    }
}
