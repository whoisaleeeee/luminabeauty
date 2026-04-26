package dao;

import luminabeauty.model.Empleado;

import java.util.ArrayList;

public interface EmpleadoDAO {
    int insertar(Empleado empleado);
    ArrayList<Empleado> listarTodos();
    Empleado buscarPorId(int idUsuario);
    int actualizar(Empleado empleado);
    int eliminar(int idUsuario, boolean logico);
}
