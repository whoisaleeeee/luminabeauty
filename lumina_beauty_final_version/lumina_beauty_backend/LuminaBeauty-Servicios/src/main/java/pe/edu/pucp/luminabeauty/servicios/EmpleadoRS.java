
package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.EmpleadoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.EmpleadoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Empleado;

import java.util.ArrayList;

@Path("EmpleadoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoRS {

    private EmpleadoBL empleadoBL;

    public EmpleadoRS() {
        this.empleadoBL = new EmpleadoBLImpl();
    }

    @POST
    @Path("registrar")
    public Empleado registrarEmpleado(Empleado empleado) {
        Empleado resultado = null;

        try {
            resultado = empleadoBL.registrarEmpleado(empleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Empleado actualizarEmpleado(Empleado empleado) {
        Empleado resultado = null;

        try {
            resultado = empleadoBL.actualizarEmpleado(empleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idEmpleado}")
    public int eliminarEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        int resultado = 0;

        try {
            empleadoBL.eliminarEmpleado(idEmpleado);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idEmpleado}")
    public Empleado buscarEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        Empleado resultado = null;

        try {
            resultado = empleadoBL.buscarEmpleado(idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Empleado> listarEmpleados() {
        ArrayList<Empleado> resultado = new ArrayList<>();

        try {
            resultado = empleadoBL.listarEmpleados();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorRol/{rol}")
    public ArrayList<Empleado> listarEmpleadosPorRol(@PathParam("rol") String rol) {
        ArrayList<Empleado> resultado = new ArrayList<>();

        try {
            resultado = empleadoBL.listarEmpleadosPorRol(rol);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
