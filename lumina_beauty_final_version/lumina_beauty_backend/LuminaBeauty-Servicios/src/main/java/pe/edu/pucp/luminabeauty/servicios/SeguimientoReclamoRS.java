package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.SeguimientoReclamoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.SeguimientoReclamoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.SeguimientoReclamo;

import java.util.ArrayList;

@Path("SeguimientoReclamoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeguimientoReclamoRS {

    private SeguimientoReclamoBL seguimientoReclamoBL;

    public SeguimientoReclamoRS() {
        this.seguimientoReclamoBL = new SeguimientoReclamoBLImpl();
    }

    @POST
    @Path("registrar")
    public SeguimientoReclamo registrarSeguimientoReclamo(SeguimientoReclamo seguimiento) {
        SeguimientoReclamo resultado = null;

        try {
            resultado = seguimientoReclamoBL.registrarSeguimientoReclamo(seguimiento);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public SeguimientoReclamo actualizarSeguimientoReclamo(SeguimientoReclamo seguimiento) {
        SeguimientoReclamo resultado = null;

        try {
            resultado = seguimientoReclamoBL.actualizarSeguimientoReclamo(seguimiento);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idSeguimientoReclamo}")
    public int eliminarSeguimientoReclamo(@PathParam("idSeguimientoReclamo") int idSeguimientoReclamo) {
        int resultado = 0;

        try {
            seguimientoReclamoBL.eliminarSeguimientoReclamo(idSeguimientoReclamo);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idSeguimientoReclamo}")
    public SeguimientoReclamo buscarSeguimientoReclamo(
            @PathParam("idSeguimientoReclamo") int idSeguimientoReclamo) {

        SeguimientoReclamo resultado = null;

        try {
            resultado = seguimientoReclamoBL.buscarSeguimientoReclamo(idSeguimientoReclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<SeguimientoReclamo> listarSeguimientosReclamo() {
        ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

        try {
            resultado = seguimientoReclamoBL.listarSeguimientosReclamo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorReclamo/{idReclamo}")
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorReclamo(@PathParam("idReclamo") int idReclamo) {
        ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

        try {
            resultado = seguimientoReclamoBL.listarSeguimientosPorReclamo(idReclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorTipo/{tipo}")
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorTipo(@PathParam("tipo") String tipo) {
        ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

        try {
            resultado = seguimientoReclamoBL.listarSeguimientosPorTipo(tipo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

        try {
            resultado = seguimientoReclamoBL.listarSeguimientosPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

        try {
            resultado = seguimientoReclamoBL.listarSeguimientosPorEmpleado(idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @POST
    @Path("cambiarEstado/{idReclamo}/{estadoNuevo}/{idEmpleado}")
    public SeguimientoReclamo registrarCambioEstadoReclamo(
            @PathParam("idReclamo") int idReclamo,
            @PathParam("estadoNuevo") String estadoNuevo,
            @PathParam("idEmpleado") int idEmpleado,
            @QueryParam("mensaje") String mensaje) {

        SeguimientoReclamo resultado = null;

        try {
            resultado = seguimientoReclamoBL.registrarCambioEstadoReclamo(
                    idReclamo,
                    estadoNuevo,
                    mensaje,
                    idEmpleado
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
