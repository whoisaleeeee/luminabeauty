package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.luminaBeauty.Business.CategoriaProductoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.CategoriaProductoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;

@Path("CategoriaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaRS {
    private CategoriaProductoBL bl;

    public CategoriaRS() {
        this.bl = new CategoriaProductoBLImpl();
    }

    @GET
    @Path("listar")
    public java.util.ArrayList<CategoriaProducto> listarTodos() {
        try {
            return bl.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}
