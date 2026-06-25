using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Pedido
    {
        [JsonPropertyName("id_pedido")]
        public int IdPedido { get; set; }

        [JsonPropertyName("codigo_pedido")]
        public string CodigoPedido { get; set; } = string.Empty;

        [JsonPropertyName("codigo_cupon_aplicado")]
        public string CodigoCuponAplicado { get; set; } = string.Empty;

        [JsonPropertyName("subtotal_productos")]
        public decimal SubtotalProductos { get; set; }

        [JsonPropertyName("costo_envio")]
        public decimal CostoEnvio { get; set; }

        [JsonPropertyName("descuento")]
        public decimal Descuento { get; set; }

        [JsonPropertyName("total")]
        public decimal Total { get; set; }

        [JsonPropertyName("estado")]
        public string Estado { get; set; } = "PENDIENTE";

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }

        [JsonPropertyName("cupon")]
        public Cupon? Cupon { get; set; }

        [JsonPropertyName("detalles")]
        public List<DetallePedido> Detalles { get; set; } = new();
    }
}