package dao;
import luminabeauty.model.Direccion;

import java.util.ArrayList;

public interface DireccionDAO {
    int insertar(Direccion direccion);
    ArrayList<Direccion> listarTodos();
    Direccion buscarPorId(int id);
    int actualizar(Direccion direccion);
    int eliminar(int id);
}


