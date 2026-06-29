package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.edu.pucp.luminaBeauty.Business.ClienteBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ClienteBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;

import java.util.ArrayList;

@Path("ClienteRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteRS {

    private ClienteBL clienteBL;

    public ClienteRS() {
        this.clienteBL = new ClienteBLImpl();
    }

    @POST
    @Path("registrar")
    public Response registrar(Cliente cliente) {
        try {
            Cliente clienteRegistrado = clienteBL.registrarCliente(cliente);

            if (clienteRegistrado != null) {
                return Response.status(Response.Status.CREATED)
                        .entity(clienteRegistrado)
                        .build();
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"No se registró el cliente\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            String mensaje = e.getMessage() == null
                    ? "Error sin mensaje"
                    : e.getMessage().replace("\"", "'");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + mensaje + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("actualizar")
    public Cliente actualizarCliente(Cliente cliente) {
        try {
            return clienteBL.actualizarCliente(cliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @PUT
    @Path("desactivar/{idCliente}")
    public Response desactivarCuenta(@PathParam("idCliente") int idCliente) {
        try {
            Cliente cliente = clienteBL.buscarCliente(idCliente);

            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"El cliente no existe\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (cliente.getEstado() == 0) {
                return Response.ok(cliente).build();
            }

            cliente.setEstado(0);

            Cliente clienteActualizado = clienteBL.actualizarCliente(cliente);

            return Response.ok(clienteActualizado).build();
        } catch (Exception ex) {
            String mensaje = ex.getMessage() == null
                    ? "No se pudo desactivar la cuenta"
                    : ex.getMessage().replace("\"", "'");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + mensaje + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @DELETE
    @Path("eliminar/{idCliente}")
    public int eliminarCliente(@PathParam("idCliente") int idCliente) {
        try {
            clienteBL.eliminarCliente(idCliente);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @GET
    @Path("buscar/{idCliente}")
    public Cliente buscarCliente(@PathParam("idCliente") int idCliente) {
        try {
            return clienteBL.buscarCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @GET
    @Path("listar")
    public ArrayList<Cliente> listarClientes() {
        try {
            return clienteBL.listarClientes();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    @PUT
    @Path("sumarPuntos/{idCliente}")
    public int sumarPuntos(@PathParam("idCliente") int idCliente,
                           @QueryParam("puntos") int puntos) {
        try {
            clienteBL.sumarPuntos(idCliente, puntos);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @PUT
    @Path("restarPuntos/{idCliente}")
    public int restarPuntos(@PathParam("idCliente") int idCliente,
                            @QueryParam("puntos") int puntos) {
        try {
            clienteBL.restarPuntos(idCliente, puntos);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }
}
