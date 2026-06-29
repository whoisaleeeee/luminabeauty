using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class UsoCuponRestService
    {
        private readonly HttpClient _http;

        public UsoCuponRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<UsoCupon?> RegistrarAsync(UsoCupon usoCupon)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/UsoCuponRS/registrar",
                    usoCupon);

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<UsoCupon>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar uso de cupón: {ex.Message}");
                return null;
            }
        }

        public async Task<bool> ClienteYaUsoAsync(int idCliente, int idCupon)
        {
            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/UsoCuponRS/clienteYaUsoCupon/{idCliente}/{idCupon}");

                if (!response.IsSuccessStatusCode)
                {
                    return false;
                }

                var resultado = await response.Content.ReadFromJsonAsync<int>();
                return resultado == 1;
            }
            catch
            {
                return false;
            }
        }
    }
}