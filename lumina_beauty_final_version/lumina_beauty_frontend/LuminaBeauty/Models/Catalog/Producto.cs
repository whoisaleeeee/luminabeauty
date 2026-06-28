using System.Text.Json.Serialization;

namespace LuminaBeauty.Models
{
    public class Producto
    {
        [JsonIgnore]
        public string Id
        {
            get
            {
                if (!string.IsNullOrWhiteSpace(Slug))
                {
                    return Slug;
                }

                if (!string.IsNullOrWhiteSpace(Sku))
                {
                    return Sku;
                }

                return IdProducto.ToString();
            }
        }

        [JsonPropertyName("id_producto")]
        public int IdProducto { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; } = string.Empty;

        [JsonPropertyName("sku")]
        public string Sku { get; set; } = string.Empty;

        [JsonPropertyName("slug")]
        public string Slug { get; set; } = string.Empty;

        [JsonPropertyName("descripcion")]
        public string Descripcion { get; set; } = string.Empty;

        [JsonPropertyName("precio")]
        public decimal Precio { get; set; }

        [JsonPropertyName("stock")]
        public int Stock { get; set; }

        [JsonPropertyName("tipoPiel")]
        public string TipoPiel { get; set; } = string.Empty;

        [JsonPropertyName("imagenUrl")]
        public string Imagen { get; set; } = string.Empty;

        [JsonPropertyName("estado")]
        public int Estado { get; set; }

        [JsonPropertyName("promedio_calificacion")]
        public double PromedioCalificacion { get; set; }

        [JsonPropertyName("cantidad_valoraciones")]
        public int CantidadValoraciones { get; set; }

        [JsonPropertyName("categoria")]
        public CategoriaProducto? Categoria { get; set; }

        [JsonPropertyName("marca")]
        public Marca? Marca { get; set; }
    }
}
