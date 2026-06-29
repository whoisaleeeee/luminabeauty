using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Cliente : Usuario
    {
        private int puntos_fidelidad;
        private String nivel_cliente;
        private Direccion direccion_principal;

        [JsonProperty("puntos_fidelidad")]
        public int Puntos_fidelidad { get => puntos_fidelidad; set => puntos_fidelidad = value; }
        [JsonProperty("nivel_cliente")]
        public string Nivel_cliente { get => nivel_cliente; set => nivel_cliente = value; }
        [JsonProperty("direccion")]
        public Direccion Direccion_principal { get => direccion_principal; set => direccion_principal = value; }
    }
}
