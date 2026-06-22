package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.CarroBL;
import pe.edu.pucp.luminaBeauty.Business.impl.CarroBLImpl;
import pe.edu.pucp.luminaBeauty.Model.CarroDeCompras;
import pe.edu.pucp.luminaBeauty.Model.Producto;

@Path("CarroRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarroRS {

    private CarroBL carroBL;

    public CarroRS() {
        this.carroBL = new CarroBLImpl();
    }

    @POST
    @Path("agregarProducto")
    public int agregarProducto(AgregarProductoRequest request) {
        int resultado = 0;
        try {
            carroBL.agregarProducto(request.getCarro(), request.getProducto(), request.getCantidad());
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    // DTO porque agregarProducto necesita 3 parámetros y JSON solo manda un body
    public static class AgregarProductoRequest {
        private CarroDeCompras carro;
        private Producto producto;
        private int cantidad;

        public CarroDeCompras getCarro() { return carro; }
        public void setCarro(CarroDeCompras carro) { this.carro = carro; }

        public Producto getProducto() { return producto; }
        public void setProducto(Producto producto) { this.producto = producto; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}