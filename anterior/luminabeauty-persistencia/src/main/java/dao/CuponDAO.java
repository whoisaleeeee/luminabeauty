package dao;

import luminabeauty.model.Cupon;

import java.util.ArrayList;

public interface CuponDAO {
    int insertar(Cupon cupon);
    ArrayList<Cupon> listarTodos();
    Cupon buscarPorId(int id);
    int actualizar(Cupon cupon);
    int eliminar(int id);
}
