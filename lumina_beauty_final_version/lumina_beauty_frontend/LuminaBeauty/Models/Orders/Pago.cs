using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class Pago
    {
        [JsonPropertyName("id_pago")]
        public int IdPago { get; set; }

        [JsonPropertyName("monto")]
        public decimal Monto { get; set; }

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = "PENDIENTE";

        [JsonPropertyName("referencia_transaccion")]
        public string ReferenciaTransaccion { get; set; } = string.Empty;

        [JsonPropertyName("fecha_pago")]
        public DateTime? FechaPago { get; set; }

        [JsonPropertyName("fecha_reembolso")]
        public DateTime? FechaReembolso { get; set; }

        [JsonPropertyName("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }

        [JsonPropertyName("fecha_actualizacion")]
        public DateTime? FechaActualizacion { get; set; }

        [JsonPropertyName("pedido")]
        public Pedido? Pedido { get; set; }

        [JsonPropertyName("metodoDePago")]
        public MetodoDePago? MetodoDePago { get; set; }
    }
}
