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

import pe.edu.pucp.luminaBeauty.Business.HistorialEstadoPedidoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.HistorialEstadoPedidoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.HistorialEstadoPedido;

import java.util.ArrayList;

@Path("HistorialEstadoPedidoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistorialEstadoPedidoRS {

    private HistorialEstadoPedidoBL historialEstadoPedidoBL;

    public HistorialEstadoPedidoRS() {
        this.historialEstadoPedidoBL = new HistorialEstadoPedidoBLImpl();
    }

    @POST
    @Path("registrar")
    public HistorialEstadoPedido registrarHistorialEstadoPedido(HistorialEstadoPedido historial) {
        HistorialEstadoPedido resultado = null;

        try {
            resultado = historialEstadoPedidoBL.registrarHistorialEstadoPedido(historial);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public HistorialEstadoPedido actualizarHistorialEstadoPedido(HistorialEstadoPedido historial) {
        HistorialEstadoPedido resultado = null;

        try {
            resultado = historialEstadoPedidoBL.actualizarHistorialEstadoPedido(historial);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idHistorialEstadoPedido}")
    public int eliminarHistorialEstadoPedido(
            @PathParam("idHistorialEstadoPedido") int idHistorialEstadoPedido) {

        int resultado = 0;

        try {
            historialEstadoPedidoBL.eliminarHistorialEstadoPedido(idHistorialEstadoPedido);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idHistorialEstadoPedido}")
    public HistorialEstadoPedido buscarHistorialEstadoPedido(
            @PathParam("idHistorialEstadoPedido") int idHistorialEstadoPedido) {

        HistorialEstadoPedido resultado = null;

        try {
            resultado = historialEstadoPedidoBL.buscarHistorialEstadoPedido(idHistorialEstadoPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<HistorialEstadoPedido> listarHistorialesEstadoPedido() {
        ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

        try {
            resultado = historialEstadoPedidoBL.listarHistorialesEstadoPedido();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorPedido/{idPedido}")
    public ArrayList<HistorialEstadoPedido> listarHistorialesPorPedido(
            @PathParam("idPedido") int idPedido) {

        ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

        try {
            resultado = historialEstadoPedidoBL.listarHistorialesPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public ArrayList<HistorialEstadoPedido> listarHistorialesPorEmpleado(
            @PathParam("idEmpleado") int idEmpleado) {

        ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

        try {
            resultado = historialEstadoPedidoBL.listarHistorialesPorEmpleado(idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEstadoNuevo/{estadoNuevo}")
    public ArrayList<HistorialEstadoPedido> listarHistorialesPorEstadoNuevo(
            @PathParam("estadoNuevo") String estadoNuevo) {

        ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

        try {
            resultado = historialEstadoPedidoBL.listarHistorialesPorEstadoNuevo(estadoNuevo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @POST
    @Path("cambiarEstado/{idPedido}/{estadoNuevo}")
    public HistorialEstadoPedido registrarCambioEstadoPedido(
            @PathParam("idPedido") int idPedido,
            @PathParam("estadoNuevo") String estadoNuevo,
            @QueryParam("comentario") String comentario,
            @QueryParam("idEmpleado") Integer idEmpleado) {

        HistorialEstadoPedido resultado = null;

        try {
            resultado = historialEstadoPedidoBL.registrarCambioEstadoPedido(
                    idPedido,
                    estadoNuevo,
                    comentario,
                    idEmpleado
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
