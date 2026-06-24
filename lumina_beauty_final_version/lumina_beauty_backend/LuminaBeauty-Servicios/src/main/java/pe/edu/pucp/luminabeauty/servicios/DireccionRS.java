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

import pe.edu.pucp.luminaBeauty.Business.DireccionBL;
import pe.edu.pucp.luminaBeauty.Business.impl.DireccionBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Direccion;

import java.util.ArrayList;

@Path("DireccionRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DireccionRS {

    private DireccionBL direccionBL;

    public DireccionRS() {
        this.direccionBL = new DireccionBLImpl();
    }

    @POST
    @Path("registrar")
    public Direccion registrarDireccion(Direccion direccion) {
        Direccion resultado = null;

        try {
            resultado = direccionBL.registrarDireccion(direccion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Direccion actualizarDireccion(Direccion direccion) {
        Direccion resultado = null;

        try {
            resultado = direccionBL.actualizarDireccion(direccion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idDireccion}")
    public int eliminarDireccion(@PathParam("idDireccion") int idDireccion) {
        int resultado = 0;

        try {
            direccionBL.eliminarDireccion(idDireccion);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idDireccion}")
    public Direccion buscarDireccion(@PathParam("idDireccion") int idDireccion) {
        Direccion resultado = null;

        try {
            resultado = direccionBL.buscarDireccion(idDireccion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Direccion> listarDirecciones() {
        ArrayList<Direccion> resultado = new ArrayList<>();

        try {
            resultado = direccionBL.listarDirecciones();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<Direccion> listarDireccionesPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<Direccion> resultado = new ArrayList<>();

        try {
            resultado = direccionBL.listarDireccionesPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

