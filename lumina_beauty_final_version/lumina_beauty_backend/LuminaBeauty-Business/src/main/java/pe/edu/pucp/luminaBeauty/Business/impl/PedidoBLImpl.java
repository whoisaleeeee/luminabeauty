package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetallePedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
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

    @Override
    public Pedido crearPedido(Pedido pedido) throws Exception {
        try {
            validarDatosPedido(pedido);

            Cliente cliente = clienteDAO.buscarPorId(pedido.getCliente().getId_usuario());

            if (cliente == null) {
                throw new Exception("El cliente asociado al pedido no existe.");
            }

            if (pedido.getCodigo_pedido() == null || pedido.getCodigo_pedido().trim().isEmpty()) {
                pedido.setCodigo_pedido(generarCodigoPedido());
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

            ArrayList<Pedido> pedidos = pedidoDAO.listarTodos();
            ArrayList<Pedido> resultado = new ArrayList<>();

            for (Pedido pedido : pedidos) {
                if (pedido.getCliente() != null &&
                        pedido.getCliente().getId_usuario() == idCliente) {

                    pedido.setDetalles(listarDetallesPorPedido(pedido.getId_pedido()));
                    resultado.add(pedido);
                }
            }

            return resultado;

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
                !estado.equals("CANCELADO")) {

            throw new Exception("Estado de pedido no válido.");
        }
    }

    private String generarCodigoPedido() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        return "P" + LocalDateTime.now().format(formatter);
    }
}

