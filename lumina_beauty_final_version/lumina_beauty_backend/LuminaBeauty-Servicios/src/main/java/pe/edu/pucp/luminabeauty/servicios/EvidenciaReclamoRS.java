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

import pe.edu.pucp.luminaBeauty.Business.EvidenciaReclamoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.EvidenciaReclamoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.EvidenciaReclamo;

import java.util.ArrayList;

@Path("EvidenciaReclamoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvidenciaReclamoRS {

    private EvidenciaReclamoBL evidenciaReclamoBL;

    public EvidenciaReclamoRS() {
        this.evidenciaReclamoBL = new EvidenciaReclamoBLImpl();
    }

    @POST
    @Path("registrar")
    public EvidenciaReclamo registrarEvidenciaReclamo(EvidenciaReclamo evidencia) {
        EvidenciaReclamo resultado = null;

        try {
            resultado = evidenciaReclamoBL.registrarEvidenciaReclamo(evidencia);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public EvidenciaReclamo actualizarEvidenciaReclamo(EvidenciaReclamo evidencia) {
        EvidenciaReclamo resultado = null;

        try {
            resultado = evidenciaReclamoBL.actualizarEvidenciaReclamo(evidencia);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idEvidenciaReclamo}")
    public int eliminarEvidenciaReclamo(@PathParam("idEvidenciaReclamo") int idEvidenciaReclamo) {
        int resultado = 0;

        try {
            evidenciaReclamoBL.eliminarEvidenciaReclamo(idEvidenciaReclamo);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idEvidenciaReclamo}")
    public EvidenciaReclamo buscarEvidenciaReclamo(
            @PathParam("idEvidenciaReclamo") int idEvidenciaReclamo) {

        EvidenciaReclamo resultado = null;

        try {
            resultado = evidenciaReclamoBL.buscarEvidenciaReclamo(idEvidenciaReclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<EvidenciaReclamo> listarEvidenciasReclamo() {
        ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

        try {
            resultado = evidenciaReclamoBL.listarEvidenciasReclamo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorReclamo/{idReclamo}")
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorReclamo(@PathParam("idReclamo") int idReclamo) {
        ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

        try {
            resultado = evidenciaReclamoBL.listarEvidenciasPorReclamo(idReclamo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorTipo/{tipoArchivo}")
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorTipo(@PathParam("tipoArchivo") String tipoArchivo) {
        ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

        try {
            resultado = evidenciaReclamoBL.listarEvidenciasPorTipo(tipoArchivo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

        try {
            resultado = evidenciaReclamoBL.listarEvidenciasPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

        try {
            resultado = evidenciaReclamoBL.listarEvidenciasPorEmpleado(idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
