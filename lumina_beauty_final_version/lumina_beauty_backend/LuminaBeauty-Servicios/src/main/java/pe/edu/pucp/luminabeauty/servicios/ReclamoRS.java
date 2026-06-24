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

import pe.edu.pucp.luminaBeauty.Business.ReclamoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ReclamoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;

import java.util.ArrayList;

@Path("ReclamoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReclamoRS {

    private ReclamoBL reclamoBL;

    public ReclamoRS() {
        this.reclamoBL = new ReclamoBLImpl();
    }

    @POST
    @Path("registrar")
    public Reclamo registrarReclamo(Reclamo reclamo) {
        Reclamo resultado = null;

        try {
            resultado = reclamoBL.registrarReclamo(reclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Reclamo actualizarReclamo(Reclamo reclamo) {
        Reclamo resultado = null;

        try {
            resultado = reclamoBL.actualizarReclamo(reclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idReclamo}")
    public int eliminarReclamo(@PathParam("idReclamo") int idReclamo) {
        int resultado = 0;

        try {
            reclamoBL.eliminarReclamo(idReclamo);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idReclamo}")
    public Reclamo buscarReclamo(@PathParam("idReclamo") int idReclamo) {
        Reclamo resultado = null;

        try {
            resultado = reclamoBL.buscarReclamo(idReclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Reclamo> listarReclamos() {
        ArrayList<Reclamo> resultado = new ArrayList<>();

        try {
            resultado = reclamoBL.listarReclamos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<Reclamo> listarReclamosPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<Reclamo> resultado = new ArrayList<>();

        try {
            resultado = reclamoBL.listarReclamosPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorPedido/{idPedido}")
    public ArrayList<Reclamo> listarReclamosPorPedido(@PathParam("idPedido") int idPedido) {
        ArrayList<Reclamo> resultado = new ArrayList<>();

        try {
            resultado = reclamoBL.listarReclamosPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEstado/{estado}")
    public ArrayList<Reclamo> listarReclamosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Reclamo> resultado = new ArrayList<>();

        try {
            resultado = reclamoBL.listarReclamosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorPrioridad/{prioridad}")
    public ArrayList<Reclamo> listarReclamosPorPrioridad(@PathParam("prioridad") String prioridad) {
        ArrayList<Reclamo> resultado = new ArrayList<>();

        try {
            resultado = reclamoBL.listarReclamosPorPrioridad(prioridad);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("cambiarEstado/{idReclamo}/{estadoNuevo}")
    public Reclamo cambiarEstadoReclamo(@PathParam("idReclamo") int idReclamo,
                                        @PathParam("estadoNuevo") String estadoNuevo) {
        Reclamo resultado = null;

        try {
            resultado = reclamoBL.cambiarEstadoReclamo(idReclamo, estadoNuevo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("cambiarPrioridad/{idReclamo}/{prioridadNueva}")
    public Reclamo cambiarPrioridadReclamo(@PathParam("idReclamo") int idReclamo,
                                           @PathParam("prioridadNueva") String prioridadNueva) {
        Reclamo resultado = null;

        try {
            resultado = reclamoBL.cambiarPrioridadReclamo(idReclamo, prioridadNueva);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
