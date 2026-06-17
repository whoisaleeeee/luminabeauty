package dao;

import luminabeauty.model.Valoracion;

import java.util.ArrayList;

public interface ValoracionDAO {
    int insertar(Valoracion valoracion);
    ArrayList<Valoracion> listarTodos();
    Valoracion buscarPorId(int id);
    int actualizar(Valoracion valoracion);
    int eliminar(int id);
}
