using Newtonsoft.Json;

namespace SoluLumina.Models;

public class Marca
{
    private int id_marca;
    private String nombre;
    private String descripcion;
    private String logo_url;
    private int estado;
    private DateTime fecha_creacion;
    private DateTime fecha_actualizacion;

    [JsonProperty("id_marca")]
    public int Id_marca { get => id_marca; set => id_marca = value; }
    [JsonProperty("nombre")]
    public string Nombre { get => nombre; set => nombre = value; }
    [JsonProperty("descripcion")]
    public string Descripcion { get => descripcion; set => descripcion = value; }
    [JsonProperty("logo_url")]
    public string Logo_url { get => logo_url; set => logo_url = value; }
    [JsonProperty("estado")]
    public int Estado { get => estado; set => estado = value; }
    [JsonProperty("fecha_creacion")]
    public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
    [JsonProperty("fecha_actualizacion")]
    public DateTime Fecha_actualizacion { get => fecha_actualizacion; set => fecha_actualizacion = value; }
}
