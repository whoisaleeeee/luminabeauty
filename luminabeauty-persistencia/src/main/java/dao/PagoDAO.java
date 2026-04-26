package dao;

import luminabeauty.model.Pago;

import java.util.ArrayList;

public interface PagoDAO {
    int insertar(Pago pago);
    ArrayList<Pago> listarTodos();
    Pago buscarPorId(int id);
    int actualizar(Pago pago);
    int eliminar(int id);
}
