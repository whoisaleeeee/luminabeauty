package dao;

import luminabeauty.model.CategoriaProducto; // O CategoriaProducto, como se llame tu clase en model
import java.util.ArrayList;

public interface CategoriaProductoDAO {
    int insertar(CategoriaProducto categoria);
    ArrayList<CategoriaProducto> listarTodas();
    int actualizar(CategoriaProducto categoria);
    int eliminar(int id);

}