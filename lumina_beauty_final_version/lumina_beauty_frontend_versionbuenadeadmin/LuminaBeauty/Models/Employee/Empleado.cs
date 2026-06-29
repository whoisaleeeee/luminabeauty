using System.Text.Json.Serialization;

namespace LuminaBeauty.Models;

public class Empleado : Usuario
{
    [JsonPropertyName("rol")]
    public string Rol { get; set; } = "ADMIN";
}