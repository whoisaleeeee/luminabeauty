
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

import pe.edu.pucp.luminaBeauty.Business.CategoriaProductoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.CategoriaProductoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;

import java.util.ArrayList;

@Path("CategoriaProductoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaProductoRS {

    private CategoriaProductoBL categoriaProductoBL;

    public CategoriaProductoRS() {
        this.categoriaProductoBL = new CategoriaProductoBLImpl();
    }

    @POST
    @Path("registrar")
    public CategoriaProducto registrarCategoria(CategoriaProducto categoria) {
        CategoriaProducto resultado = null;

        try {
            resultado = categoriaProductoBL.registrarCategoria(categoria);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public CategoriaProducto actualizarCategoria(CategoriaProducto categoria) {
        CategoriaProducto resultado = null;

        try {
            resultado = categoriaProductoBL.actualizarCategoria(categoria);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idCategoria}")
    public int eliminarCategoria(@PathParam("idCategoria") int idCategoria) {
        int resultado = 0;

        try {
            categoriaProductoBL.eliminarCategoria(idCategoria);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idCategoria}")
    public CategoriaProducto buscarCategoria(@PathParam("idCategoria") int idCategoria) {
        CategoriaProducto resultado = null;

        try {
            resultado = categoriaProductoBL.buscarCategoria(idCategoria);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<CategoriaProducto> listarCategorias() {
        ArrayList<CategoriaProducto> resultado = new ArrayList<>();

        try {
            resultado = categoriaProductoBL.listarCategorias();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorNombre/{nombre}")
    public ArrayList<CategoriaProducto> buscarCategoriasPorNombre(@PathParam("nombre") String nombre) {
        ArrayList<CategoriaProducto> resultado = new ArrayList<>();

        try {
            resultado = categoriaProductoBL.buscarCategoriasPorNombre(nombre);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
