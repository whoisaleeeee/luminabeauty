
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

import pe.edu.pucp.luminaBeauty.Business.ProductoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ProductoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Producto;

import java.util.ArrayList;

@Path("ProductoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoRS {

    private ProductoBL productoBL;

    public ProductoRS() {
        this.productoBL = new ProductoBLImpl();
    }

    @POST
    @Path("registrar")
    public Producto registrarProducto(Producto producto) {
        Producto resultado = null;

        try {
            resultado = productoBL.registrarProducto(producto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Producto actualizarProducto(Producto producto) {
        Producto resultado = null;

        try {
            resultado = productoBL.actualizarProducto(producto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idProducto}")
    public int eliminarProducto(@PathParam("idProducto") int idProducto) {
        int resultado = 0;

        try {
            productoBL.eliminarProducto(idProducto);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idProducto}")
    public Producto buscarProducto(@PathParam("idProducto") int idProducto) {
        Producto resultado = null;

        try {
            resultado = productoBL.buscarProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Producto> listarProductos() {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = productoBL.listarProductos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("filtrarTipoPiel/{tipoPiel}")
    public ArrayList<Producto> filtrarPorTipoPiel(@PathParam("tipoPiel") String tipoPiel) {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = productoBL.filtrarPorTipoPiel(tipoPiel);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("stockBajo/{umbralMinimo}")
    public ArrayList<Producto> listarProductosConStockBajo(@PathParam("umbralMinimo") int umbralMinimo) {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = productoBL.listarProductosConStockBajo(umbralMinimo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("validarStock/{idProducto}/{cantidad}")
    public int validarStock(@PathParam("idProducto") int idProducto,
                            @PathParam("cantidad") int cantidad) {
        int resultado = 0;

        try {
            productoBL.validarStock(idProducto, cantidad);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("descontarStock/{idProducto}/{cantidad}")
    public int descontarStock(@PathParam("idProducto") int idProducto,
                              @PathParam("cantidad") int cantidad) {
        int resultado = 0;

        try {
            productoBL.descontarStock(idProducto, cantidad);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("aumentarStock/{idProducto}/{cantidad}")
    public int aumentarStock(@PathParam("idProducto") int idProducto,
                             @PathParam("cantidad") int cantidad) {
        int resultado = 0;

        try {
            productoBL.aumentarStock(idProducto, cantidad);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

