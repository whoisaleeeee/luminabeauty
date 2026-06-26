using Newtonsoft.Json;
using System.Text.Json;

namespace SoluLumina.Models
{
    public class MovimientoInventario
    {
        private int id_movimiento_inventario;
        private String tipo_movimiento;
        private int cantidad;
        private int stock_anterior;
        private int stock_posterior;
        private String motivo;
        private DateTime fecha_creacion;

        private Producto producto;
        private Pedido pedido;
        private Devolucion devolucion;
        private Empleado registrado_por;

        [JsonProperty("id_movimiento_inventario")]
        public int Id_movimiento_inventario { get => id_movimiento_inventario; set => id_movimiento_inventario = value; }
        [JsonProperty("tipo_movimiento")]
        public string Tipo_movimiento { get => tipo_movimiento; set => tipo_movimiento = value; }
        [JsonProperty("cantidad")]
        public int Cantidad { get => cantidad; set => cantidad = value; }
        [JsonProperty("stock_anterior")]
        public int Stock_anterior { get => stock_anterior; set => stock_anterior = value; }
        [JsonProperty("stock_posterior")]
        public int Stock_posterior { get => stock_posterior; set => stock_posterior = value; }
        [JsonProperty("motivo")]
        public string Motivo { get => motivo; set => motivo = value; }
        [JsonProperty("fecha_creacion")]
        public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
        [JsonProperty("producto")]
        public Producto Producto { get => producto; set => producto = value; }
        [JsonProperty("pedido")]
        public Pedido Pedido { get => pedido; set => pedido = value; }
        [JsonProperty("devolucion")]
        public Devolucion Devolucion { get => devolucion; set => devolucion = value; }
        [JsonProperty("registrado_por")]
        public Empleado Registrado_por { get => registrado_por; set => registrado_por = value; }
    }
}
