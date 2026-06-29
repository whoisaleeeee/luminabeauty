using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Empleado : Usuario
    {
        private string rol;

        [JsonProperty("rol")]
        public string Rol { get => rol; set => rol = value; }
    }
}
