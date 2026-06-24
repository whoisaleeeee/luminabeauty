package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.AlertaStockBL;
import pe.edu.pucp.luminaBeauty.Business.impl.AlertaStockBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Producto;

import java.util.ArrayList;

@Path("AlertaStockRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertaStockRS {

    private AlertaStockBL alertaStockBL;

    public AlertaStockRS() {
        this.alertaStockBL = new AlertaStockBLImpl();
    }

    @GET
    @Path("productosStockBajo/{umbralMinimo}")
    public ArrayList<Producto> listarProductosConStockBajo(@PathParam("umbralMinimo") int umbralMinimo) {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = alertaStockBL.listarProductosConStockBajo(umbralMinimo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("productosSinStock")
    public ArrayList<Producto> listarProductosSinStock() {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = alertaStockBL.listarProductosSinStock();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("verificarStockBajo/{idProducto}/{umbralMinimo}")
    public int productoTieneStockBajo(@PathParam("idProducto") int idProducto,
                                      @PathParam("umbralMinimo") int umbralMinimo) {
        int resultado = 0;

        try {
            boolean tieneStockBajo = alertaStockBL.productoTieneStockBajo(idProducto, umbralMinimo);

            if (tieneStockBajo) {
                resultado = 1;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("mensaje/{idProducto}/{umbralMinimo}")
    public String obtenerMensajeAlertaStock(@PathParam("idProducto") int idProducto,
                                            @PathParam("umbralMinimo") int umbralMinimo) {
        String resultado = "";

        try {
            resultado = alertaStockBL.obtenerMensajeAlertaStock(idProducto, umbralMinimo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            resultado = ex.getMessage();
        }

        return resultado;
    }

    @GET
    @Path("contarStockBajo/{umbralMinimo}")
    public int contarProductosConStockBajo(@PathParam("umbralMinimo") int umbralMinimo) {
        int resultado = 0;

        try {
            resultado = alertaStockBL.contarProductosConStockBajo(umbralMinimo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("contarSinStock")
    public int contarProductosSinStock() {
        int resultado = 0;

        try {
            resultado = alertaStockBL.contarProductosSinStock();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}