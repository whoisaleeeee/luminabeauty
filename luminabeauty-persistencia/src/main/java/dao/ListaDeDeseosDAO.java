package dao;

import luminabeauty.model.ListaDeDeseos;

import java.util.ArrayList;

public interface ListaDeDeseosDAO {
    int insertar(ListaDeDeseos lista);
    ArrayList<ListaDeDeseos> listarTodos();
    ListaDeDeseos buscarPorId(int id);
    int actualizar(ListaDeDeseos lista);
    int eliminar(int id);
}
