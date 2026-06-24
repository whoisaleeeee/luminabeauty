package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Producto;

import java.util.ArrayList;

public interface ProductoBL {

    Producto registrarProducto(Producto producto) throws Exception;

    Producto actualizarProducto(Producto producto) throws Exception;

    void eliminarProducto(int idProducto) throws Exception;

    Producto buscarProducto(int idProducto) throws Exception;

    ArrayList<Producto> listarProductos() throws Exception;

    ArrayList<Producto> filtrarPorTipoPiel(String tipo_piel) throws Exception;

    ArrayList<Producto> listarProductosConStockBajo(int umbralMinimo) throws Exception;

    void validarStock(int idProducto, int cantidad) throws Exception;

    void descontarStock(int idProducto, int cantidad) throws Exception;

    void aumentarStock(int idProducto, int cantidad) throws Exception;
}