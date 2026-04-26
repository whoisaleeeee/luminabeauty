package dao;

import luminabeauty.model.Envio;

import java.util.ArrayList;

public interface EnvioDAO {
    int insertar(Envio envio);
    ArrayList<Envio> listarTodos();
    Envio buscarPorId(int id);
    int actualizar(Envio envio);
    int eliminar(int id);
}
