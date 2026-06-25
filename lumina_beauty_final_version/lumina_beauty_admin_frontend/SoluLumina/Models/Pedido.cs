using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Pedido
    {
        private int id_pedido;
        private String codigo_pedido;
        private String codigo_cupon_aplicado;
        private Decimal subtotal_productos;
        private Decimal costo_envio;
        private Decimal descuento;
        private Decimal total;
        private String estado;
        private DateTime fecha_creacion;
        private DateTime fecha_actualizacion;

        private Cliente cliente;
        private Cupon cupon;

        private List<DetallePedido> detalles;

        [JsonProperty("id_pedido")]
        public int Id_pedido { get => id_pedido; set => id_pedido = value; }
        [JsonProperty("codigo_pedido")]
        public string Codigo_pedido { get => codigo_pedido; set => codigo_pedido = value; }
        [JsonProperty("codigo_cupon_aplicado")]
        public string Codigo_cupon_aplicado { get => codigo_cupon_aplicado; set => codigo_cupon_aplicado = value; }
        [JsonProperty("subtotal_productos")]
        public decimal Subtotal_productos { get => subtotal_productos; set => subtotal_productos = value; }
        [JsonProperty("costo_envio")]
        public decimal Costo_envio { get => costo_envio; set => costo_envio = value; }
        [JsonProperty("descuento")]
        public decimal Descuento { get => descuento; set => descuento = value; }
        [JsonProperty("total")]
        public decimal Total { get => total; set => total = value; }
        [JsonProperty("estado")]
        public string Estado { get => estado; set => estado = value; }
        [JsonProperty("fecha_creacion")]
        public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
        [JsonProperty("fecha_actualizacion")]
        public DateTime Fecha_actualizacion { get => fecha_actualizacion; set => fecha_actualizacion = value; }
        [JsonProperty("cliente")]
        public Cliente Cliente { get => cliente; set => cliente = value; }
        [JsonProperty("cupon")]
        public Cupon Cupon { get => cupon; set => cupon = value; }
        [JsonProperty("detalles")]
        public List<DetallePedido> Detalles { get => detalles; set => detalles = value; }
    }
}
