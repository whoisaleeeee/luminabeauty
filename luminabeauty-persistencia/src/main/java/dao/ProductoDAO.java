package dao;
import luminabeauty.model.Producto;
import java.util.ArrayList;

public interface ProductoDAO {
    ArrayList<Producto> listarTodos();
    int insertar(Producto producto);
}