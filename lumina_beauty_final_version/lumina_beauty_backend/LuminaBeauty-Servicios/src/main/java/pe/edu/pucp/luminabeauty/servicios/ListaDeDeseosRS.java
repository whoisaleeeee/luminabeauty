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

import pe.edu.pucp.luminaBeauty.Business.ListaDeDeseosBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ListaDeDeseosBLImpl;
import pe.edu.pucp.luminaBeauty.Model.ListaDeDeseos;

import java.util.ArrayList;

@Path("ListaDeDeseosRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListaDeDeseosRS {

    private ListaDeDeseosBL listaDeDeseosBL;

    public ListaDeDeseosRS() {
        this.listaDeDeseosBL = new ListaDeDeseosBLImpl();
    }

    @POST
    @Path("registrar")
    public ListaDeDeseos registrarListaDeDeseos(ListaDeDeseos listaDeDeseos) {
        ListaDeDeseos resultado = null;

        try {
            resultado = listaDeDeseosBL.registrarListaDeDeseos(listaDeDeseos);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @PUT
    @Path("actualizar")
    public ListaDeDeseos actualizarListaDeDeseos(ListaDeDeseos listaDeDeseos) {
        ListaDeDeseos resultado = null;

        try {
            resultado = listaDeDeseosBL.actualizarListaDeDeseos(listaDeDeseos);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idListaDeDeseos}")
    public int eliminarListaDeDeseos(@PathParam("idListaDeDeseos") int idListaDeDeseos) {
        int resultado = 0;

        try {
            listaDeDeseosBL.eliminarListaDeDeseos(idListaDeDeseos);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idListaDeDeseos}")
    public ListaDeDeseos buscarListaDeDeseos(@PathParam("idListaDeDeseos") int idListaDeDeseos) {
        ListaDeDeseos resultado = null;

        try {
            resultado = listaDeDeseosBL.buscarListaDeDeseos(idListaDeDeseos);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<ListaDeDeseos> listarListasDeDeseos() {
        ArrayList<ListaDeDeseos> resultado = new ArrayList<>();

        try {
            resultado = listaDeDeseosBL.listarListasDeDeseos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public ArrayList<ListaDeDeseos> listarListasPorCliente(@PathParam("idCliente") int idCliente) {
        ArrayList<ListaDeDeseos> resultado = new ArrayList<>();

        try {
            resultado = listaDeDeseosBL.listarListasPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorNombre/{nombre}")
    public ArrayList<ListaDeDeseos> buscarListasPorNombre(@PathParam("nombre") String nombre) {
        ArrayList<ListaDeDeseos> resultado = new ArrayList<>();

        try {
            resultado = listaDeDeseosBL.buscarListasPorNombre(nombre);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
