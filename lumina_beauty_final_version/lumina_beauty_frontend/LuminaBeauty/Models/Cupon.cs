using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Cupon
    {
        [JsonPropertyName("id_cupon")]
        public int IdCupon { get; set; }

        [JsonPropertyName("codigo")]
        public string Codigo { get; set; } = string.Empty;

        [JsonPropertyName("tipo_descuento")]
        public string TipoDescuento { get; set; } = string.Empty;

        [JsonPropertyName("valor_descuento")]
        public decimal ValorDescuento { get; set; }

        [JsonPropertyName("fecha_inicio")]
        public DateTime? FechaInicio { get; set; }

        [JsonPropertyName("fecha_fin")]
        public DateTime? FechaFin { get; set; }

        [JsonPropertyName("limite_uso")]
        public int? LimiteUso { get; set; }

        [JsonPropertyName("estado")]
        public int Estado { get; set; }
    }
}