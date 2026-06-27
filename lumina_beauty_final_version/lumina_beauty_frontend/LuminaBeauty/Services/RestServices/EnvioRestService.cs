using System.Net.Http.Json;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{
    public class EnvioRestService
    {
        private readonly HttpClient _http;

        public EnvioRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Envio?> BuscarPorPedidoAsync(int idPedido)
        {
            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/EnvioRS/buscarPorPedido/{idPedido}"
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Envio>();
            }
            catch
            {
                return null;
            }
        }
    }
}