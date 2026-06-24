using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Producto
    {
        private int id_producto;
        private String nombre;
        private String sku;
        private String slug;
        private String descripcion;
        private Decimal precio;
        private int stock;
        private String tipo_piel;
        private String imagen_url;
        private int estado;
        private DateTime fecha_creacion;
        private DateTime fecha_actualizacion;

        private CategoriaProducto categoria;
        private Marca marca;

        [JsonProperty("id_producto")]
        public int Id_producto { get => id_producto; set => id_producto = value; }
        [JsonProperty("nombre")]
        public string Nombre { get => nombre; set => nombre = value; }
        [JsonProperty("sku")]
        public string Sku { get => sku; set => sku = value; }
        [JsonProperty("slug")]
        public string Slug { get => slug; set => slug = value; }
        [JsonProperty("descripcion")]
        public string Descripcion { get => descripcion; set => descripcion = value; }
        [JsonProperty("precio")]
        public decimal Precio { get => precio; set => precio = value; }
        [JsonProperty("stock")]
        public int Stock { get => stock; set => stock = value; }
        [JsonProperty("tipoPiel")]
        public string Tipo_piel { get => tipo_piel; set => tipo_piel = value; }
        [JsonProperty("imagen_url")]
        public string Imagen_url { get => imagen_url; set => imagen_url = value; }
        [JsonProperty("estado")]
        public int Estado { get => estado; set => estado = value; }
        [JsonProperty("fecha_creacion")]
        public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
        [JsonProperty("fecha_actualizacion")]
        public DateTime Fecha_actualizacion { get => fecha_actualizacion; set => fecha_actualizacion = value; }
        [JsonProperty("categoria")]
        public CategoriaProducto Categoria { get => categoria; set => categoria = value; }
        [JsonProperty("marca")]
        public Marca Marca { get => marca; set => marca = value; }
    }
}
