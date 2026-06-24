package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.AlertaStockBL;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class AlertaStockBLImpl implements AlertaStockBL {

    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    @Override
    public ArrayList<Producto> listarProductosConStockBajo(int umbralMinimo) throws Exception {
        try {
            if (umbralMinimo < 0) {
                throw new Exception("El umbral mínimo no puede ser negativo.");
            }

            ArrayList<Producto> productos = productoDAO.listarTodos();
            ArrayList<Producto> resultado = new ArrayList<>();

            for (Producto producto : productos) {
                if (producto.getEstado() == 1 &&
                        producto.getStock() > 0 &&
                        producto.getStock() <= umbralMinimo) {
                    resultado.add(producto);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Producto> listarProductosSinStock() throws Exception {
        try {
            ArrayList<Producto> productos = productoDAO.listarTodos();
            ArrayList<Producto> resultado = new ArrayList<>();

            for (Producto producto : productos) {
                if (producto.getEstado() == 1 &&
                        producto.getStock() <= 0) {
                    resultado.add(producto);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public boolean productoTieneStockBajo(int idProducto, int umbralMinimo) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            if (umbralMinimo < 0) {
                throw new Exception("El umbral mínimo no puede ser negativo.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            return producto.getEstado() == 1 &&
                    producto.getStock() > 0 &&
                    producto.getStock() <= umbralMinimo;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public String obtenerMensajeAlertaStock(int idProducto, int umbralMinimo) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            if (umbralMinimo < 0) {
                throw new Exception("El umbral mínimo no puede ser negativo.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            if (producto.getEstado() != 1) {
                return "El producto no está activo.";
            }

            if (producto.getStock() <= 0) {
                return "ALERTA: El producto " + producto.getNombre() + " se encuentra sin stock.";
            }

            if (producto.getStock() <= umbralMinimo) {
                return "ALERTA: El producto " + producto.getNombre() +
                        " tiene stock bajo. Stock actual: " + producto.getStock() +
                        ". Umbral mínimo: " + umbralMinimo + ".";
            }

            return "El producto " + producto.getNombre() + " tiene stock suficiente.";

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarProductosConStockBajo(int umbralMinimo) throws Exception {
        try {
            if (umbralMinimo < 0) {
                throw new Exception("El umbral mínimo no puede ser negativo.");
            }

            ArrayList<Producto> productos = productoDAO.listarTodos();
            int contador = 0;

            for (Producto producto : productos) {
                if (producto.getEstado() == 1 &&
                        producto.getStock() > 0 &&
                        producto.getStock() <= umbralMinimo) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarProductosSinStock() throws Exception {
        try {
            ArrayList<Producto> productos = productoDAO.listarTodos();
            int contador = 0;

            for (Producto producto : productos) {
                if (producto.getEstado() == 1 &&
                        producto.getStock() <= 0) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }
}
