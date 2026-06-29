using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class MetodoDePago
    {
        [JsonPropertyName("id_metodo_pago")]
        public int IdMetodoPago { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = string.Empty;
    }
}
