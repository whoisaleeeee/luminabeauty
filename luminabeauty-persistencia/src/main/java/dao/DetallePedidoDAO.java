package dao;

import luminabeauty.model.DetallePedido;

import java.util.ArrayList;

public interface DetallePedidoDAO {
    int insertar(DetallePedido detalle);
    ArrayList<DetallePedido> listarTodos();
    DetallePedido buscarPorId(int id);
    int actualizar(DetallePedido detalle);
    int eliminar(int id);
}
