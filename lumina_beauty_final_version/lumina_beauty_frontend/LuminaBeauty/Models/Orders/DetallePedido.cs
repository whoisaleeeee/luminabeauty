using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class DetallePedido
    {
        [JsonPropertyName("id_detalle_pedido")]
        public int IdDetallePedido { get; set; }

        [JsonPropertyName("nombre_producto")]
        public string NombreProducto { get; set; } = string.Empty;

        [JsonPropertyName("sku_producto")]
        public string SkuProducto { get; set; } = string.Empty;

        [JsonPropertyName("producto")]
        public Producto? Producto { get; set; }

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precioUnitario")]
        public decimal PrecioUnitario { get; set; }

        private decimal _subtotal;

        [JsonPropertyName("subtotal")]
        public decimal Subtotal
        {
            get => _subtotal > 0 ? _subtotal : PrecioUnitario * Cantidad;
            set => _subtotal = value;
        }
    }
}