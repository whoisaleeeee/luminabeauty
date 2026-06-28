using LuminaBeauty.Models;
using LuminaBeauty.Models;
using LuminaBeauty.Models;
using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class Valoracion
    {
        [JsonPropertyName("id_valoracion")]
        public int IdValoracion { get; set; }

        [JsonPropertyName("calificacion")]
        public int Calificacion { get; set; }

        [JsonPropertyName("comentario")]
        public string Comentario { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = string.Empty;

        [JsonPropertyName("respuesta_tienda")]
        public string RespuestaTienda { get; set; } = string.Empty;

        [JsonPropertyName("respondido_en")]
        public DateTime? RespondidoEn { get; set; }

        [JsonPropertyName("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }

        [JsonPropertyName("fecha_actualizacion")]
        public DateTime? FechaActualizacion { get; set; }

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }

        [JsonPropertyName("producto")]
        public Producto? Producto { get; set; }

        [JsonPropertyName("detallePedido")]
        public DetallePedido? DetallePedido { get; set; }
    }
}