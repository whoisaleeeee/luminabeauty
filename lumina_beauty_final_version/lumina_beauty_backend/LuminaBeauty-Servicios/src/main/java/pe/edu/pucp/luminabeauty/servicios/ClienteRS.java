package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.ClienteBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ClienteBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;

@Path("ClienteRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteRS {

    private ClienteBL clienteBL;

    public ClienteRS() {
        this.clienteBL = new ClienteBLImpl();
    }

    @POST
    public Cliente registrarCliente(Cliente cliente) {
        Cliente resultado = null;
        try {
            resultado = clienteBL.registrarCliente(cliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("sumarPuntos/{idCliente}")
    public int sumarPuntos(@PathParam("idCliente") Integer idCliente, @QueryParam("puntos") int puntos) {
        int resultado = 0;
        try {
            clienteBL.sumarPuntos(idCliente, puntos);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }
}
