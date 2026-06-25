using System.Net.Http.Json;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{
    public class MovimientoPuntosFidelidadRestService
    {
        private readonly HttpClient _http;

        public MovimientoPuntosFidelidadRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<List<MovimientoPuntosFidelidad>>
            ListarPorClienteAsync(int idCliente)
        {
            try
            {
                var movimientos = await _http.GetFromJsonAsync<
                    List<MovimientoPuntosFidelidad>
                >($"webresources/MovimientoPuntosFidelidadRS/listarPorCliente/{idCliente}");

                return movimientos ?? [];
            }
            catch
            {
                return [];
            }
        }
    }
}