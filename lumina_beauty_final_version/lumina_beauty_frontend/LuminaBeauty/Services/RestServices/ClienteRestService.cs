using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class ClienteRestService
    {
        private readonly HttpClient _http;

        public ClienteRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Cliente?> RegistrarClienteAsync(Cliente cliente)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/ClienteRS/registrar",
                    cliente
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Cliente>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar cliente: {ex.Message}");
                return null;
            }
        }

        public async Task<int> SumarPuntosAsync(int idCliente, int puntos)
        {
            using var response = await _http.PutAsync(
                $"webresources/ClienteRS/sumarPuntos/{idCliente}?puntos={puntos}",
                null
            );

            return response.IsSuccessStatusCode
                ? await response.Content.ReadFromJsonAsync<int>()
                : 0;
        }

        public async Task<Cliente?> BuscarClienteAsync(int idCliente)
        {
            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/ClienteRS/buscar/{idCliente}"
                );

                return response.IsSuccessStatusCode
                    ? await response.Content.ReadFromJsonAsync<Cliente>()
                    : null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al buscar cliente: {ex.Message}");
                return null;
            }
        }

        public async Task<List<Cliente>> ListarClientesAsync()
        {
            try
            {
                using var response = await _http.GetAsync("webresources/ClienteRS/listar");

                return response.IsSuccessStatusCode
                    ? await response.Content.ReadFromJsonAsync<List<Cliente>>() ?? new List<Cliente>()
                    : new List<Cliente>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al listar clientes: {ex.Message}");
                return new List<Cliente>();
            }
        }

        public async Task<Cliente?> ActualizarClienteAsync(Cliente cliente)
        {
            try
            {
                using var response = await _http.PutAsJsonAsync(
                    "webresources/ClienteRS/actualizar",
                    cliente
                );

                return response.IsSuccessStatusCode
                    ? await response.Content.ReadFromJsonAsync<Cliente>()
                    : null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al actualizar cliente: {ex.Message}");
                return null;
            }
        }

        public async Task<bool> DesactivarCuentaAsync(int idCliente)
        {
            try
            {
                using var response = await _http.PutAsync(
                    $"webresources/ClienteRS/desactivar/{idCliente}",
                    null
                );

                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== DESACTIVAR CUENTA ==========");
                Console.WriteLine($"HTTP: {(int)response.StatusCode} - {response.StatusCode}");
                Console.WriteLine($"Respuesta: {contenido}");
                Console.WriteLine("========================================");

                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al desactivar cuenta: {ex.Message}");
                return false;
            }
        }
    }
}
