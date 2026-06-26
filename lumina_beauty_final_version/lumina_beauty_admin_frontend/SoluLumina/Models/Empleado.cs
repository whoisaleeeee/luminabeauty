using Microsoft.AspNetCore.Components;
using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Empleado
    {
        private String rol;

        [JsonProperty("rol")]
        public string Rol { get => rol; set => rol = value; }
    }
}
