using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class DetallePedido
    {
        [JsonPropertyName("id_detalle_pedido")]
        public int IdDetallePedido { get; set; }

        [JsonPropertyName("producto")]
        public Producto? Producto { get; set; }

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precio_unitario")]
        public decimal PrecioUnitario { get; set; }

        [JsonPropertyName("subtotal")]
        public decimal Subtotal { get; set; }
    }
}