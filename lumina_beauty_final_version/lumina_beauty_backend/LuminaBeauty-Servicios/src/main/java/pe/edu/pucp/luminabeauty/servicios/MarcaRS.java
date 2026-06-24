package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import pe.edu.pucp.luminaBeauty.Business.MarcaBL;
import pe.edu.pucp.luminaBeauty.Business.impl.MarcaBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Marca;

import java.util.List;

@Path("MarcaRS")
public class MarcaRS {
    MarcaBL marcaBL = new MarcaBLImpl();
    @GET
    public List<Marca> listar() throws Exception {
        return marcaBL.listaMarcas();
    }
}
