using System.Text.Json.Serialization;

namespace LuminaBeauty.Servicios.Modelo
{
    public class Cliente : Usuario
    {
        [JsonPropertyName("puntos_fidelidad")]
        public int PuntosFidelidad { get; set; }

        [JsonPropertyName("nivel_cliente")]
        public string NivelCliente { get; set; } = "BRONCE";


    }
}
