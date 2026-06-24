package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.PagoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.PagoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Pago;

import java.util.ArrayList;

@Path("PagoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagoRS {

    private PagoBL pagoBL;

    public PagoRS() {
        this.pagoBL = new PagoBLImpl();
    }

    @POST
    @Path("registrar")
    public Pago registrarPago(Pago pago) {
        Pago resultado = null;

        try {
            resultado = pagoBL.registrarPago(pago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Pago actualizarPago(Pago pago) {
        Pago resultado = null;

        try {
            resultado = pagoBL.actualizarPago(pago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idPago}")
    public int eliminarPago(@PathParam("idPago") int idPago) {
        int resultado = 0;

        try {
            pagoBL.eliminarPago(idPago);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idPago}")
    public Pago buscarPago(@PathParam("idPago") int idPago) {
        Pago resultado = null;

        try {
            resultado = pagoBL.buscarPago(idPago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Pago> listarPagos() {
        ArrayList<Pago> resultado = new ArrayList<>();

        try {
            resultado = pagoBL.listarPagos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEstado/{estado}")
    public ArrayList<Pago> listarPagosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Pago> resultado = new ArrayList<>();

        try {
            resultado = pagoBL.listarPagosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorMetodoPago/{idMetodoPago}")
    public ArrayList<Pago> listarPagosPorMetodoPago(@PathParam("idMetodoPago") int idMetodoPago) {
        ArrayList<Pago> resultado = new ArrayList<>();

        try {
            resultado = pagoBL.listarPagosPorMetodoPago(idMetodoPago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorPedido/{idPedido}")
    public Pago buscarPagoPorPedido(@PathParam("idPedido") int idPedido) {
        Pago resultado = null;

        try {
            resultado = pagoBL.buscarPagoPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("completar/{idPago}")
    public Pago completarPago(@PathParam("idPago") int idPago,
                              @QueryParam("referencia") String referenciaTransaccion) {
        Pago resultado = null;

        try {
            resultado = pagoBL.completarPago(idPago, referenciaTransaccion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("fallido/{idPago}")
    public Pago marcarPagoFallido(@PathParam("idPago") int idPago) {
        Pago resultado = null;

        try {
            resultado = pagoBL.marcarPagoFallido(idPago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("reembolsar/{idPago}")
    public Pago reembolsarPago(@PathParam("idPago") int idPago) {
        Pago resultado = null;

        try {
            resultado = pagoBL.reembolsarPago(idPago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}