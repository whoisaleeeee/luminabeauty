package dao;

import luminabeauty.model.Pedido;

import java.util.ArrayList;

public interface PedidoDAO {
    int insertar(Pedido pedido);
    ArrayList<Pedido> listarTodos();
    Pedido buscarPorId(int id);
    int actualizar(Pedido pedido);
    int eliminar(int id);
}
