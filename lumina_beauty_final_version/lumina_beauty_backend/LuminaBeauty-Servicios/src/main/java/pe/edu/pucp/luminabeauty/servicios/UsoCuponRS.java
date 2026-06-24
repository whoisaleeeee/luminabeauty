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

import pe.edu.pucp.luminaBeauty.Business.UsoCuponBL;
import pe.edu.pucp.luminaBeauty.Business.impl.UsoCuponBLImpl;
import pe.edu.pucp.luminaBeauty.Model.UsoCupon;

import java.util.ArrayList;

@Path("UsoCuponRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsoCuponRS {

    private UsoCuponBL usoCuponBL;

    public UsoCuponRS() {
        this.usoCuponBL = new UsoCuponBLImpl();
    }

    @POST
    @Path("registrar")
    public UsoCupon registrarUsoCupon(UsoCupon usoCupon) {
        UsoCupon resultado = null;

        try {
            resultado = usoCuponBL.registrarUsoCupon(usoCupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public UsoCupon actualizarUsoCupon(UsoCupon usoCupon) {
        UsoCupon resultado = null;

        try {
            resultado = usoCuponBL.actualizarUsoCupon(usoCupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idUsoCupon}")
    public int eliminarUsoCupon(@PathParam("idUsoCupon") int idUsoCupon) {
        int resultado = 0;

        try {
            usoCuponBL.eliminarUsoCupon(idUsoCupon);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idUsoCupon}")
    public UsoCupon buscarUsoCupon(@PathParam("idUsoCupon") int idUsoCupon) {
        UsoCupon resultado = null;

        try {
            resultado = usoCuponBL.buscarUsoCupon(idUsoCupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<UsoCupon> listarUsosCupon() {
        ArrayList<UsoCupon> resultado = new ArrayList<>();

        try {
            resultado = usoCuponBL.listarUsosCupon();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<UsoCupon> listarUsosPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<UsoCupon> resultado = new ArrayList<>();

        try {
            resultado = usoCuponBL.listarUsosPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCupon/{idCupon}")
    public ArrayList<UsoCupon> listarUsosPorCupon(@PathParam("idCupon") int idCupon) {
        ArrayList<UsoCupon> resultado = new ArrayList<>();

        try {
            resultado = usoCuponBL.listarUsosPorCupon(idCupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorPedido/{idPedido}")
    public UsoCupon buscarUsoPorPedido(@PathParam("idPedido") int idPedido) {
        UsoCupon resultado = null;

        try {
            resultado = usoCuponBL.buscarUsoPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("clienteYaUsoCupon/{idCliente}/{idCupon}")
    public int clienteYaUsoCupon(@PathParam("idCliente") int idCliente,
                                 @PathParam("idCupon") int idCupon) {
        int resultado = 0;

        try {
            boolean yaUso = usoCuponBL.clienteYaUsoCupon(idCliente, idCupon);

            if (yaUso) {
                resultado = 1;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
