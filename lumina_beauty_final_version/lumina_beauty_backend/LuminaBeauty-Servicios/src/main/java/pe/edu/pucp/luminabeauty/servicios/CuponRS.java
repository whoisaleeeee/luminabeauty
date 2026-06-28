    package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.CuponBL;
import pe.edu.pucp.luminaBeauty.Business.impl.CuponBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Cupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Path("CuponRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuponRS {

    private CuponBL cuponBL;

    public CuponRS() {
        this.cuponBL = new CuponBLImpl();
    }

    @POST
    @Path("registrar")
    public Cupon registrarCupon(Cupon cupon) {
        Cupon resultado = null;

        try {
            resultado = cuponBL.registrarCupon(cupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @POST
    @Path("registrarParams")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Cupon registrarCuponParams(
            @QueryParam("codigo") String codigo,
            @QueryParam("tipo_descuento") String tipoDescuento,
            @QueryParam("valor_descuento") String valorDescuento,
            @QueryParam("fecha_inicio") String fechaInicio,
            @QueryParam("fecha_fin") String fechaFin,
            @QueryParam("limite_uso") String limiteUso
    ) {
        Cupon resultado = null;
        try {
            Cupon cupon = new Cupon();
            cupon.setCodigo(codigo);
            cupon.setTipo_descuento(tipoDescuento);
            cupon.setValor_descuento(new BigDecimal(valorDescuento != null && !valorDescuento.isEmpty() ? valorDescuento : "0"));

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            if (fechaInicio != null && !fechaInicio.isEmpty()) {
                cupon.setFecha_inicio(LocalDateTime.parse(fechaInicio.substring(0, 19), fmt));
            }
            if (fechaFin != null && !fechaFin.isEmpty()) {
                cupon.setFecha_fin(LocalDateTime.parse(fechaFin.substring(0, 19), fmt));
            }
            if (limiteUso != null && !limiteUso.isEmpty()) {
                cupon.setLimite_uso(Integer.parseInt(limiteUso));
            }
            cupon.setEstado(1);

            resultado = cuponBL.registrarCupon(cupon);
        } catch (Exception ex) {
            System.out.println("Error registrarParams: " + ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("actualizar")
    public Cupon actualizarCupon(Cupon cupon) {
        Cupon resultado = null;

        try {
            resultado = cuponBL.actualizarCupon(cupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @DELETE
    @Path("eliminar/{idCupon}")
    public int eliminarCupon(@PathParam("idCupon") int idCupon) {
        int resultado = 0;

        try {
            cuponBL.eliminarCupon(idCupon);
            resultado = 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscar/{idCupon}")
    public Cupon buscarCupon(@PathParam("idCupon") int idCupon) {
        Cupon resultado = null;

        try {
            resultado = cuponBL.buscarCupon(idCupon);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listar")
    public ArrayList<Cupon> listarCupones() {
        ArrayList<Cupon> resultado = new ArrayList<>();

        try {
            resultado = cuponBL.listarCupones();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("listarActivos")
    public ArrayList<Cupon> listarCuponesActivos() {
        ArrayList<Cupon> resultado = new ArrayList<>();

        try {
            resultado = cuponBL.listarCuponesActivos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("buscarPorCodigo/{codigo}")
    public Cupon buscarCuponPorCodigo(@PathParam("codigo") String codigo) {
        Cupon resultado = null;

        try {
            resultado = cuponBL.buscarCuponPorCodigo(codigo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("validar/{codigo}")
    public int validarCupon(@PathParam("codigo") String codigo) {
        int resultado = 0;

        try {
            boolean valido = cuponBL.validarCupon(codigo);

            if (valido) {
                resultado = 1;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("aplicar/{codigo}")
    public Cupon aplicarCupon(@PathParam("codigo") String codigo) {
        Cupon resultado = null;

        try {
            resultado = cuponBL.aplicarCupon(codigo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}

