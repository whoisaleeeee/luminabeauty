package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.PedidoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Pedido;

@Path("PedidoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoRS {

    private PedidoBL pedidoBL;

    public PedidoRS() {
        this.pedidoBL = new PedidoBLImpl();
    }

    @POST
    public Pedido crearPedido(Pedido pedido) {
        Pedido resultado = null;
        try {
            resultado = pedidoBL.crearPedido(pedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }
}
