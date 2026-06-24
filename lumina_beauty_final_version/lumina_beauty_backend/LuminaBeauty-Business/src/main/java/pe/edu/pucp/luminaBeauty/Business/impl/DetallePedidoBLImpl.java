package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.DetallePedidoBL;
import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetallePedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DetallePedidoBLImpl implements DetallePedidoBL {

    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    @Override
    public DetallePedido registrarDetallePedido(DetallePedido detallePedido) throws Exception {
        try {
            validarDatosDetallePedido(detallePedido);

            Pedido pedido = pedidoDAO.buscarPorId(detallePedido.getPedido().getId_pedido());

            if (pedido == null) {
                throw new Exception("El pedido asociado al detalle no existe.");
            }

            Producto producto = productoDAO.buscarPorId(detallePedido.getProducto().getId_producto());

            if (producto == null) {
                throw new Exception("El producto asociado al detalle no existe.");
            }

            detallePedido.setNombre_producto(producto.getNombre());
            detallePedido.setSku_producto(producto.getSku());

            if (detallePedido.getPrecioUnitario() == null ||
                    detallePedido.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                detallePedido.setPrecioUnitario(producto.getPrecio());
            }

            DetallePedido detalleRegistrado = detallePedidoDAO.insertar(detallePedido);
            TransactionContext.commit();

            return detalleRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar detalle de pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public DetallePedido actualizarDetallePedido(DetallePedido detallePedido) throws Exception {
        try {
            if (detallePedido == null || detallePedido.getId_detalle_pedido() <= 0) {
                throw new Exception("El ID del detalle de pedido no es válido.");
            }

            DetallePedido detalleExistente = detallePedidoDAO.buscarPorId(
                    detallePedido.getId_detalle_pedido()
            );

            if (detalleExistente == null) {
                throw new Exception("El detalle de pedido no existe.");
            }

            validarDatosDetallePedido(detallePedido);

            Pedido pedido = pedidoDAO.buscarPorId(detallePedido.getPedido().getId_pedido());

            if (pedido == null) {
                throw new Exception("El pedido asociado al detalle no existe.");
            }

            Producto producto = productoDAO.buscarPorId(detallePedido.getProducto().getId_producto());

            if (producto == null) {
                throw new Exception("El producto asociado al detalle no existe.");
            }

            detallePedido.setNombre_producto(producto.getNombre());
            detallePedido.setSku_producto(producto.getSku());

            if (detallePedido.getPrecioUnitario() == null ||
                    detallePedido.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                detallePedido.setPrecioUnitario(producto.getPrecio());
            }

            DetallePedido detalleActualizado = detallePedidoDAO.actualizar(detallePedido);
            TransactionContext.commit();

            return detalleActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar detalle de pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarDetallePedido(int idDetallePedido) throws Exception {
        try {
            if (idDetallePedido <= 0) {
                throw new Exception("El ID del detalle de pedido no es válido.");
            }

            DetallePedido detallePedido = detallePedidoDAO.buscarPorId(idDetallePedido);

            if (detallePedido == null) {
                throw new Exception("El detalle de pedido no existe.");
            }

            detallePedidoDAO.eliminar(detallePedido);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar detalle de pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public DetallePedido buscarDetallePedido(int idDetallePedido) throws Exception {
        try {
            if (idDetallePedido <= 0) {
                throw new Exception("El ID del detalle de pedido no es válido.");
            }

            return detallePedidoDAO.buscarPorId(idDetallePedido);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<DetallePedido> listarDetallesPedido() throws Exception {
        try {
            return detallePedidoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<DetallePedido> listarDetallesPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            ArrayList<DetallePedido> detalles = detallePedidoDAO.listarTodos();
            ArrayList<DetallePedido> resultado = new ArrayList<>();

            for (DetallePedido detalle : detalles) {
                if (detalle.getPedido() != null &&
                        detalle.getPedido().getId_pedido() == idPedido) {
                    resultado.add(detalle);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<DetallePedido> listarDetallesPorProducto(int idProducto) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            ArrayList<DetallePedido> detalles = detallePedidoDAO.listarTodos();
            ArrayList<DetallePedido> resultado = new ArrayList<>();

            for (DetallePedido detalle : detalles) {
                if (detalle.getProducto() != null &&
                        detalle.getProducto().getId_producto() == idProducto) {
                    resultado.add(detalle);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosDetallePedido(DetallePedido detallePedido) throws Exception {
        if (detallePedido == null) {
            throw new Exception("El detalle de pedido no puede ser nulo.");
        }

        if (detallePedido.getPedido() == null ||
                detallePedido.getPedido().getId_pedido() <= 0) {
            throw new Exception("Debe asignar un pedido válido.");
        }

        if (detallePedido.getProducto() == null ||
                detallePedido.getProducto().getId_producto() <= 0) {
            throw new Exception("Debe asignar un producto válido.");
        }

        if (detallePedido.getCantidad() <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }

        if (detallePedido.getPrecioUnitario() != null &&
                detallePedido.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("El precio unitario no puede ser negativo.");
        }
    }
}

