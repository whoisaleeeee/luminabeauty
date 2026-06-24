package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.ProductoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ProductoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Producto;

@Path("ProductoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoRS {

    private ProductoBL productoBL;

    public ProductoRS() {
        this.productoBL = new ProductoBLImpl();
    }

    @GET
    @Path("validarStock/{idProducto}/{cantidad}")
    public int validarStock(@PathParam("idProducto") Integer idProducto, @PathParam("cantidad") int cantidad) {
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
    public int descontarStock(@PathParam("idProducto") Integer idProducto, @PathParam("cantidad") int cantidad) {
        int resultado = 0;
        try {
            productoBL.descontarStock(idProducto, cantidad);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("{id}")
    public Producto buscarProducto(@PathParam("id") Integer id) {
        Producto resultado = new Producto();
        try {
            resultado = productoBL.buscarProducto(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        resultado.setId_producto(10);
        return resultado;
    }
}