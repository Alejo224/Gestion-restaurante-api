package com.backend.sistemarestaurante.modules.pagos.controller;

import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
import com.backend.sistemarestaurante.modules.pedidos.repository.PedidoRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PedidoRepository pedidoRepository;

    @Value("${stripe.webhook.secret:whsec_temp}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            System.out.println("Webhook recibido de Stripe");

            // En desarrollo, procesar sin verificación
            System.out.println("Modo desarrollo - Procesando sin verificación estricta");

            // Intentar procesar el evento
            try {
                Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
                return procesarEventoStripe(event);
            } catch (SignatureVerificationException e) {
                // En desarrollo, continuar aunque falle la verificación
                System.out.println("Firma no verificada, pero continuando en modo desarrollo");
                return ResponseEntity.ok("Webhook received (dev mode)");
            }

        } catch (Exception e) {
            System.err.println("Error procesando webhook: " + e.getMessage());
            return ResponseEntity.ok("Webhook received"); // Siempre responder 200 a Stripe
        }
    }

    private ResponseEntity<String> procesarEventoStripe(Event event) {
        String eventType = event.getType();
        System.out.println("Tipo de evento: " + eventType);

        try {
            switch (eventType) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event);
                    break;

                case "checkout.session.expired":
                    handleCheckoutSessionExpired(event);
                    break;

                case "payment_intent.payment_failed":
                    handlePaymentFailed(event);
                    break;

                default:
                    System.out.println("Evento no manejado: " + eventType);
            }

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            System.err.println("Error procesando evento: " + e.getMessage());
            return ResponseEntity.ok("Event processed with errors"); // Siempre 200
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        try {
            Session session = (Session) event.getData().getObject();
            String pedidoId = session.getMetadata().get("pedido_id");

            System.out.println("Pago completado para pedido: " + pedidoId);

            if (pedidoId == null) {
                System.out.println("⚠Sesión sin pedido_id en metadata");
                return;
            }

            Pedido pedido = pedidoRepository.findById(Long.parseLong(pedidoId))
                    .orElse(null);

            if (pedido != null) {
                pedido.setEstadoPedidoEnum(EstadoPedidoEnum.COMPLETADO);
                pedidoRepository.save(pedido);
                System.out.println("Pedido actualizado a COMPLETADO: " + pedidoId);
            } else {
                System.out.println("Pedido no encontrado en BD: " + pedidoId);
            }

        } catch (Exception e) {
            System.err.println("Error en handleCheckoutSessionCompleted: " + e.getMessage());
        }
    }

    private void handleCheckoutSessionExpired(Event event) {
        try {
            Session session = (Session) event.getData().getObject();
            String pedidoId = session.getMetadata().get("pedido_id");

            if (pedidoId != null) {
                System.out.println("Sesión expirada para pedido: " + pedidoId);

                Pedido pedido = pedidoRepository.findById(Long.parseLong(pedidoId))
                        .orElse(null);

                if (pedido != null) {
                    pedido.setEstadoPedidoEnum(EstadoPedidoEnum.CANCELADO);
                    pedido.setMotivoCancelacion("Sesión de pago expirada");
                    pedidoRepository.save(pedido);
                    System.out.println("Pedido actualizado a CANCELADO: " + pedidoId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en handleCheckoutSessionExpired: " + e.getMessage());
        }
    }

    private void handlePaymentFailed(Event event) {
        System.out.println("Pago fallido detectado: " + event.getId());
    }
}