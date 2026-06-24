
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrar(Cliente cliente) {
        try {
            System.out.println("===== CLIENTE RECIBIDO =====");
            System.out.println("Nombre: " + cliente.getNombres());
            System.out.println("Correo: " + cliente.getCorreo());
            System.out.println("============================");

            Cliente clienteRegistrado = clienteBL.registrarCliente(cliente);

            if (clienteRegistrado != null) {
                return Response.status(Response.Status.CREATED)
                        .entity(clienteRegistrado)
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se registró el cliente\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

        }catch (Exception e) {
            e.printStackTrace();

            String mensaje = e.getMessage();
            if (mensaje == null) {
                mensaje = "Error sin mensaje";
            }

            mensaje = mensaje.replace("\"", "'");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + mensaje + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @PUT
    @Path("actualizar")
    public Cliente actualizarCliente(Cliente cliente) {
        Cliente resultado = null;

        try {
            resultado = clienteBL.actualizarCliente(cliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idCliente}")
    public int eliminarCliente(@PathParam("idCliente") int idCliente) {
        int resultado = 0;

        try {
            clienteBL.eliminarCliente(idCliente);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idCliente}")
    public Cliente buscarCliente(@PathParam("idCliente") int idCliente) {
        Cliente resultado = null;

        try {
            resultado = clienteBL.buscarCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> resultado = new ArrayList<>();

        try {
            resultado = clienteBL.listarClientes();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("sumarPuntos/{idCliente}")
    public int sumarPuntos(@PathParam("idCliente") int idCliente,
                           @QueryParam("puntos") int puntos) {
        int resultado = 0;

        try {
            clienteBL.sumarPuntos(idCliente, puntos);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("restarPuntos/{idCliente}")
    public int restarPuntos(@PathParam("idCliente") int idCliente,
                            @QueryParam("puntos") int puntos) {
        int resultado = 0;

        try {
            clienteBL.restarPuntos(idCliente, puntos);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

