using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST;

public class EmpleadoRestService
{
    private readonly HttpClient _http;

    public EmpleadoRestService(HttpClient http)
    {
        _http = http;
    }

    public async Task<List<Empleado>> ListarEmpleadosAsync()
    {
        try
        {
            using var response = await _http.GetAsync("webresources/EmpleadoRS/listar");

            return response.IsSuccessStatusCode
                ? await response.Content.ReadFromJsonAsync<List<Empleado>>() ?? new List<Empleado>()
                : new List<Empleado>();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al listar empleados: {ex.Message}");
            return new List<Empleado>();
        }
    }

    public async Task<Empleado?> RegistrarEmpleadoAsync(Empleado empleado)
    {
        try
        {
            using var response = await _http.PostAsJsonAsync(
                "webresources/EmpleadoRS/registrar",
                empleado);

            return response.IsSuccessStatusCode
                ? await response.Content.ReadFromJsonAsync<Empleado>()
                : null;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al registrar empleado: {ex.Message}");
            return null;
        }
    }

    public async Task<Empleado?> ActualizarEmpleadoAsync(Empleado empleado)
    {
        try
        {
            using var response = await _http.PutAsJsonAsync(
                "webresources/EmpleadoRS/actualizar",
                empleado);

            return response.IsSuccessStatusCode
                ? await response.Content.ReadFromJsonAsync<Empleado>()
                : null;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al actualizar empleado: {ex.Message}");
            return null;
        }
    }

    public async Task<bool> EliminarEmpleadoAsync(int idEmpleado)
    {
        try
        {
            using var response = await _http.DeleteAsync(
                $"webresources/EmpleadoRS/eliminar/{idEmpleado}");

            if (!response.IsSuccessStatusCode) return false;

            var resultado = await response.Content.ReadFromJsonAsync<int>();
            return resultado == 1;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error al eliminar empleado: {ex.Message}");
            return false;
        }
    }
}
