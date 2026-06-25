using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class CategoriaProducto
    {
        [JsonPropertyName("id_categoria")]
        public int IdCategoria { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = string.Empty;

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public int Estado { get; set; }
    }
}
