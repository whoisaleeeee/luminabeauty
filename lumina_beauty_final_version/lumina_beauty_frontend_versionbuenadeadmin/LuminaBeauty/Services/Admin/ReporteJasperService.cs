using Microsoft.Extensions.Configuration;

namespace LuminaBeauty.Services.Admin
{
    public class ReporteJasperService
    {
        private readonly string _urlBase;

        public ReporteJasperService(IConfiguration configuration)
        {
            _urlBase = configuration["ApiSettings:URL_BASE"]?.TrimEnd('/') ?? string.Empty;
        }

        public IReadOnlyList<ReporteJasperOption> ObtenerReportes()
        {
            return new List<ReporteJasperOption>
            {
                new("ventas-consolidadas", "Ventas Consolidadas", true),
                new("inventario-general", "Inventario General", false),
                new("rendimiento-por-marca", "Rendimiento por Marca", true)
            };
        }

        public string ConstruirUrlGeneracion(
            string codigoReporte,
            DateTime? fechaInicio,
            DateTime? fechaFin)
        {
            string codigo = Uri.EscapeDataString(codigoReporte);
            string url = $"{_urlBase}/ReporteJasperRS/generar/{codigo}";

            ReporteJasperOption? reporte = ObtenerReportes()
                .FirstOrDefault(r => r.Codigo == codigoReporte);

            if (reporte?.RequiereFechas == true)
            {
                string inicio = fechaInicio?.ToString("yyyy-MM-dd") ?? string.Empty;
                string fin = fechaFin?.ToString("yyyy-MM-dd") ?? string.Empty;

                url += $"?fechaInicio={Uri.EscapeDataString(inicio)}&fechaFin={Uri.EscapeDataString(fin)}";
            }

            return url;
        }
    }

    public record ReporteJasperOption(
        string Codigo,
        string Nombre,
        bool RequiereFechas);
}
