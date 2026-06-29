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
import jakarta.ws.rs.core.Response;

import pe.edu.pucp.luminaBeauty.Business.EnvioBL;
import pe.edu.pucp.luminaBeauty.Business.impl.EnvioBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Envio;

import java.util.ArrayList;

@Path("EnvioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnvioRS {

    private final EnvioBL envioBL;

    public EnvioRS() {
        this.envioBL = new EnvioBLImpl();
    }

    @POST
    @Path("registrar")
    public Response registrarEnvio(Envio envio) {
        try {
            Envio resultado = envioBL.registrarEnvio(envio);

            if (resultado == null || resultado.getId_envio() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo registrar el envío\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity(resultado)
                    .build();

        } catch (Exception ex) {
            ex.printStackTrace();

            String mensaje = ex.getMessage() == null
                    ? "Error al registrar el envío"
                    : ex.getMessage().replace("\"", "'");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + mensaje + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("actualizar")
    public Response actualizarEnvio(Envio envio) {
        try {
            Envio resultado = envioBL.actualizarEnvio(envio);

            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo actualizar el envío\"}")
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

    @GET
    @Path("buscar/{idEnvio}")
    public Response buscarEnvio(@PathParam("idEnvio") int idEnvio) {
        try {
            Envio resultado = envioBL.buscarEnvio(idEnvio);

            if (resultado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Envío no encontrado\"}")
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
    @Path("buscarPorPedido/{idPedido}")
    public Response buscarEnvioPorPedido(@PathParam("idPedido") int idPedido) {
        try {
            Envio resultado = envioBL.buscarEnvioPorPedido(idPedido);

            if (resultado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"El pedido no tiene envío registrado\"}")
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
    public Response listarEnvios() {
        try {
            ArrayList<Envio> resultado = envioBL.listarEnvios();
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
    @Path("listarPorEstado/{estado}")
    public Response listarEnviosPorEstado(@PathParam("estado") String estado) {
        try {
            ArrayList<Envio> resultado = envioBL.listarEnviosPorEstado(estado);
            return Response.ok(resultado).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("listarPorZona/{zonaEnvio}")
    public Response listarEnviosPorZona(
            @PathParam("zonaEnvio") String zonaEnvio) {

        try {
            ArrayList<Envio> resultado = envioBL.listarEnviosPorZona(zonaEnvio);
            return Response.ok(resultado).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @DELETE
    @Path("eliminar/{idEnvio}")
    public Response eliminarEnvio(@PathParam("idEnvio") int idEnvio) {
        try {
            envioBL.eliminarEnvio(idEnvio);

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
    @Path("despachar/{idEnvio}")
    public Response despacharEnvio(
            @PathParam("idEnvio") int idEnvio,
            @QueryParam("numeroSeguimiento") String numeroSeguimiento) {

        try {
            return Response.ok(
                    envioBL.despacharEnvio(idEnvio, numeroSeguimiento)
            ).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("enTransito/{idEnvio}")
    public Response marcarEnvioEnTransito(@PathParam("idEnvio") int idEnvio) {
        try {
            return Response.ok(
                    envioBL.marcarEnvioEnTransito(idEnvio)
            ).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("entregado/{idEnvio}")
    public Response marcarEnvioEntregado(@PathParam("idEnvio") int idEnvio) {
        try {
            return Response.ok(
                    envioBL.marcarEnvioEntregado(idEnvio)
            ).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("devuelto/{idEnvio}")
    public Response marcarEnvioDevuelto(@PathParam("idEnvio") int idEnvio) {
        try {
            return Response.ok(
                    envioBL.marcarEnvioDevuelto(idEnvio)
            ).build();

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + ex.getMessage().replace("\"", "'") + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}