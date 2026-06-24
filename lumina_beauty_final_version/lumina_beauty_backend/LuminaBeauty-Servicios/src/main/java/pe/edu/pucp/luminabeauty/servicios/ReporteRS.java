package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.ReporteBL;
import pe.edu.pucp.luminaBeauty.Business.impl.ReporteBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;

import java.math.BigDecimal;
import java.util.ArrayList;

@Path("ReporteRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteRS {

    private ReporteBL reporteBL;

    public ReporteRS() {
        this.reporteBL = new ReporteBLImpl();
    }

    @GET
    @Path("contarClientesActivos")
    public int contarClientesActivos() {
        int resultado = 0;

        try {
            resultado = reporteBL.contarClientesActivos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("contarProductosActivos")
    public int contarProductosActivos() {
        int resultado = 0;

        try {
            resultado = reporteBL.contarProductosActivos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("contarPedidosPorEstado/{estado}")
    public int contarPedidosPorEstado(@PathParam("estado") String estado) {
        int resultado = 0;

        try {
            resultado = reporteBL.contarPedidosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("contarPagosPorEstado/{estado}")
    public int contarPagosPorEstado(@PathParam("estado") String estado) {
        int resultado = 0;

        try {
            resultado = reporteBL.contarPagosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("contarReclamosPorEstado/{estado}")
    public int contarReclamosPorEstado(@PathParam("estado") String estado) {
        int resultado = 0;

        try {
            resultado = reporteBL.contarReclamosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("contarEnviosPorEstado/{estado}")
    public int contarEnviosPorEstado(@PathParam("estado") String estado) {
        int resultado = 0;

        try {
            resultado = reporteBL.contarEnviosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("totalVentasCompletadas")
    public BigDecimal calcularTotalVentasCompletadas() {
        BigDecimal resultado = BigDecimal.ZERO;

        try {
            resultado = reporteBL.calcularTotalVentasCompletadas();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("totalVentasPorEstadoPedido/{estado}")
    public BigDecimal calcularTotalVentasPorEstadoPedido(@PathParam("estado") String estado) {
        BigDecimal resultado = BigDecimal.ZERO;

        try {
            resultado = reporteBL.calcularTotalVentasPorEstadoPedido(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("productosStockBajo/{umbralMinimo}")
    public ArrayList<Producto> listarProductosConStockBajo(@PathParam("umbralMinimo") int umbralMinimo) {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarProductosConStockBajo(umbralMinimo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("productosSinStock")
    public ArrayList<Producto> listarProductosSinStock() {
        ArrayList<Producto> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarProductosSinStock();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("pedidosPorEstado/{estado}")
    public ArrayList<Pedido> listarPedidosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Pedido> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarPedidosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("pagosPorEstado/{estado}")
    public ArrayList<Pago> listarPagosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Pago> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarPagosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("reclamosPorEstado/{estado}")
    public ArrayList<Reclamo> listarReclamosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Reclamo> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarReclamosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("enviosPorEstado/{estado}")
    public ArrayList<Envio> listarEnviosPorEstado(@PathParam("estado") String estado) {
        ArrayList<Envio> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarEnviosPorEstado(estado);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @GET
    @Path("clientesActivos")
    public ArrayList<Cliente> listarClientesActivos() {
        ArrayList<Cliente> resultado = new ArrayList<>();

        try {
            resultado = reporteBL.listarClientesActivos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }
}
