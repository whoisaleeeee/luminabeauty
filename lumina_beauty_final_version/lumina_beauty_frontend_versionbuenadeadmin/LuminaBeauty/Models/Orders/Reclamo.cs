using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class Reclamo
    {
        [JsonPropertyName("id_reclamo")]
        public int IdReclamo { get; set; }

        [JsonPropertyName("tipo")]
        public string Tipo { get; set; } = string.Empty;

        [JsonPropertyName("asunto")]
        public string Asunto { get; set; } = string.Empty;

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = "ABIERTO";

        [JsonPropertyName("prioridad")]
        public string Prioridad { get; set; } = "MEDIA";

        [JsonPropertyName("area_asignada")]
        public string AreaAsignada { get; set; } = string.Empty;

        [JsonPropertyName("resuelto_en")]
        public DateTime? ResueltoEn { get; set; }

        [JsonPropertyName("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }

        [JsonPropertyName("fecha_actualizacion")]
        public DateTime? FechaActualizacion { get; set; }

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }

        [JsonPropertyName("pedido")]
        public Pedido? Pedido { get; set; }

        public string Codigo => $"REC-{IdReclamo:D4}";
    }
}