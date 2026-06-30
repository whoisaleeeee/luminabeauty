using System;
using System.Collections.Generic;
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
            var url = $"{_urlBase}/ReporteJasperRS/generar/{codigoReporte}";

            if (fechaInicio.HasValue && fechaFin.HasValue)
            {
                url +=
                    $"?fechaInicio={fechaInicio.Value:yyyy-MM-dd}" +
                    $"&fechaFin={fechaFin.Value:yyyy-MM-dd}";
            }

            return url;
        }
    }

    public record ReporteJasperOption(
        string Codigo,
        string Nombre,
        bool RequiereFechas);
}