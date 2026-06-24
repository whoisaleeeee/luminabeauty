package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Producto;

import java.util.ArrayList;

public interface AlertaStockBL {

    ArrayList<Producto> listarProductosConStockBajo(int umbralMinimo) throws Exception;

    ArrayList<Producto> listarProductosSinStock() throws Exception;

    boolean productoTieneStockBajo(int idProducto, int umbralMinimo) throws Exception;

    String obtenerMensajeAlertaStock(int idProducto, int umbralMinimo) throws Exception;

    int contarProductosConStockBajo(int umbralMinimo) throws Exception;

    int contarProductosSinStock() throws Exception;
}
