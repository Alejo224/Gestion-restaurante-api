package com.backend.sistemarestaurante.modules.pedidos.service;

import com.backend.sistemarestaurante.modules.pedidos.DetallePedido;
import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.dtos.DetallePedidoRequest;
import com.backend.sistemarestaurante.modules.pedidos.dtos.DetallePedidoResponse;
import com.backend.sistemarestaurante.modules.pedidos.dtos.PedidoRequest;
import com.backend.sistemarestaurante.modules.pedidos.dtos.PedidoResponse;
import com.backend.sistemarestaurante.modules.pedidos.enums.TipoServicio;
import com.backend.sistemarestaurante.modules.pedidos.repository.PedidoRepository;
import com.backend.sistemarestaurante.modules.platos.Plato;
import com.backend.sistemarestaurante.modules.platos.PlatoRepository;
import com.backend.sistemarestaurante.modules.usuarios.Usuario;
import com.backend.sistemarestaurante.modules.usuarios.UsuarioRepository;
import com.backend.sistemarestaurante.shared.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoService{

    //repositorios y otrosservicios necesarios

    private final PedidoRepository pedidoRepository;
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PlatoRepository platoRepository;

    // Metodo de crear pedido
    public PedidoResponse crearPedido(PedidoRequest pedidoRequest, String email){

        //  Buscar usuario por EMAIL (que viene en el token)
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el email: " + email));
        
        // Crear pedido base
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());

        // Mapear campos comunes
        modelMapper.map(pedidoRequest, pedido); //  Copia automáticamente: tipoServicio, notas, etc.

        // Campos condicionales (ModelMapper no puede mapear estos)
        if (pedidoRequest.getTipoServicio() == TipoServicio.DOMICILIO){
            // SOLO PARA DOMICILIO: telefono y direccion de entrega
            pedido.setTelefonoContacto(pedidoRequest.getTelefonoContacto());
            pedido.setDireccionEntrega(pedidoRequest.getDireccionEntrega());

            // Limpiar campo de recoger pedido
            pedido.setHoraRecogida(null);

        }
        if (pedidoRequest.getTipoServicio() == TipoServicio.RECOGER_PEDIDO){
            // SOLO PARA RECOGER_PEDIDO: telefono y hora de recogida
            pedido.setTelefonoContacto(pedidoRequest.getTelefonoContacto());
            pedido.setHoraRecogida(pedidoRequest.getHoraRecogida());

            // Limpiar el camdo de direccion de entrega
            pedido.setDireccionEntrega(null);
        }

        // Agregar detalles del carrito
        for (DetallePedidoRequest detallePedidoRequest : pedidoRequest.getDetallePedidoRequestList()){
            Plato plato = platoRepository.findById(detallePedidoRequest.getPlatoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + detallePedidoRequest.getPlatoId()));

            pedido.agregarDetalle(plato, detallePedidoRequest.getCantidad(), detallePedidoRequest.getNotas());
        }

        // Calcular totales
        pedido.calcularTotales();
        
        // Guardar
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        // Convertir a responde con modelMapper
        return convertirAResponse(pedidoGuardado);
    }

    private PedidoResponse convertirAResponse(Pedido pedido) {
        // ModelMapper automático para campos simples
        PedidoResponse response = modelMapper.map(pedido, PedidoResponse.class);

        // Campos que necesitan lógica especial
        response.setNombreUsuario(pedido.getUsuario().getNombreCompleto());

        // Copiar campos condicionales (ya estan en la entidad pero para asegurarse)
        response.setHoraRecogida(pedido.getHoraRecogida());
        response.setTelefonoContacto(pedido.getTelefonoContacto());
        response.setDireccionEntrega(pedido.getDireccionEntrega());

        // Convertir detalles
        response.setDetalles(pedido.getDetalles().stream()
                .map(this::convertirDetalleAResponse)
                .collect(Collectors.toList()));

        return response;
    }

    private DetallePedidoResponse convertirDetalleAResponse(DetallePedido detalle) {
        // modelMapper para detalles también
        DetallePedidoResponse response = modelMapper.map(detalle, DetallePedidoResponse.class);

        // Campos especiales
        response.setPlatoNombre(detalle.getPlato().getNombre());

        return response;
    }

    //  Opción A: Por email (recomendado con tu enfoque de token)
    public List<PedidoResponse> obtenerPedidosPorUsuario(String email) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioEmail(email);
        return pedidos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // ✅ Opción B: Por ID (si lo prefieres)
    public List<PedidoResponse> obtenerPedidosPorUsuarioId(Long usuarioId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        return pedidos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
}
