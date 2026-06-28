package pe.edu.pucp.luminabeauty.servicios;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import pe.edu.pucp.luminaBeauty.dbManager.DBManager;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("ReporteJasperRS")
public class ReporteJasperRS {

    private static final String PARAM_FECHA_INICIO = "FechaIni";
    private static final String PARAM_FECHA_FIN = "FechaFin";

    private static final Map<String, ReporteConfig> REPORTES = crearReportes();

    @Context
    private ServletContext servletContext;

    @GET
    @Path("listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReporteDisponible> listarReportes() {
        List<ReporteDisponible> reportes = new ArrayList<>();

        for (ReporteConfig config : REPORTES.values()) {
            reportes.add(new ReporteDisponible(
                    config.codigo(),
                    config.nombre(),
                    config.archivoJasper()
            ));
        }

        return reportes ;
    }

    @GET
    @Path("ventasConsolidadas")
    @Produces("application/pdf")
    public Response generarVentasConsolidadas(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        return generarReporte("ventas-consolidadas", fechaInicio, fechaFin);
    }

    @GET
    @Path("inventarioGeneral")
    @Produces("application/pdf")
    public Response generarInventarioGeneral() {
        return generarReporte("inventario-general", null, null);
    }

    @GET
    @Path("rendimientoPorMarca")
    @Produces("application/pdf")
    public Response generarRendimientoPorMarca(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        return generarReporte("rendimiento-por-marca", fechaInicio, fechaFin);
    }

    @GET
    @Path("generar/{codigoReporte}")
    @Produces("application/pdf")
    public Response generarReportePorCodigo(
            @PathParam("codigoReporte") String codigoReporte,
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        return generarReporte(codigoReporte, fechaInicio, fechaFin);
    }

    private Response generarReporte(
            String codigoReporte,
            String fechaInicio,
            String fechaFin) {

        try {
            ReporteConfig config = buscarReporte(codigoReporte);

            LocalDate inicio = null;
            LocalDate fin = null;

            if (config.requiereFechas()) {
                inicio = validarFecha(fechaInicio, "fechaInicio");
                fin = validarFecha(fechaFin, "fechaFin");

                if (fin.isBefore(inicio)) {
                    return badRequest("La fecha fin no puede ser menor que la fecha inicio.");
                }
            }

            String reportsPath = obtenerRutaReports();
            String jasperPath = reportsPath + File.separator + config.archivoJasper();

            File archivoJasper = new File(jasperPath);
            if (!archivoJasper.exists()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontro el archivo Jasper: " + config.archivoJasper())
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }

            Map<String, Object> parametros = new HashMap<>();

            if (config.requiereFechas()) {
                parametros.put(PARAM_FECHA_INICIO, Date.valueOf(inicio));
                parametros.put(PARAM_FECHA_FIN, Date.valueOf(fin));
            }

            parametros.put("SUBREPORT_DIR", reportsPath + File.separator);

            byte[] pdf;

            try (Connection connection = DBManager.getInstance().getConnection()) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperPath,
                        parametros,
                        connection
                );

                pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            }

            String nombreArchivo = construirNombreArchivo(config, inicio, fin);

            return Response.ok(pdf, "application/pdf")
                    .header("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"")
                    .build();

        } catch (IllegalArgumentException ex) {
            return badRequest(ex.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el reporte: " + ex.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    private ReporteConfig buscarReporte(String codigoReporte) {
        if (codigoReporte == null || codigoReporte.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del reporte es obligatorio.");
        }

        ReporteConfig config = REPORTES.get(codigoReporte.trim());

        if (config == null) {
            throw new IllegalArgumentException("Reporte no soportado: " + codigoReporte);
        }

        return config;
    }

    private String obtenerRutaReports() {
        String reportsPath = servletContext.getRealPath("/WEB-INF/reports");

        if (reportsPath == null || reportsPath.trim().isEmpty()) {
            throw new IllegalStateException("No se pudo resolver la ruta /WEB-INF/reports.");
        }

        return reportsPath;
    }

    private LocalDate validarFecha(String valor, String nombreParametro) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El parametro " + nombreParametro + " es obligatorio.");
        }

        try {
            return LocalDate.parse(valor.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "El parametro " + nombreParametro + " debe tener formato yyyy-MM-dd."
            );
        }
    }

    private Response badRequest(String mensaje) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(mensaje)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    private static Map<String, ReporteConfig> crearReportes() {
        Map<String, ReporteConfig> reportes = new LinkedHashMap<>();

        reportes.put(
                "ventas-consolidadas",
                new ReporteConfig(
                        "ventas-consolidadas",
                        "Ventas consolidadas",
                        "VentasConsolidadas.jasper",
                        "ventas_consolidadas",
                        true
                )
        );

        reportes.put(
                "inventario-general",
                new ReporteConfig(
                        "inventario-general",
                        "Inventario general",
                        "InventarioGeneral.jasper",
                        "inventario_general",
                        false
                )
        );

        reportes.put(
                "rendimiento-por-marca",
                new ReporteConfig(
                        "rendimiento-por-marca",
                        "Rendimiento por marca",
                        "RendimientoPorMarca.jasper",
                        "rendimiento_por_marca",
                        true
                )
        );

        return Collections.unmodifiableMap(reportes);
    }

    private String construirNombreArchivo(
            ReporteConfig config,
            LocalDate inicio,
            LocalDate fin) {

        if (config.requiereFechas()) {
            return config.prefijoArchivo()
                    + "_" + inicio
                    + "_a_" + fin
                    + ".pdf";
        }

        return config.prefijoArchivo() + ".pdf";
    }

    private record ReporteConfig(
            String codigo,
            String nombre,
            String archivoJasper,
            String prefijoArchivo,
            boolean requiereFechas) {
    }

    public static class ReporteDisponible {
        private String codigo;
        private String nombre;
        private String archivoJasper;

        public ReporteDisponible() {
        }

        public ReporteDisponible(String codigo, String nombre, String archivoJasper) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.archivoJasper = archivoJasper;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getArchivoJasper() {
            return archivoJasper;
        }

        public void setArchivoJasper(String archivoJasper) {
            this.archivoJasper = archivoJasper;
        }
    }
}
