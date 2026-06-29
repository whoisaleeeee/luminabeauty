using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class ValoracionRestService
    {
        private readonly HttpClient _http;

        public ValoracionRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<List<Valoracion>> ListarPublicadasPorProductoAsync(
            int idProducto)
        {
            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/ValoracionRS/listarPublicadasPorProducto/{idProducto}"
                );

                if (!response.IsSuccessStatusCode)
                {
                    return [];
                }

                return await response.Content
                    .ReadFromJsonAsync<List<Valoracion>>() ?? [];
            }
            catch
            {
                return [];
            }
        }
    }
}