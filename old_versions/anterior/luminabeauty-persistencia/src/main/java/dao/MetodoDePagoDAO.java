package dao;

import luminabeauty.model.MetodoDePago;

import java.util.ArrayList;

public interface MetodoDePagoDAO {
    int insertar(MetodoDePago metodo);
    ArrayList<MetodoDePago> listarTodos();
    MetodoDePago buscarPorId(int id);
    int actualizar(MetodoDePago metodo);
    int eliminar(int id);
}
