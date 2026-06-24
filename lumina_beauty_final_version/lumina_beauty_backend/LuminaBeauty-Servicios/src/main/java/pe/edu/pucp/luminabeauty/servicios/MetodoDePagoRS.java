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

import pe.edu.pucp.luminaBeauty.Business.MetodoDePagoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.MetodoDePagoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;

import java.util.ArrayList;

@Path("MetodoDePagoRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetodoDePagoRS {

    private MetodoDePagoBL metodoDePagoBL;

    public MetodoDePagoRS() {
        this.metodoDePagoBL = new MetodoDePagoBLImpl();
    }

    @POST
    @Path("registrar")
    public MetodoDePago registrarMetodoDePago(MetodoDePago metodoDePago) {
        MetodoDePago resultado = null;

        try {
            resultado = metodoDePagoBL.registrarMetodoDePago(metodoDePago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public MetodoDePago actualizarMetodoDePago(MetodoDePago metodoDePago) {
        MetodoDePago resultado = null;

        try {
            resultado = metodoDePagoBL.actualizarMetodoDePago(metodoDePago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idMetodoPago}")
    public int eliminarMetodoDePago(@PathParam("idMetodoPago") int idMetodoPago) {
        int resultado = 0;

        try {
            metodoDePagoBL.eliminarMetodoDePago(idMetodoPago);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idMetodoPago}")
    public MetodoDePago buscarMetodoDePago(@PathParam("idMetodoPago") int idMetodoPago) {
        MetodoDePago resultado = null;

        try {
            resultado = metodoDePagoBL.buscarMetodoDePago(idMetodoPago);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<MetodoDePago> listarMetodosDePago() {
        ArrayList<MetodoDePago> resultado = new ArrayList<>();

        try {
            resultado = metodoDePagoBL.listarMetodosDePago();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarActivos")
    public ArrayList<MetodoDePago> listarMetodosDePagoActivos() {
        ArrayList<MetodoDePago> resultado = new ArrayList<>();

        try {
            resultado = metodoDePagoBL.listarMetodosDePagoActivos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorNombre/{nombre}")
    public ArrayList<MetodoDePago> buscarMetodosDePagoPorNombre(@PathParam("nombre") String nombre) {
        ArrayList<MetodoDePago> resultado = new ArrayList<>();

        try {
            resultado = metodoDePagoBL.buscarMetodosDePagoPorNombre(nombre);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
