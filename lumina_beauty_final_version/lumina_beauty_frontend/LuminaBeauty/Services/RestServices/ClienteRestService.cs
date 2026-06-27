using System.Net.Http.Json;
using LuminaBeauty.Servicios.Modelo;

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
                using var response = await _http.PostAsJsonAsync("webresources/ClienteRS/registrar", cliente);

                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== REGISTRO CLIENTE ==========");
                Console.WriteLine($"URL llamada: {_http.BaseAddress}webresources/ClienteRS/registrar");
                Console.WriteLine($"Estado HTTP: {(int)response.StatusCode} - {response.StatusCode}");
                Console.WriteLine($"Respuesta backend: '{contenido}'");
                Console.WriteLine("======================================");

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                if (string.IsNullOrWhiteSpace(contenido))
                {
                    Console.WriteLine("El backend respondió OK, pero sin JSON.");
                    return null;
                }

                return System.Text.Json.JsonSerializer.Deserialize<Cliente>(
                    contenido,
                    new System.Text.Json.JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al llamar ClienteRS/registrar:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<int> SumarPuntosAsync(int idCliente, int puntos)
        {
            using var response = await _http.PutAsync($"webresources/ClienteRS/sumarPuntos/{idCliente}?puntos={puntos}", null);
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"ClienteRS/sumarPuntos fallo con estado {(int)response.StatusCode}.");
                return 0;
            }

            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<Cliente?> BuscarClienteAsync(int idCliente)
        {
            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/ClienteRS/buscar/{idCliente}"
                );

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine(
                        $"ClienteRS/buscar/{idCliente} falló con estado {(int)response.StatusCode}."
                    );
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Cliente>();
            }
            catch (Exception ex)
            {
                Console.WriteLine(
                    $"Error al llamar ClienteRS/buscar/{idCliente}: {ex.Message}"
                );
                return null;
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

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine(
                        $"ClienteRS/actualizar falló con estado {(int)response.StatusCode}."
                    );
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Cliente>();
            }
            catch (Exception ex)
            {
                Console.WriteLine(
                    $"Error al llamar ClienteRS/actualizar: {ex.Message}"
                );
                return null;
            }
        }
    }
}