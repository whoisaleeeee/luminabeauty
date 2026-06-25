package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

import pe.edu.pucp.luminaBeauty.Business.MovimientoPuntosFidelidadBL;
import pe.edu.pucp.luminaBeauty.Business.impl.MovimientoPuntosFidelidadBLImpl;
import pe.edu.pucp.luminaBeauty.Model.MovimientoPuntosFidelidad;

@Path("MovimientoPuntosFidelidadRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimientoPuntosFidelidadRS {

    private final MovimientoPuntosFidelidadBL movimientoBL;

    public MovimientoPuntosFidelidadRS() {
        this.movimientoBL = new MovimientoPuntosFidelidadBLImpl();
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<MovimientoPuntosFidelidad> listarPorCliente(
            @PathParam("idCliente") int idCliente
    ) {
        ArrayList<MovimientoPuntosFidelidad> resultado =
                new ArrayList<>();

        try {
            resultado = movimientoBL.listarMovimientosPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}