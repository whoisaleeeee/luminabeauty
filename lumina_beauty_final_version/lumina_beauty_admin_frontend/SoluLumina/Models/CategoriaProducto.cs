using Newtonsoft.Json;

namespace SoluLumina.Models;

public class CategoriaProducto
{
    private int id_categoria;
    private String nombre;
    private String descripcion;
    private int estado;
    private DateTime fecha_creacion;
    private DateTime fecha_actualizacion;

    [JsonProperty("id_categoria")]
    public int Id_categoria { get => id_categoria; set => id_categoria = value; }
    [JsonProperty("nombre")]
    public string Nombre { get => nombre; set => nombre = value; }
    [JsonProperty("descripcion")]
    public string Descripcion { get => descripcion; set => descripcion = value; }
    [JsonProperty("estado")]
    public int Estado { get => estado; set => estado = value; }
    [JsonProperty("fecha_creacion")]
    public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
    [JsonProperty("fecha_actualizacion")]
    public DateTime Fecha_actualizacion { get => fecha_actualizacion; set => fecha_actualizacion = value; }
}
