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

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.PedidoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Pedido;

import java.util.ArrayList;

@Path("PedidoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoRS {

    private PedidoBL pedidoBL;

    public PedidoRS() {
        this.pedidoBL = new PedidoBLImpl();
    }

    @POST
    @Path("crear")
    public Pedido crearPedido(Pedido pedido) {
        Pedido resultado = null;

        try {
            resultado = pedidoBL.crearPedido(pedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idPedido}")
    public Pedido buscarPedido(@PathParam("idPedido") int idPedido) {
        Pedido resultado = null;

        try {
            resultado = pedidoBL.buscarPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Pedido> listarPedidos() {
        ArrayList<Pedido> resultado = new ArrayList<>();

        try {
            resultado = pedidoBL.listarPedidos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<Pedido> listarPedidosPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<Pedido> resultado = new ArrayList<>();

        try {
            resultado = pedidoBL.listarPedidosPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("cancelar/{idPedido}")
    public int cancelarPedido(@PathParam("idPedido") int idPedido) {
        int resultado = 0;

        try {
            pedidoBL.cancelarPedido(idPedido);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizarEstado/{idPedido}/{estadoNuevo}")
    public Pedido actualizarEstadoPedido(@PathParam("idPedido") int idPedido,
                                         @PathParam("estadoNuevo") String estadoNuevo) {
        Pedido resultado = null;

        try {
            resultado = pedidoBL.actualizarEstadoPedido(idPedido, estadoNuevo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

