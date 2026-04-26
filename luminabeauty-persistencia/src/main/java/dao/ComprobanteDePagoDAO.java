package dao;

import luminabeauty.model.ComprobanteDePago;

import java.util.ArrayList;

public interface ComprobanteDePagoDAO {
    int insertar(ComprobanteDePago comprobante);
    ArrayList<ComprobanteDePago> listarTodos();
    ComprobanteDePago buscarPorId(int id);
    int actualizar(ComprobanteDePago comprobante);
    int eliminar(int id);
}
