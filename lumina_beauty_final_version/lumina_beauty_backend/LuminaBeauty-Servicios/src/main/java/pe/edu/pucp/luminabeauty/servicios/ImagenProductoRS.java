
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

import pe.edu.pucp.luminaBeauty.Business.ImagenProductoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ImagenProductoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.ImagenProducto;

import java.util.ArrayList;

@Path("ImagenProductoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImagenProductoRS {

    private ImagenProductoBL imagenProductoBL;

    public ImagenProductoRS() {
        this.imagenProductoBL = new ImagenProductoBLImpl();
    }

    @POST
    @Path("registrar")
    public ImagenProducto registrarImagenProducto(ImagenProducto imagen) {
        ImagenProducto resultado = null;

        try {
            resultado = imagenProductoBL.registrarImagenProducto(imagen);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public ImagenProducto actualizarImagenProducto(ImagenProducto imagen) {
        ImagenProducto resultado = null;

        try {
            resultado = imagenProductoBL.actualizarImagenProducto(imagen);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idImagenProducto}")
    public int eliminarImagenProducto(@PathParam("idImagenProducto") int idImagenProducto) {
        int resultado = 0;

        try {
            imagenProductoBL.eliminarImagenProducto(idImagenProducto);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idImagenProducto}")
    public ImagenProducto buscarImagenProducto(@PathParam("idImagenProducto") int idImagenProducto) {
        ImagenProducto resultado = null;

        try {
            resultado = imagenProductoBL.buscarImagenProducto(idImagenProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<ImagenProducto> listarImagenesProducto() {
        ArrayList<ImagenProducto> resultado = new ArrayList<>();

        try {
            resultado = imagenProductoBL.listarImagenesProducto();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorProducto/{idProducto}")
    public ArrayList<ImagenProducto> listarImagenesPorProducto(@PathParam("idProducto") int idProducto) {
        ArrayList<ImagenProducto> resultado = new ArrayList<>();

        try {
            resultado = imagenProductoBL.listarImagenesPorProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("establecerPrincipal/{idImagenProducto}")
    public int establecerImagenPrincipal(@PathParam("idImagenProducto") int idImagenProducto) {
        int resultado = 0;

        try {
            imagenProductoBL.establecerImagenPrincipal(idImagenProducto);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
