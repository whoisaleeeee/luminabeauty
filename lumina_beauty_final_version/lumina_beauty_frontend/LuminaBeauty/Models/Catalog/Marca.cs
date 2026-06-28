using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class Marca
    {
        [JsonPropertyName("id_marca")]
        public int IdMarca { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = string.Empty;

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = string.Empty;

        [JsonPropertyName("logo_url")]
        public string Logo { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public int Estado { get; set; }
    }
}
