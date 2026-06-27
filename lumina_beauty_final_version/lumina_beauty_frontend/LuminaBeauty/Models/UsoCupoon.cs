using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class UsoCupon
    {
        [JsonPropertyName("id_uso_cupon")]
        public int IdUsoCupon { get; set; }

        [JsonPropertyName("cupon")]
        public Cupon? Cupon { get; set; }

        [JsonPropertyName("cliente")]
        public Cliente? Cliente { get; set; }

        [JsonPropertyName("pedido")]
        public Pedido? Pedido { get; set; }
    }
}