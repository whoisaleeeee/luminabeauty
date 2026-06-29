package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.MovimientoPuntosFidelidadDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetallePedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.MovimientoPuntosFidelidadDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.Model.MovimientoPuntosFidelidad;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;
import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.EnvioDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Envio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PedidoBLImpl implements PedidoBL {

    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final EnvioDAO envioDAO = new EnvioDAOImpl();
    private final MovimientoPuntosFidelidadDAO movimientoPuntosDAO =
            new MovimientoPuntosFidelidadDAOImpl();

    @Override
    public Pedido crearPedido(Pedido pedido) throws Exception {
        try {
            validarDatosPedido(pedido);

            Cliente cliente = clienteDAO.buscarPorId(pedido.getCliente().getId_usuario());

            if (cliente == null) {
                throw new Exception("El cliente asociado al pedido no existe.");
            }

            String codigoPedido = pedido.getCodigo_pedido();

            if (codigoPedido == null ||
                    codigoPedido.trim().isEmpty() ||
                    codigoPedido.trim().length() > 14) {

                pedido.setCodigo_pedido(generarCodigoPedido());
            } else {
                pedido.setCodigo_pedido(codigoPedido.trim());
            }

            if (pedido.getEstado() == null || pedido.getEstado().trim().isEmpty()) {
                pedido.setEstado("PENDIENTE");
            }

            validarEstadoPedido(pedido.getEstado());

            if (pedido.getCosto_envio() == null) {
                pedido.setCosto_envio(BigDecimal.ZERO);
            }

            if (pedido.getDescuento() == null) {
                pedido.setDescuento(BigDecimal.ZERO);
            }

            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetallePedido detalle : pedido.getDetalles()) {
                validarDatosDetalle(detalle);

                Producto productoBD = productoDAO.buscarPorId(detalle.getProducto().getId_producto());

                if (productoBD == null) {
                    throw new Exception("El producto no existe.");
                }

                if (productoBD.getStock() < detalle.getCantidad()) {
                    throw new Exception("No hay stock suficiente para el producto: " + productoBD.getNombre());
                }

                detalle.setNombre_producto(productoBD.getNombre());
                detalle.setSku_producto(productoBD.getSku());
                detalle.setPrecioUnitario(productoBD.getPrecio());

                BigDecimal subtotalDetalle = productoBD.getPrecio()
                        .multiply(BigDecimal.valueOf(detalle.getCantidad()));

                subtotal = subtotal.add(subtotalDetalle);
            }

            pedido.setSubtotal_productos(subtotal);
            pedido.calcularTotal();

            Pedido pedidoRegistrado = pedidoDAO.insertar(pedido);

            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedidoRegistrado);

                detallePedidoDAO.insertar(detalle);

                Producto productoBD = productoDAO.buscarPorId(detalle.getProducto().getId_producto());
                productoBD.setStock(productoBD.getStock() - detalle.getCantidad());
                productoDAO.actualizar(productoBD);
            }

            TransactionContext.commit();

            return pedidoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al crear pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pedido buscarPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido != null) {
                pedido.setDetalles(listarDetallesPorPedido(idPedido));
            }

            return pedido;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pedido> listarPedidos() throws Exception {
        try {
            ArrayList<Pedido> pedidos = pedidoDAO.listarTodos();

            for (Pedido pedido : pedidos) {
                pedido.setDetalles(listarDetallesPorPedido(pedido.getId_pedido()));
            }

            return pedidos;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pedido> listarPedidosPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<Pedido> pedidos = pedidoDAO.listarPorCliente(idCliente);

            for (Pedido pedido : pedidos) {
                pedido.setDetalles(
                        listarDetallesPorPedido(pedido.getId_pedido())
                );
            }

            return pedidos;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void cancelarPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            if ("CANCELADO".equalsIgnoreCase(pedido.getEstado())) {
                throw new Exception("El pedido ya se encuentra cancelado.");
            }

            if ("ENTREGADO".equalsIgnoreCase(pedido.getEstado())) {
                throw new Exception("No se puede cancelar un pedido entregado.");
            }

            ArrayList<DetallePedido> detalles = listarDetallesPorPedido(idPedido);

            for (DetallePedido detalle : detalles) {
                if (detalle.getProducto() != null) {
                    Producto productoBD = productoDAO.buscarPorId(detalle.getProducto().getId_producto());

                    if (productoBD != null) {
                        productoBD.setStock(productoBD.getStock() + detalle.getCantidad());
                        productoDAO.actualizar(productoBD);
                    }
                }
            }

            Envio envioAsociado = null;

            for (Envio envio : envioDAO.listarTodos()) {
                if (envio.getPedido() != null &&
                        envio.getPedido().getId_pedido() == idPedido) {
                    envioAsociado = envio;
                    break;
                }
            }

            if (envioAsociado != null &&
                    ("DESPACHADO".equalsIgnoreCase(envioAsociado.getEstado()) ||
                            "EN_TRANSITO".equalsIgnoreCase(envioAsociado.getEstado()) ||
                            "ENTREGADO".equalsIgnoreCase(envioAsociado.getEstado()))) {

                throw new Exception(
                        "No se puede cancelar un pedido que ya fue despachado, está en tránsito o fue entregado."
                );
            }

            pedidoDAO.eliminar(pedido);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al cancelar pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pedido actualizarEstadoPedido(int idPedido, String estadoNuevo) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            if (estadoNuevo == null || estadoNuevo.trim().isEmpty()) {
                throw new Exception("El nuevo estado del pedido es obligatorio.");
            }

            estadoNuevo = estadoNuevo.trim().toUpperCase();
            validarEstadoPedido(estadoNuevo);

            if ("CANCELADO".equals(estadoNuevo)) {
                cancelarPedido(idPedido);
                return null;
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            if ("CANCELADO".equalsIgnoreCase(pedido.getEstado())) {
                throw new Exception("No se puede actualizar un pedido cancelado.");
            }

            if ("DEVUELTO".equals(estadoNuevo)) {
                if ("DEVUELTO".equalsIgnoreCase(pedido.getEstado())) {
                    throw new Exception("El pedido ya se encuentra devuelto.");
                }

                revertirPuntosPorDevolucion(pedido);
            }

            pedido.setEstado(estadoNuevo);

            Pedido pedidoActualizado = pedidoDAO.actualizar(pedido);
            TransactionContext.commit();

            return pedidoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar estado del pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void revertirPuntosPorDevolucion(Pedido pedido) throws Exception {
        if (pedido.getCliente() == null ||
                pedido.getCliente().getId_usuario() <= 0) {
            throw new Exception(
                    "El pedido no tiene un cliente válido para revertir puntos."
            );
        }

        Cliente cliente = clienteDAO.buscarPorId(
                pedido.getCliente().getId_usuario()
        );

        if (cliente == null) {
            throw new Exception("No se encontró el cliente asociado al pedido.");
        }

        ArrayList<MovimientoPuntosFidelidad> movimientos =
                movimientoPuntosDAO.listarPorCliente(cliente.getId_usuario());

        MovimientoPuntosFidelidad acumulacionOriginal = null;
        boolean ajusteYaRegistrado = false;

        for (MovimientoPuntosFidelidad movimiento : movimientos) {
            if (movimiento.getPedido() == null ||
                    movimiento.getPedido().getId_pedido()
                            != pedido.getId_pedido()) {
                continue;
            }

            if ("AJUSTE_DEVOLUCION".equalsIgnoreCase(
                    movimiento.getTipo_movimiento())) {
                ajusteYaRegistrado = true;
            }

            if ("ACUMULACION_COMPRA".equalsIgnoreCase(
                    movimiento.getTipo_movimiento())
                    && movimiento.getPuntos() > 0) {
                acumulacionOriginal = movimiento;
            }
        }

        if (ajusteYaRegistrado) {
            throw new Exception(
                    "Los puntos de este pedido ya fueron revertidos."
            );
        }

        // Si el pedido no generó puntos, no se debe crear un ajuste vacío.
        if (acumulacionOriginal == null) {
            return;
        }

        int puntosARevertir = acumulacionOriginal.getPuntos();
        int saldoAnterior = cliente.getPuntos_fidelidad();

        if (saldoAnterior < puntosARevertir) {
            throw new Exception(
                    "No se puede marcar el pedido como devuelto porque el cliente ya utilizó los puntos obtenidos en esta compra."
            );
        }

        int saldoPosterior = saldoAnterior - puntosARevertir;

        cliente.setPuntos_fidelidad(saldoPosterior);
        cliente.setNivel_cliente(calcularNivelCliente(saldoPosterior));
        clienteDAO.actualizar(cliente);

        MovimientoPuntosFidelidad ajuste =
                new MovimientoPuntosFidelidad();

        ajuste.setCliente(cliente);
        ajuste.setPedido(pedido);
        ajuste.setTipo_movimiento("AJUSTE_DEVOLUCION");
        ajuste.setPuntos(-puntosARevertir);
        ajuste.setSaldo_anterior(saldoAnterior);
        ajuste.setSaldo_posterior(saldoPosterior);
        ajuste.setMotivo(
                "Reversión de puntos por devolución del pedido "
                        + pedido.getCodigo_pedido()
        );

        movimientoPuntosDAO.insertar(ajuste);
    }

    private ArrayList<DetallePedido> listarDetallesPorPedido(int idPedido) throws Exception {
        ArrayList<DetallePedido> detalles = detallePedidoDAO.listarTodos();
        ArrayList<DetallePedido> resultado = new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            if (detalle.getPedido() != null &&
                    detalle.getPedido().getId_pedido() == idPedido) {
                resultado.add(detalle);
            }
        }

        return resultado;
    }

    private void validarDatosPedido(Pedido pedido) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no puede ser nulo.");
        }

        if (pedido.getCliente() == null || pedido.getCliente().getId_usuario() <= 0) {
            throw new Exception("Debe asignar un cliente válido al pedido.");
        }

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new Exception("El pedido debe tener al menos un detalle.");
        }

        if (pedido.getDescuento() != null && pedido.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("El descuento no puede ser negativo.");
        }

        if (pedido.getCosto_envio() != null && pedido.getCosto_envio().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("El costo de envío no puede ser negativo.");
        }
    }

    private void validarDatosDetalle(DetallePedido detalle) throws Exception {
        if (detalle == null) {
            throw new Exception("El detalle del pedido no puede ser nulo.");
        }

        if (detalle.getProducto() == null || detalle.getProducto().getId_producto() <= 0) {
            throw new Exception("Debe asignar un producto válido al detalle.");
        }

        if (detalle.getCantidad() <= 0) {
            throw new Exception("La cantidad del producto debe ser mayor a cero.");
        }
    }

    private void validarEstadoPedido(String estado) throws Exception {
        if (!estado.equals("PENDIENTE") &&
                !estado.equals("CONFIRMADO") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("ENVIADO") &&
                !estado.equals("ENTREGADO") &&
                !estado.equals("DEVUELTO") &&
                !estado.equals("CANCELADO")) {

            throw new Exception("Estado de pedido no válido.");
        }
    }

    private String calcularNivelCliente(int puntos) {
        if (puntos >= 1000) {
            return "PLATINO";
        } else if (puntos >= 500) {
            return "ORO";
        } else if (puntos >= 200) {
            return "PLATA";
        }

        return "BRONCE";
    }

    private String generarCodigoPedido() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return "P" + LocalDateTime.now().format(formatter);
    }
}

