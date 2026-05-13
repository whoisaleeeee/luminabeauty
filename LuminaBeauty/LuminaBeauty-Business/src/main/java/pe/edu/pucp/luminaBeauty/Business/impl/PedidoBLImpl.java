package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;

import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetallePedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class PedidoBLImpl implements PedidoBL {

    private ProductoDAO productoDAO = new ProductoDAOImpl();
    private PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();

    @Override
    public Pedido crearPedido(Pedido pedido) throws Exception {
        try {
            if (pedido == null) {
                throw new Exception("El pedido no puede ser nulo.");
            }

            if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
                throw new Exception("El pedido debe tener al menos un detalle.");
            }

            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto productoBD = productoDAO.buscarPorId(detalle.getProducto().getId());

                if (productoBD == null) {
                    throw new Exception("El producto no existe.");
                }

                if (productoBD.getStock() < detalle.getCantidad()) {
                    throw new Exception("No hay stock suficiente para el producto: " + productoBD.getNombre());
                }
            }

            pedidoDAO.insertar(pedido);

            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
                detallePedidoDAO.insertar(detalle);

                Producto productoBD = productoDAO.buscarPorId(detalle.getProducto().getId());
                productoBD.setStock(productoBD.getStock() - detalle.getCantidad());
                productoDAO.actualizar(productoBD);
            }

            TransactionContext.commit();

            return pedido;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Ocurrió un error al crear el pedido.", ex);
        } finally {
            TransactionContext.close();
        }
    }
}