package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Producto;

import java.util.List;

public interface ProductoBL {
    void validarStock(Integer idProducto, int cantidad) throws Exception;
    void descontarStock(Integer idProducto, int cantidad) throws Exception;
    Producto buscarProducto(Integer id) throws Exception;
    List<Producto> listarProductos() throws Exception;
}