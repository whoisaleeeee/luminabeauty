package dao;

import luminabeauty.model.DetalleCarro;

import java.util.ArrayList;

public interface DetalleCarroDAO {
    int insertar(DetalleCarro detalle);
    ArrayList<DetalleCarro> listarTodos();
    DetalleCarro buscarPorId(int id);
    int actualizar(DetalleCarro detalle);
    int eliminar(int id);
}
