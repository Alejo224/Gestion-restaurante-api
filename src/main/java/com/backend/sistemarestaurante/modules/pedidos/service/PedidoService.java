package com.backend.sistemarestaurante.modules.pedidos.service;

import com.backend.sistemarestaurante.modules.pedidos.DetallePedido;
import com.backend.sistemarestaurante.modules.pedidos.Pedido;
import com.backend.sistemarestaurante.modules.pedidos.dto.*;
import com.backend.sistemarestaurante.modules.pedidos.enums.EstadoPedidoEnum;
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
import java.util.ArrayList;
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
        // Buscar usuario por EMAIL
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el email: " + email));

        // Mapear con ModelMapper
        Pedido pedido = modelMapper.map(pedidoRequest, Pedido.class);

        //  Establecer los valores que NO vienen del request
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedidoEnum(EstadoPedidoEnum.BORRADOR);

        // Campos condicionales (ya los mapeó ModelMapper, solo ajustar)
        if (pedidoRequest.getTipoServicio() == TipoServicio.DOMICILIO){
            // Limpiar campo de recoger pedido
            pedido.setHoraRecogida(null);
        }
        if (pedidoRequest.getTipoServicio() == TipoServicio.RECOGER_PEDIDO){
            // Limpiar el campo de direccion de entrega
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

        return convertirAResponse(pedidoGuardado);
    }

    private PedidoResponse convertirAResponse(Pedido pedido) {
        // ModelMapper automático para campos simples
        PedidoResponse response = modelMapper.map(pedido, PedidoResponse.class);

        // Campos que necesitan lógica especial
        response.setNombreUsuario(pedido.getUsuario().getNombreCompleto());
        response.setEmailUsuario(pedido.getUsuario().getEmail());

        // Validar nulos en detalles
        if (pedido.getDetalles() != null) {
            response.setDetalles(pedido.getDetalles().stream()
                    .map(this::convertirDetalleAResponse)
                    .collect(Collectors.toList()));
        } else {
            response.setDetalles(new ArrayList<>());
        }

        return response;
    }

    private DetallePedidoResponse convertirDetalleAResponse(DetallePedido detalle) {
        // modelMapper para detalles también
        DetallePedidoResponse response = modelMapper.map(detalle, DetallePedidoResponse.class);

        // Campos especiales
        response.setPlatoNombre(detalle.getPlato().getNombre());

        return response;
    }

    //  Obtner pedido por email (enfoque de token)
    public List<PedidoResponse> obtenerPedidosPorUsuario(String email) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioEmail(email);
        return pedidos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // Obtener pedido Por ID
    public List<PedidoResponse> obtenerPedidosPorUsuarioId(Long usuarioId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        return pedidos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // metodo obtenr todos los pedidos de los usuarios
    public List<PedidoResponse> obtenerTodosLosPedidos(){
        List<Pedido> pedidos = pedidoRepository.findAll();

        return pedidos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // Cancelar pedido
    public PedidoResponse cancelarPedido(Long id, CancelarPedidoRequest request){

        // Verificar si el pedido existe
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Pedido no encontrado con el id: " + id));

        // Validar que se puede cancelar
        if (!pedidoExistente.puedeSerCancelado()){
            throw new IllegalStateException("No se puede ser cancelado por el estado: " + pedidoExistente.getEstadoPedidoEnum());
        }

        // Cancelar pedido
        pedidoExistente.cancelar(request.getMotivoCancelacion());

        // Guardar el pedido camcelado
        Pedido peidoCancelado = pedidoRepository.save(pedidoExistente);

        // Convertir a DTO de respuesta y retornar
        return convertirAResponse(peidoCancelado);
    }

    // Actualizar el estado del pedido
    public PedidoResponse actualizarEstadoPedido(Long id, ActualizarEstadoRequest request){

        // Verificar si el pedido existe
        Pedido pedidoExiste = pedidoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Pedido no encontrado con el id: " + id));

        validarTransicionEstado(pedidoExiste.getEstadoPedidoEnum(), request.getNuevoEstado());

        // Cambiar estado del pedido
        pedidoExiste.cambiarEstado(request.getNuevoEstado());
        
        // si es RECOGER_PEDIDO y el estado es LISTO, asignar hora recogida
        if (pedidoExiste.getTipoServicio() == TipoServicio.RECOGER_PEDIDO && request.getNuevoEstado() == EstadoPedidoEnum.LISTO && request.getHoraRecogida() != null){
            pedidoExiste.asignarHoraRecogida(request.getHoraRecogida());
        }

        // Guardar el pedido actualizado
        Pedido pedidoActualizado = pedidoRepository.save(pedidoExiste);

        return convertirAResponse(pedidoActualizado);
    }

    private void validarTransicionEstado(EstadoPedidoEnum estadoActual, EstadoPedidoEnum nuevoEstado){
        if (estadoActual == EstadoPedidoEnum.CANCELADO) {
            throw new IllegalStateException("No se puede modificar un pedido cancelado");
        }
        if (estadoActual == EstadoPedidoEnum.COMPLETADO && nuevoEstado != EstadoPedidoEnum.COMPLETADO) {
            throw new IllegalStateException("No se puede modificar un pedido completado");
        }
    }

    // Obtner pedido por id
    public PedidoResponse obtenerPedidoPorId(Long id){
        // Verificar si el pedido existe
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Pedido no encontrado con el id: " + id));

        // Mapeo a DTO para campos simples
        PedidoResponse response = modelMapper.map(pedidoExistente, PedidoResponse.class);

        // Campos que necesitan lógica especial
        response.setNombreUsuario(pedidoExistente.getUsuario().getNombreCompleto());
        response.setEmailUsuario(pedidoExistente.getUsuario().getEmail());

        return response;
    }

    public Pedido obtenerPedidoEntityPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }
}
