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

import pe.edu.pucp.luminaBeauty.Business.ReembolsoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ReembolsoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Reembolso;

import java.util.ArrayList;

@Path("ReembolsoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReembolsoRS {

    private ReembolsoBL reembolsoBL;

    public ReembolsoRS() {
        this.reembolsoBL = new ReembolsoBLImpl();
    }

    @POST
    @Path("registrar")
    public Reembolso registrarReembolso(Reembolso reembolso) {
        Reembolso resultado = null;

        try {
            resultado = reembolsoBL.registrarReembolso(reembolso);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Reembolso actualizarReembolso(Reembolso reembolso) {
        Reembolso resultado = null;

        try {
            resultado = reembolsoBL.actualizarReembolso(reembolso);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idReembolso}")
    public int eliminarReembolso(@PathParam("idReembolso") int idReembolso) {
        int resultado = 0;

        try {
            reembolsoBL.eliminarReembolso(idReembolso);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idReembolso}")
    public Reembolso buscarReembolso(@PathParam("idReembolso") int idReembolso) {
        Reembolso resultado = null;

        try {
            resultado = reembolsoBL.buscarReembolso(idReembolso);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Reembolso> listarReembolsos() {
        ArrayList<Reembolso> resultado = new ArrayList<>();

        try {
            resultado = reembolsoBL.listarReembolsos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEstado/{estado}")
    public ArrayList<Reembolso> listarReembolsosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Reembolso> resultado = new ArrayList<>();

        try {
            resultado = reembolsoBL.listarReembolsosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorPago/{idPago}")
    public ArrayList<Reembolso> listarReembolsosPorPago(@PathParam("idPago") int idPago) {
        ArrayList<Reembolso> resultado = new ArrayList<>();

        try {
            resultado = reembolsoBL.listarReembolsosPorPago(idPago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorDevolucion/{idDevolucion}")
    public ArrayList<Reembolso> listarReembolsosPorDevolucion(@PathParam("idDevolucion") int idDevolucion) {
        ArrayList<Reembolso> resultado = new ArrayList<>();

        try {
            resultado = reembolsoBL.listarReembolsosPorDevolucion(idDevolucion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public ArrayList<Reembolso> listarReembolsosPorEmpleado(@PathParam("idEmpleado") int idEmpleado) {
        ArrayList<Reembolso> resultado = new ArrayList<>();

        try {
            resultado = reembolsoBL.listarReembolsosPorEmpleado(idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("procesar/{idReembolso}/{idEmpleado}")
    public Reembolso procesarReembolso(@PathParam("idReembolso") int idReembolso,
                                       @PathParam("idEmpleado") int idEmpleado,
                                       @QueryParam("referencia") String referenciaTransaccion) {
        Reembolso resultado = null;

        try {
            resultado = reembolsoBL.procesarReembolso(idReembolso, idEmpleado, referenciaTransaccion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("fallido/{idReembolso}")
    public Reembolso marcarReembolsoFallido(@PathParam("idReembolso") int idReembolso,
                                            @QueryParam("motivo") String motivo) {
        Reembolso resultado = null;

        try {
            resultado = reembolsoBL.marcarReembolsoFallido(idReembolso, motivo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
