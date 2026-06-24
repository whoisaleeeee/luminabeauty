using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Cupon
    {
        [JsonPropertyName("id_cupon")]
        public int IdCupon { get; set; }

        [JsonPropertyName("codigo")]
        public string Codigo { get; set; } = string.Empty;
    }
}