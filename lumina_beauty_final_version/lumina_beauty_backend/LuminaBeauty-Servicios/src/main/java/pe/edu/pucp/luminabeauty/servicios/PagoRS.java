package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.PagoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.PagoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Pago;

@Path("PagoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagoRS {

    private PagoBL pagoBL;

    public PagoRS() {
        this.pagoBL = new PagoBLImpl();
    }

    @POST
    public Pago registrarPago(Pago pago) {
        Pago resultado = null;
        try {
            resultado = pagoBL.registrarPago(pago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }
}
