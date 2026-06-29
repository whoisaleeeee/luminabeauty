using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class ReclamoRestService
    {
        private readonly HttpClient _http;

        public ReclamoRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<List<Reclamo>> ListarReclamosAsync()
        {
            try
            {
                return await _http.GetFromJsonAsync<List<Reclamo>>(
                    "webresources/ReclamoRS/listar"
                ) ?? new List<Reclamo>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al listar reclamos: {ex.Message}");
                return new List<Reclamo>();
            }
        }

        public async Task<Reclamo?> CambiarEstadoAsync(
            int idReclamo,
            string estadoNuevo)
        {
            try
            {
                using var response = await _http.PutAsync(
                    $"webresources/ReclamoRS/cambiarEstado/{idReclamo}/{estadoNuevo}",
                    null
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Reclamo>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al cambiar estado: {ex.Message}");
                return null;
            }
        }

        public async Task<Reclamo?> AsignarAreaAsync(
            int idReclamo,
            string area)
        {
            try
            {
                string areaCodificada = Uri.EscapeDataString(area);

                using var response = await _http.PutAsync(
                    $"webresources/ReclamoRS/asignarArea/{idReclamo}?area={areaCodificada}",
                    null
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Reclamo>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al asignar área: {ex.Message}");
                return null;
            }
        }
    }
}