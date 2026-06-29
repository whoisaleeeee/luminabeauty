using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Usuario
    {
        private int id_usuario;
        private String nombres;
        private String apellidos;
        private String correo;
        private String contrasena_hash;
        private String telefono;
        private String dni;
        private String tipo_usuario;
        private int estado;
        private DateTime fecha_creacion;
        private DateTime fecha_actualizacion;

        [JsonProperty("id_usuario")]
        public int Id_usuario { get => id_usuario; set => id_usuario = value; }
        [JsonProperty("nombres")]
        public string Nombres { get => nombres; set => nombres = value; }
        [JsonProperty("apellidos")]
        public string Apellidos { get => apellidos; set => apellidos = value; }
        [JsonProperty("correo")]
        public string Correo { get => correo; set => correo = value; }
        [JsonProperty("contrasena_hash")]
        public string Contrasena_hash { get => contrasena_hash; set => contrasena_hash = value; }
        [JsonProperty("telefono")]
        public string Telefono { get => telefono; set => telefono = value; }
        [JsonProperty("dni")]
        public string Dni { get => dni; set => dni = value; }
        [JsonProperty("tipo_usuario")]
        public string Tipo_usuario { get => tipo_usuario; set => tipo_usuario = value; }
        [JsonProperty("estado")]
        public int Estado { get => estado; set => estado = value; }
        [JsonProperty("fecha_creacion")]
        public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
        [JsonProperty("fecha_actualizacion")]
        public DateTime Fecha_actualizacion { get => fecha_actualizacion; set => fecha_actualizacion = value; }
    }
}
