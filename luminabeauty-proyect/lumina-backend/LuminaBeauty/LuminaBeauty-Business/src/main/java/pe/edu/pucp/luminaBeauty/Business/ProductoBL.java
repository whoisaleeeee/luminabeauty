package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Producto;

public interface ProductoBL {
    void validarStock(int idProducto, int cantidad) throws Exception;
    void descontarStock(int idProducto, int cantidad) throws Exception;
    Producto buscarProducto(Integer id) throws Exception;
}