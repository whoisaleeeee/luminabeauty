package dao;

import luminabeauty.model.Cliente;

import java.util.ArrayList;

public interface ClienteDAO {
    int insertar(Cliente cliente);
    ArrayList<Cliente> listarTodos();
    Cliente buscarPorId(int idUsuario);
    int actualizar(Cliente cliente);
    int eliminar(int idUsuario);
}
