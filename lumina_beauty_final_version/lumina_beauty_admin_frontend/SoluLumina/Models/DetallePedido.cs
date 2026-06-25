using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class DetallePedido
    {
        private int id_detalle_pedido;
        private string nombre_producto;
        private string sku_producto;
        private int cantidad;
        private decimal precio_unitario;

        [JsonProperty("id_detalle_pedido")]
        public int Id_detalle_pedido { get => id_detalle_pedido; set => id_detalle_pedido = value; }

        [JsonProperty("nombre_producto")]
        public string Nombre_producto { get => nombre_producto; set => nombre_producto = value; }

        [JsonProperty("sku_producto")]
        public string Sku_producto { get => sku_producto; set => sku_producto = value; }

        [JsonProperty("cantidad")]
        public int Cantidad { get => cantidad; set => cantidad = value; }

        [JsonProperty("precioUnitario")]
        public decimal PrecioUnitario { get => precio_unitario; set => precio_unitario = value; }

        public decimal Subtotal => Cantidad * PrecioUnitario;
    }
}
