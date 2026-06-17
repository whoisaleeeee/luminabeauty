package dao;
import luminabeauty.model.Producto;
import java.util.ArrayList;

public interface ProductoDAO {
    ArrayList<Producto> listarTodos();
    int insertar(Producto producto);
    Producto buscarPorId(int id);
    int actualizar(Producto producto);
    int eliminar(int id);
}