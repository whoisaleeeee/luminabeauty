package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.CarroDeCompras;
import pe.edu.pucp.luminaBeauty.Model.Producto;

public interface CarroBL {
    void agregarProducto(CarroDeCompras carro, Producto producto, int cantidad) throws Exception;
}