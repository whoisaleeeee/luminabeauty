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

import pe.edu.pucp.luminaBeauty.Business.ValoracionBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ValoracionBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Valoracion;

import java.util.ArrayList;

@Path("ValoracionRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ValoracionRS {

    private ValoracionBL valoracionBL;

    public ValoracionRS() {
        this.valoracionBL = new ValoracionBLImpl();
    }

    @POST
    @Path("registrar")
    public Valoracion registrarValoracion(Valoracion valoracion) {
        Valoracion resultado = null;

        try {
            resultado = valoracionBL.registrarValoracion(valoracion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Valoracion actualizarValoracion(Valoracion valoracion) {
        Valoracion resultado = null;

        try {
            resultado = valoracionBL.actualizarValoracion(valoracion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idValoracion}")
    public int eliminarValoracion(@PathParam("idValoracion") int idValoracion) {
        int resultado = 0;

        try {
            valoracionBL.eliminarValoracion(idValoracion);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idValoracion}")
    public Valoracion buscarValoracion(@PathParam("idValoracion") int idValoracion) {
        Valoracion resultado = null;

        try {
            resultado = valoracionBL.buscarValoracion(idValoracion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Valoracion> listarValoraciones() {
        ArrayList<Valoracion> resultado = new ArrayList<>();

        try {
            resultado = valoracionBL.listarValoraciones();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<Valoracion> listarValoracionesPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<Valoracion> resultado = new ArrayList<>();

        try {
            resultado = valoracionBL.listarValoracionesPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorProducto/{idProducto}")
    public ArrayList<Valoracion> listarValoracionesPorProducto(@PathParam("idProducto") int idProducto) {
        ArrayList<Valoracion> resultado = new ArrayList<>();

        try {
            resultado = valoracionBL.listarValoracionesPorProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEstado/{estado}")
    public ArrayList<Valoracion> listarValoracionesPorEstado(@PathParam("estado") String estado) {
        ArrayList<Valoracion> resultado = new ArrayList<>();

        try {
            resultado = valoracionBL.listarValoracionesPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("publicar/{idValoracion}")
    public Valoracion publicarValoracion(@PathParam("idValoracion") int idValoracion) {
        Valoracion resultado = null;

        try {
            resultado = valoracionBL.publicarValoracion(idValoracion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("rechazar/{idValoracion}")
    public Valoracion rechazarValoracion(@PathParam("idValoracion") int idValoracion) {
        Valoracion resultado = null;

        try {
            resultado = valoracionBL.rechazarValoracion(idValoracion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("responder/{idValoracion}/{idEmpleado}")
    public Valoracion responderValoracion(@PathParam("idValoracion") int idValoracion,
                                          @PathParam("idEmpleado") int idEmpleado,
                                          @QueryParam("respuesta") String respuesta) {
        Valoracion resultado = null;

        try {
            resultado = valoracionBL.responderValoracion(idValoracion, respuesta, idEmpleado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPublicadasPorProducto/{idProducto}")
    public ArrayList<Valoracion> listarPublicadasPorProducto(
            @PathParam("idProducto") int idProducto
    ) {
        ArrayList<Valoracion> resultado = new ArrayList<>();

        try {
            resultado = valoracionBL
                    .listarPublicadasPorProducto(idProducto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
