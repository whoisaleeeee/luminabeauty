
package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.MarcaBL;
import pe.edu.pucp.luminaBeauty.Business.impl.MarcaBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Marca;

import java.util.ArrayList;

@Path("MarcaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaRS {

    private MarcaBL marcaBL;

    public MarcaRS() {
        this.marcaBL = new MarcaBLImpl();
    }

    @POST
    @Path("registrar")
    public Marca registrarMarca(Marca marca) {
        Marca resultado = null;

        try {
            resultado = marcaBL.registrarMarca(marca);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Marca actualizarMarca(Marca marca) {
        Marca resultado = null;

        try {
            resultado = marcaBL.actualizarMarca(marca);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idMarca}")
    public int eliminarMarca(@PathParam("idMarca") int idMarca) {
        int resultado = 0;

        try {
            marcaBL.eliminarMarca(idMarca);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idMarca}")
    public Marca buscarMarca(@PathParam("idMarca") int idMarca) {
        Marca resultado = null;

        try {
            resultado = marcaBL.buscarMarca(idMarca);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Marca> listarMarcas() {
        ArrayList<Marca> resultado = new ArrayList<>();

        try {
            resultado = marcaBL.listarMarcas();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorNombre/{nombre}")
    public ArrayList<Marca> buscarMarcasPorNombre(@PathParam("nombre") String nombre) {
        ArrayList<Marca> resultado = new ArrayList<>();

        try {
            resultado = marcaBL.buscarMarcasPorNombre(nombre);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

