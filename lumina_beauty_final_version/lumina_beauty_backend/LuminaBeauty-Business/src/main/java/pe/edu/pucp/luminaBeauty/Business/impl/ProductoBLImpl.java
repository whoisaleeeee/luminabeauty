package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ProductoBL;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductoBLImpl implements ProductoBL {

    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    @Override
    public Producto registrarProducto(Producto producto) throws Exception {
        try {
            validarDatosProducto(producto);

            if (producto.getEstado() != 0 && producto.getEstado() != 1) {
                producto.setEstado(1);
            }

            Producto productoRegistrado = productoDAO.insertar(producto);
            TransactionContext.commit();

            return productoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar producto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Producto actualizarProducto(Producto producto) throws Exception {
        try {
            if (producto.getId_producto() <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto productoExistente = productoDAO.buscarPorId(producto.getId_producto());

            if (productoExistente == null) {
                throw new Exception("El producto no existe.");
            }

            validarDatosProducto(producto);

            Producto productoActualizado = productoDAO.actualizar(producto);
            TransactionContext.commit();

            return productoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar producto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarProducto(int idProducto) throws Exception {
        try {
            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            productoDAO.eliminar(producto);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar producto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Producto buscarProducto(int idProducto) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            return productoDAO.buscarPorId(idProducto);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Producto> listarProductos() throws Exception {
        try {
            return productoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Producto> filtrarPorTipoPiel(String tipo_piel) throws Exception {
        try {
            ArrayList<Producto> productos = productoDAO.listarTodos();
            ArrayList<Producto> filtrados = new ArrayList<>();

            if (tipo_piel == null || tipo_piel.trim().isEmpty()) {
                return productos;
            }

            for (Producto producto : productos) {
                if (producto.getTipoPiel() != null &&
                        (producto.getTipoPiel().equalsIgnoreCase(tipo_piel)
                                || producto.getTipoPiel().equalsIgnoreCase("TODOS"))) {

                    filtrados.add(producto);
                }
            }

            return filtrados;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Producto> listarProductosConStockBajo(int umbralMinimo) throws Exception {
        try {
            if (umbralMinimo < 0) {
                throw new Exception("El umbral mínimo no puede ser negativo.");
            }

            ArrayList<Producto> productos = productoDAO.listarTodos();
            ArrayList<Producto> productosStockBajo = new ArrayList<>();

            for (Producto producto : productos) {
                if (producto.getStock() <= umbralMinimo) {
                    productosStockBajo.add(producto);
                }
            }

            return productosStockBajo;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void validarStock(int id_producto, int cantidad) throws Exception {
        try {
            Producto producto = productoDAO.buscarPorId(id_producto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser mayor a cero.");
            }

            if (producto.getStock() < cantidad) {
                throw new Exception("No hay stock suficiente para: " + producto.getNombre());
            }

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void descontarStock(int id_producto, int cantidad) throws Exception {
        try {
            Producto producto = productoDAO.buscarPorId(id_producto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser mayor a cero.");
            }

            if (producto.getStock() < cantidad) {
                throw new Exception("No hay stock suficiente para: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - cantidad);
            productoDAO.actualizar(producto);

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al descontar stock: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void aumentarStock(int id_producto, int cantidad) throws Exception {
        try {
            Producto producto = productoDAO.buscarPorId(id_producto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            if (cantidad <= 0) {
                throw new Exception("La cantidad debe ser mayor a cero.");
            }

            producto.setStock(producto.getStock() + cantidad);
            productoDAO.actualizar(producto);

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al aumentar stock: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosProducto(Producto producto) throws Exception {
        if (producto == null) {
            throw new Exception("El producto no puede ser nulo.");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del producto es obligatorio.");
        }

        if (producto.getSku() == null || producto.getSku().trim().isEmpty()) {
            throw new Exception("El SKU del producto es obligatorio.");
        }

        if (producto.getSlug() == null || producto.getSlug().trim().isEmpty()) {
            throw new Exception("El slug del producto es obligatorio.");
        }

        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("El precio del producto no puede ser negativo.");
        }

        if (producto.getStock() < 0) {
            throw new Exception("El stock no puede ser negativo.");
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId_categoria() <= 0) {
            throw new Exception("Debe asignar una categoría válida al producto.");
        }

        if (producto.getMarca() == null || producto.getMarca().getId_marca() <= 0) {
            throw new Exception("Debe asignar una marca válida al producto.");
        }

        if (producto.getTipoPiel() != null && !producto.getTipoPiel().trim().isEmpty()) {
            validarTipoPiel(producto.getTipoPiel());
        }
    }

    private void validarTipoPiel(String tipo_piel) throws Exception {
        if (!tipo_piel.equals("SECA") &&
                !tipo_piel.equals("GRASA") &&
                !tipo_piel.equals("MIXTA") &&
                !tipo_piel.equals("SENSIBLE") &&
                !tipo_piel.equals("NORMAL") &&
                !tipo_piel.equals("TODOS")) {

            throw new Exception("Tipo de piel no válido.");
        }
    }
}