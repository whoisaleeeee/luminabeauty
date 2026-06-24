using System.Net.Http.Json;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{
    public class AuthRestService
    {
        private readonly HttpClient _http;

        public AuthRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Cliente?> LoginClienteAsync(string correo, string contrasena)
        {
            var requestBody = new
            {
                correo,
                contrasena
            };

            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/AuthRS/loginCliente",
                    requestBody
                );

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine(
                        $"AuthRS/loginCliente falló con estado {(int)response.StatusCode}."
                    );
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Cliente>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error en AuthRS/loginCliente: {ex.Message}");
                return null;
            }
        }

        public async Task<bool> ValidarCredencialesAsync(string correo, string contrasena)
        {
            var cliente = await LoginClienteAsync(correo, contrasena);
            return cliente != null && cliente.Id > 0;
        }
    }
}