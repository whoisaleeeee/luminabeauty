package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.EnvioBL;
import pe.edu.pucp.luminaBeauty.Business.impl.EnvioBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Envio;

@Path("EnvioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnvioRS {

    private EnvioBL envioBL;

    public EnvioRS() {
        this.envioBL = new EnvioBLImpl();
    }

    @POST
    public Envio crearEnvio(Envio envio) {
        Envio resultado = null;
        try {
            resultado = envioBL.crearEnvio(envio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("estado/{idEnvio}")
    public int actualizarEstado(@PathParam("idEnvio") Integer idEnvio, @QueryParam("nuevoEstado") String nuevoEstado) {
        int resultado = 0;
        try {
            envioBL.actualizarEstado(idEnvio, nuevoEstado);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }
}
