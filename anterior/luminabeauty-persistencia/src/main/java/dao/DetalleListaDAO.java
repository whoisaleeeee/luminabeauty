package dao;

import luminabeauty.model.DetalleLista;

import java.util.ArrayList;

public interface DetalleListaDAO {
    int insertar(DetalleLista detalle);
    ArrayList<DetalleLista> listarTodos();
    DetalleLista buscarPorId(int id);
    int actualizar(DetalleLista detalle);
    int eliminar(int id);
}
