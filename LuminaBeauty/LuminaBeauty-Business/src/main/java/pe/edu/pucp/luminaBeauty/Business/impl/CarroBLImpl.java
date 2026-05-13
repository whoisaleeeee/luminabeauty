package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.CarroBL;
import pe.edu.pucp.luminaBeauty.DAO.DetalleCarroDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetalleCarroDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.CarroDeCompras;
import pe.edu.pucp.luminaBeauty.Model.DetalleCarro;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class CarroBLImpl implements CarroBL {

    private ProductoDAO productoDAO = new ProductoDAOImpl();
    private DetalleCarroDAO detalleCarroDAO = new DetalleCarroDAOImpl();

    @Override
    public void agregarProducto(CarroDeCompras carro, Producto producto, int cantidad) throws Exception {
        try {
            if (carro == null) {
                throw new Exception("El carro no puede ser nulo.");
            }

            if (producto == null) {
                throw new Exception("El producto no puede ser nulo.");
            }

            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser mayor a cero.");
            }

            Producto productoBD = productoDAO.buscarPorId(producto.getId());

            if (productoBD == null) {
                throw new Exception("El producto no existe.");
            }

            if (productoBD.getStock() < cantidad) {
                throw new Exception("No hay stock suficiente.");
            }

            DetalleCarro detalle = new DetalleCarro();
            detalle.setCarro(carro);
            detalle.setIdProducto(productoBD);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(productoBD.getPrecio());

            detalleCarroDAO.insertar(detalle);

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al agregar producto al carro.", ex);
        } finally {
            TransactionContext.close();
        }
    }
}