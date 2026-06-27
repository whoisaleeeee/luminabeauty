using System.Net;
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

        public async Task<Envio?> RegistrarAsync(Envio envio)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/EnvioRS/registrar",
                    envio);

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine(
                        $"EnvioRS/registrar falló: {(int)response.StatusCode} - {response.StatusCode}");
                    return null;
                }

                if (response.StatusCode == HttpStatusCode.NoContent)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Envio>();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al registrar envío:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<Envio?> BuscarPorPedidoAsync(int idPedido)
        {
            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/EnvioRS/buscarPorPedido/{idPedido}");

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