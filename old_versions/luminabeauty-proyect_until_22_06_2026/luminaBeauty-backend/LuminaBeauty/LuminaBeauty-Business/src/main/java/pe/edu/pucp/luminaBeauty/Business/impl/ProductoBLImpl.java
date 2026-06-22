package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.Exception.BLException;
import pe.edu.pucp.luminaBeauty.Business.ProductoBL;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class ProductoBLImpl implements ProductoBL {

    private ProductoDAO productoDAO = new ProductoDAOImpl();

    @Override
    public void validarStock(int idProducto, int cantidad) throws BLException {
        try{
            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser mayor a cero.");
            }

            if (producto.getStock() < cantidad) {
                throw new Exception("No hay stock suficiente para: " + producto.getNombre());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void descontarStock(int idProducto, int cantidad) throws BLException {
        try {
            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            if (producto.getStock() < cantidad) {
                throw new Exception("No hay stock suficiente.");
            }

            producto.setStock(producto.getStock() - cantidad);
            productoDAO.actualizar(producto);

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al descontar stock.", ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Producto buscarProducto(Integer id) throws BLException {
        try {
            return productoDAO.buscarPorId(id);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}