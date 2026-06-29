using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class CuponRestService
    {
        private readonly HttpClient _http;

        public CuponRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Cupon?> AplicarAsync(string codigo)
        {
            try
            {
                var codigoSeguro = Uri.EscapeDataString(codigo.Trim().ToUpperInvariant());

                using var response = await _http.GetAsync(
                    $"webresources/CuponRS/aplicar/{codigoSeguro}");

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Cupon>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al aplicar cupón: {ex.Message}");
                return null;
            }
        }
    }
}