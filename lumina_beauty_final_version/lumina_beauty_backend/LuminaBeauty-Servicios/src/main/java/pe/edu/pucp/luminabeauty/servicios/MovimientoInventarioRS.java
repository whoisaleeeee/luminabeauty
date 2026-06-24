package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.MovimientoInventarioBL;
import pe.edu.pucp.luminaBeauty.Business.impl.MovimientoInventarioBLImpl;
import pe.edu.pucp.luminaBeauty.Model.MovimientoInventario;

import java.util.ArrayList;

@Path("MovimientoInventarioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimientoInventarioRS {

    private MovimientoInventarioBL movimientoInventarioBL;

    public MovimientoInventarioRS() {
        this.movimientoInventarioBL = new MovimientoInventarioBLImpl();
    }

    @POST
    @Path("registrar")
    public MovimientoInventario registrarMovimientoInventario(MovimientoInventario movimiento) {
        MovimientoInventario resultado = null;

        try {
            resultado = movimientoInventarioBL.registrarMovimientoInventario(movimiento);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public MovimientoInventario actualizarMovimientoInventario(MovimientoInventario movimiento) {
        MovimientoInventario resultado = null;

        try {
            resultado = movimientoInventarioBL.actualizarMovimientoInventario(movimiento);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idMovimientoInventario}")
    public int eliminarMovimientoInventario(@PathParam("idMovimientoInventario") int idMovimientoInventario) {
        int resultado = 0;

        try {
            movimientoInventarioBL.eliminarMovimientoInventario(idMovimientoInventario);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idMovimientoInventario}")
    public MovimientoInventario buscarMovimientoInventario(
            @PathParam("idMovimientoInventario") int idMovimientoInventario) {

        MovimientoInventario resultado = null;

        try {
            resultado = movimientoInventarioBL.buscarMovimientoInventario(idMovimientoInventario);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<MovimientoInventario> listarMovimientosInventario() {
        ArrayList<MovimientoInventario> resultado = new ArrayList<>();

        try {
            resultado = movimientoInventarioBL.listarMovimientosInventario();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorProducto/{idProducto}")
    public ArrayList<MovimientoInventario> listarMovimientosPorProducto(
            @PathParam("idProducto") int idProducto) {

        ArrayList<MovimientoInventario> resultado = new ArrayList<>();

        try {
            resultado = movimientoInventarioBL.listarMovimientosPorProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorTipo/{tipoMovimiento}")
    public ArrayList<MovimientoInventario> listarMovimientosPorTipo(
            @PathParam("tipoMovimiento") String tipoMovimiento) {

        ArrayList<MovimientoInventario> resultado = new ArrayList<>();

        try {
            resultado = movimientoInventarioBL.listarMovimientosPorTipo(tipoMovimiento);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorPedido/{idPedido}")
    public ArrayList<MovimientoInventario> listarMovimientosPorPedido(
            @PathParam("idPedido") int idPedido) {

        ArrayList<MovimientoInventario> resultado = new ArrayList<>();

        try {
            resultado = movimientoInventarioBL.listarMovimientosPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorDevolucion/{idDevolucion}")
    public ArrayList<MovimientoInventario> listarMovimientosPorDevolucion(
            @PathParam("idDevolucion") int idDevolucion) {

        ArrayList<MovimientoInventario> resultado = new ArrayList<>();

        try {
            resultado = movimientoInventarioBL.listarMovimientosPorDevolucion(idDevolucion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEmpleado/{idEmpleado}")
    public ArrayList<MovimientoInventario> listarMovimientosPorEmpleado(
            @PathParam("idEmpleado") int idEmpleado) {

        ArrayList<MovimientoInventario> resultado = new ArrayList<>();

        try {
            resultado = movimientoInventarioBL.listarMovimientosPorEmpleado(idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
