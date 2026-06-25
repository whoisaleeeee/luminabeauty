using System.Net.Http.Json;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{
    public class DireccionRestService
    {
        private readonly HttpClient _http;

        public DireccionRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<List<Direccion>> ListarPorClienteAsync(int idCliente)
        {
            try
            {
                using var response = await _http.GetAsync($"webresources/DireccionRS/listarPorCliente/{idCliente}");

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine($"DireccionRS/listarPorCliente falló con estado {(int)response.StatusCode}.");
                    return new List<Direccion>();
                }

                return await response.Content.ReadFromJsonAsync<List<Direccion>>() ?? new List<Direccion>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al listar direcciones del cliente: {ex.Message}");
                return new List<Direccion>();
            }
        }
    }
}