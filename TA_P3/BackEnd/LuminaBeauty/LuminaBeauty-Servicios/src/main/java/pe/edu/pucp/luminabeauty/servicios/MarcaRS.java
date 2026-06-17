package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.luminaBeauty.Business.MarcaBL;
import pe.edu.pucp.luminaBeauty.Business.impl.MarcaBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Marca;

@Path("MarcaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaRS {
    private MarcaBL bl;

    public MarcaRS() {
        this.bl = new MarcaBLImpl();
    }

    @GET
    @Path("listar")
    public java.util.ArrayList<Marca> listarTodos() {
        try {
            return bl.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}
