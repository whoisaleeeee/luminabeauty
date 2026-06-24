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

import pe.edu.pucp.luminaBeauty.Business.DetallePedidoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.DetallePedidoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;

import java.util.ArrayList;

@Path("DetallePedidoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DetallePedidoRS {

    private DetallePedidoBL detallePedidoBL;

    public DetallePedidoRS() {
        this.detallePedidoBL = new DetallePedidoBLImpl();
    }

    @POST
    @Path("registrar")
    public DetallePedido registrarDetallePedido(DetallePedido detallePedido) {
        DetallePedido resultado = null;

        try {
            resultado = detallePedidoBL.registrarDetallePedido(detallePedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public DetallePedido actualizarDetallePedido(DetallePedido detallePedido) {
        DetallePedido resultado = null;

        try {
            resultado = detallePedidoBL.actualizarDetallePedido(detallePedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idDetallePedido}")
    public int eliminarDetallePedido(@PathParam("idDetallePedido") int idDetallePedido) {
        int resultado = 0;

        try {
            detallePedidoBL.eliminarDetallePedido(idDetallePedido);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idDetallePedido}")
    public DetallePedido buscarDetallePedido(@PathParam("idDetallePedido") int idDetallePedido) {
        DetallePedido resultado = null;

        try {
            resultado = detallePedidoBL.buscarDetallePedido(idDetallePedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<DetallePedido> listarDetallesPedido() {
        ArrayList<DetallePedido> resultado = new ArrayList<>();

        try {
            resultado = detallePedidoBL.listarDetallesPedido();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorPedido/{idPedido}")
    public ArrayList<DetallePedido> listarDetallesPorPedido(@PathParam("idPedido") int idPedido) {
        ArrayList<DetallePedido> resultado = new ArrayList<>();

        try {
            resultado = detallePedidoBL.listarDetallesPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorProducto/{idProducto}")
    public ArrayList<DetallePedido> listarDetallesPorProducto(@PathParam("idProducto") int idProducto) {
        ArrayList<DetallePedido> resultado = new ArrayList<>();

        try {
            resultado = detallePedidoBL.listarDetallesPorProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

