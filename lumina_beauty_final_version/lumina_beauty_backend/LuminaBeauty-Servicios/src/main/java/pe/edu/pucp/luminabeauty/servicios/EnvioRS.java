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

import pe.edu.pucp.luminaBeauty.Business.EnvioBL;
import pe.edu.pucp.luminaBeauty.Business.impl.EnvioBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Envio;

import java.util.ArrayList;

@Path("EnvioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnvioRS {

    private EnvioBL envioBL;

    public EnvioRS() {
        this.envioBL = new EnvioBLImpl();
    }

    @POST
    @Path("registrar")
    public Envio registrarEnvio(Envio envio) {
        Envio resultado = null;

        try {
            resultado = envioBL.registrarEnvio(envio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Envio actualizarEnvio(Envio envio) {
        Envio resultado = null;

        try {
            resultado = envioBL.actualizarEnvio(envio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idEnvio}")
    public int eliminarEnvio(@PathParam("idEnvio") int idEnvio) {
        int resultado = 0;

        try {
            envioBL.eliminarEnvio(idEnvio);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idEnvio}")
    public Envio buscarEnvio(@PathParam("idEnvio") int idEnvio) {
        Envio resultado = null;

        try {
            resultado = envioBL.buscarEnvio(idEnvio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Envio> listarEnvios() {
        ArrayList<Envio> resultado = new ArrayList<>();

        try {
            resultado = envioBL.listarEnvios();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorEstado/{estado}")
    public ArrayList<Envio> listarEnviosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Envio> resultado = new ArrayList<>();

        try {
            resultado = envioBL.listarEnviosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorZona/{zonaEnvio}")
    public ArrayList<Envio> listarEnviosPorZona(@PathParam("zonaEnvio") String zonaEnvio) {
        ArrayList<Envio> resultado = new ArrayList<>();

        try {
            resultado = envioBL.listarEnviosPorZona(zonaEnvio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorPedido/{idPedido}")
    public Envio buscarEnvioPorPedido(@PathParam("idPedido") int idPedido) {
        Envio resultado = null;

        try {
            resultado = envioBL.buscarEnvioPorPedido(idPedido);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("despachar/{idEnvio}")
    public Envio despacharEnvio(@PathParam("idEnvio") int idEnvio,
                                @QueryParam("numeroSeguimiento") String numeroSeguimiento) {
        Envio resultado = null;

        try {
            resultado = envioBL.despacharEnvio(idEnvio, numeroSeguimiento);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("enTransito/{idEnvio}")
    public Envio marcarEnvioEnTransito(@PathParam("idEnvio") int idEnvio) {
        Envio resultado = null;

        try {
            resultado = envioBL.marcarEnvioEnTransito(idEnvio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("entregado/{idEnvio}")
    public Envio marcarEnvioEntregado(@PathParam("idEnvio") int idEnvio) {
        Envio resultado = null;

        try {
            resultado = envioBL.marcarEnvioEntregado(idEnvio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("devuelto/{idEnvio}")
    public Envio marcarEnvioDevuelto(@PathParam("idEnvio") int idEnvio) {
        Envio resultado = null;

        try {
            resultado = envioBL.marcarEnvioDevuelto(idEnvio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
