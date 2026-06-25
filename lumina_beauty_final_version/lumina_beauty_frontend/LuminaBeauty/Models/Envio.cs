using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Envio
    {
        [JsonPropertyName("id_envio")]
        public int IdEnvio { get; set; }

        [JsonPropertyName("zona_envio")]
        public string ZonaEnvio { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = string.Empty;

        [JsonPropertyName("numero_seguimiento")]
        public string NumeroSeguimiento { get; set; } = string.Empty;

        [JsonPropertyName("direccion_envio")]
        public string DireccionEnvio { get; set; } = string.Empty;

        [JsonPropertyName("ciudad_envio")]
        public string CiudadEnvio { get; set; } = string.Empty;

        [JsonPropertyName("pais_envio")]
        public string PaisEnvio { get; set; } = string.Empty;

        [JsonPropertyName("referencia_envio")]
        public string ReferenciaEnvio { get; set; } = string.Empty;

        [JsonPropertyName("codigo_postal_envio")]
        public string CodigoPostalEnvio { get; set; } = string.Empty;

        [JsonPropertyName("fecha_envio")]
        public DateTime? FechaEnvio { get; set; }

        [JsonPropertyName("fecha_entrega_estimada")]
        public DateTime? FechaEntregaEstimada { get; set; }

        [JsonPropertyName("fecha_entrega_real")]
        public DateTime? FechaEntregaReal { get; set; }

        [JsonPropertyName("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }

        [JsonPropertyName("fecha_actualizacion")]
        public DateTime? FechaActualizacion { get; set; }

        [JsonPropertyName("pedido")]
        public Pedido? Pedido { get; set; }
    }
}