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
import jakarta.ws.rs.core.Response;

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.PedidoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Pedido;

import java.util.ArrayList;

@Path("PedidoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoRS {

    private final PedidoBL pedidoBL;

    public PedidoRS() {
        this.pedidoBL = new PedidoBLImpl();
    }

    @POST
    @Path("crear")
    public Response crearPedido(Pedido pedido) {
        try {
            Pedido resultado = pedidoBL.crearPedido(pedido);

            if (resultado == null || resultado.getId_pedido() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo crear el pedido\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Evita la referencia circular:
            // Pedido -> DetallePedido -> Pedido
            resultado.setDetalles(new ArrayList<>());

            return Response.status(Response.Status.CREATED)
                    .entity(resultado)
                    .build();

        } catch (Exception ex) {
            ex.printStackTrace();

            String mensaje = ex.getMessage() == null
                    ? "Error al crear el pedido"
                    : ex.getMessage().replace("\"", "'");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + mensaje + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("buscar/{idPedido}")
    public Response buscarPedido(@PathParam("idPedido") int idPedido) {
        try {
            Pedido resultado = pedidoBL.buscarPedido(idPedido);

            if (resultado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Pedido no encontrado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.ok(resultado).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("listar")
    public Response listarPedidos() {
        try {
            ArrayList<Pedido> resultado = pedidoBL.listarPedidos();
            return Response.ok(resultado).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public Response listarPedidosPorCliente(@PathParam("idCliente") int idCliente) {
        try {
            ArrayList<Pedido> resultado = pedidoBL.listarPedidosPorCliente(idCliente);
            return Response.ok(resultado).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @DELETE
    @Path("cancelar/{idPedido}")
    public Response cancelarPedido(@PathParam("idPedido") int idPedido) {
        try {
            pedidoBL.cancelarPedido(idPedido);

            return Response.ok("{\"resultado\":1}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("actualizarEstado/{idPedido}/{estadoNuevo}")
    public Response actualizarEstadoPedido(
            @PathParam("idPedido") int idPedido,
            @PathParam("estadoNuevo") String estadoNuevo) {

        try {
            Pedido resultado = pedidoBL.actualizarEstadoPedido(
                    idPedido,
                    estadoNuevo
            );

            if (resultado == null && "CANCELADO".equalsIgnoreCase(estadoNuevo)) {
                return Response.ok("{\"resultado\":\"Pedido cancelado\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo actualizar el pedido\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.ok(resultado).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}