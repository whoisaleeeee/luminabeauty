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

        public async Task<List<Reclamo>> ListarPorClienteAsync(int idCliente)
        {
            try
            {
                return await _http.GetFromJsonAsync<List<Reclamo>>(
                    $"webresources/ReclamoRS/listarPorCliente/{idCliente}"
                ) ?? new List<Reclamo>();
            }
            catch (Exception ex)
            {
                Console.WriteLine(
                    $"Error al listar reclamos por cliente: {ex.Message}"
                );

                return new List<Reclamo>();
            }
        }

        public async Task<Reclamo?> RegistrarReclamoAsync(Reclamo reclamo)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/ReclamoRS/registrar",
                    reclamo
                );

                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== REGISTRAR RECLAMO ==========");
                Console.WriteLine(
                    $"URL: {_http.BaseAddress}webresources/ReclamoRS/registrar"
                );
                Console.WriteLine(
                    $"HTTP: {(int)response.StatusCode} - {response.StatusCode}"
                );
                Console.WriteLine($"Respuesta: {contenido}");
                Console.WriteLine("========================================");

                if (!response.IsSuccessStatusCode ||
                    string.IsNullOrWhiteSpace(contenido))
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Reclamo>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar reclamo: {ex.Message}");
                return null;
            }
        }

        public async Task<Reclamo?> CambiarEstadoAsync(
            int idReclamo,
            string estadoNuevo)
        {
            try
            {
                string estadoCodificado = Uri.EscapeDataString(estadoNuevo);

                using var response = await _http.PutAsync(
                    $"webresources/ReclamoRS/cambiarEstado/{idReclamo}/{estadoCodificado}",
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

        public async Task<Reclamo?> ActualizarReclamoAsync(Reclamo reclamo)
        {
            try
            {
                using var response = await _http.PutAsJsonAsync(
                    "webresources/ReclamoRS/actualizar",
                    reclamo
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Reclamo>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al actualizar reclamo: {ex.Message}");
                return null;
            }
        }
    }
}