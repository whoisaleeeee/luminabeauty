using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class CarroDeCompras
    {
        [JsonPropertyName("id_carrito")]
        public int Id { get; set; }

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }

        [JsonPropertyName("detalles")]
        public List<DetalleCarro> Detalles { get; set; } = new();
    }

    public class DetalleCarro
    {
        [JsonPropertyName("id_detalle_carro")]
        public int Id { get; set; }

        [JsonPropertyName("producto")]
        public Producto? Producto { get; set; }

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precio_unitario")]
        public decimal PrecioUnitario { get; set; }
    }
}
