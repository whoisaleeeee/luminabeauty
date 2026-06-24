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

import pe.edu.pucp.luminaBeauty.Business.DetalleListaBL;
import pe.edu.pucp.luminaBeauty.Business.impl.DetalleListaBLImpl;
import pe.edu.pucp.luminaBeauty.Model.DetalleLista;

import java.util.ArrayList;

@Path("DetalleListaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DetalleListaRS {

    private DetalleListaBL detalleListaBL;

    public DetalleListaRS() {
        this.detalleListaBL = new DetalleListaBLImpl();
    }

    @POST
    @Path("registrar")
    public DetalleLista registrarDetalleLista(DetalleLista detalleLista) {
        DetalleLista resultado = null;

        try {
            resultado = detalleListaBL.registrarDetalleLista(detalleLista);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public DetalleLista actualizarDetalleLista(DetalleLista detalleLista) {
        DetalleLista resultado = null;

        try {
            resultado = detalleListaBL.actualizarDetalleLista(detalleLista);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idDetalleLista}")
    public int eliminarDetalleLista(@PathParam("idDetalleLista") int idDetalleLista) {
        int resultado = 0;

        try {
            detalleListaBL.eliminarDetalleLista(idDetalleLista);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idDetalleLista}")
    public DetalleLista buscarDetalleLista(@PathParam("idDetalleLista") int idDetalleLista) {
        DetalleLista resultado = null;

        try {
            resultado = detalleListaBL.buscarDetalleLista(idDetalleLista);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<DetalleLista> listarDetallesLista() {
        ArrayList<DetalleLista> resultado = new ArrayList<>();

        try {
            resultado = detalleListaBL.listarDetallesLista();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorLista/{idListaDeDeseos}")
    public ArrayList<DetalleLista> listarDetallesPorLista(
            @PathParam("idListaDeDeseos") int idListaDeDeseos) {

        ArrayList<DetalleLista> resultado = new ArrayList<>();

        try {
            resultado = detalleListaBL.listarDetallesPorLista(idListaDeDeseos);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorProducto/{idProducto}")
    public ArrayList<DetalleLista> listarDetallesPorProducto(
            @PathParam("idProducto") int idProducto) {

        ArrayList<DetalleLista> resultado = new ArrayList<>();

        try {
            resultado = detalleListaBL.listarDetallesPorProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
