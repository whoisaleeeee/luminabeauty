using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Usuario
    {
        [JsonPropertyName("id_usuario")]
        public int Id { get; set; }

        [JsonPropertyName("nombres")]
        public string Nombre { get; set; } = string.Empty;

        [JsonPropertyName("apellidos")]
        public string Apellido { get; set; } = string.Empty;

        [JsonPropertyName("correo")]
        public string Correo { get; set; } = string.Empty;

        [JsonPropertyName("contrasena_hash")]
        public string Contrasena { get; set; } = string.Empty;

        [JsonPropertyName("telefono")]
        public string Telefono { get; set; } = string.Empty;

        [JsonPropertyName("dni")]
        public string Dni { get; set; } = string.Empty;

        [JsonPropertyName("tipo_usuario")]
        public string TipoUsuario { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public int Estado { get; set; } = 1;
    }
}
