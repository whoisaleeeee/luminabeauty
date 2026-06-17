package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.CuponBL;
import pe.edu.pucp.luminaBeauty.Business.impl.CuponBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Cupon;

import java.math.BigDecimal;

@Path("CuponRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuponRS {

    private CuponBL cuponBL;

    public CuponRS() {
        this.cuponBL = new CuponBLImpl();
    }

    @POST
    @Path("validar")
    public int validarCupon(Cupon cupon) {
        int resultado = 0;
        try {
            cuponBL.validarCupon(cupon);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @POST
    @Path("aplicarDescuento")
    public BigDecimal aplicarDescuento(AplicarDescuentoRequest request) {
        BigDecimal resultado = null;
        try {
            resultado = cuponBL.aplicarDescuento(request.getCupon(), request.getTotal());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    // DTO porque aplicarDescuento necesita 2 parámetros y JSON solo manda un body
    public static class AplicarDescuentoRequest {
        private Cupon cupon;
        private BigDecimal total;

        public Cupon getCupon() { return cupon; }
        public void setCupon(Cupon cupon) { this.cupon = cupon; }

        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
    }
}
