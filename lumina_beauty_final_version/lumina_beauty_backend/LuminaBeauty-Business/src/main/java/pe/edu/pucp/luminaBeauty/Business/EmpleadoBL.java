
package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Empleado;

import java.util.ArrayList;

public interface EmpleadoBL {

    Empleado registrarEmpleado(Empleado empleado) throws Exception;

    Empleado actualizarEmpleado(Empleado empleado) throws Exception;

    void eliminarEmpleado(int idEmpleado) throws Exception;

    Empleado buscarEmpleado(int idEmpleado) throws Exception;

    ArrayList<Empleado> listarEmpleados() throws Exception;

    ArrayList<Empleado> listarEmpleadosPorRol(String rol) throws Exception;
}
