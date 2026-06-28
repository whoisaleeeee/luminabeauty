using LuminaBeauty.Models;
using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class MovimientoPuntosFidelidad
    {
        [JsonPropertyName("id_movimiento_puntos")]
        public int IdMovimientoPuntos { get; set; }

        [JsonPropertyName("tipo_movimiento")]
        public string TipoMovimiento { get; set; } = string.Empty;

        [JsonPropertyName("puntos")]
        public int Puntos { get; set; }

        [JsonPropertyName("saldo_anterior")]
        public int SaldoAnterior { get; set; }

        [JsonPropertyName("saldo_posterior")]
        public int SaldoPosterior { get; set; }

        [JsonPropertyName("motivo")]
        public string Motivo { get; set; } = string.Empty;

        [JsonPropertyName("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }

        [JsonPropertyName("pedido")]
        public Pedido? Pedido { get; set; }
    }
}