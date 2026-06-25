using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Direccion
    {
        [JsonPropertyName("id_direccion")]
        public int IdDireccion { get; set; }

        [JsonPropertyName("direccion")]
        public string DireccionTexto { get; set; } = string.Empty;

        [JsonPropertyName("ciudad")]
        public string Ciudad { get; set; } = string.Empty;

        [JsonPropertyName("pais")]
        public string Pais { get; set; } = string.Empty;

        [JsonPropertyName("referencia")]
        public string Referencia { get; set; } = string.Empty;

        [JsonPropertyName("codigo_postal")]
        public string CodigoPostal { get; set; } = string.Empty;

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }
    }
}