using System.Net.Http.Json;
using LuminaBeauty.Models;

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
                using var response = await _http.GetAsync(
                    $"webresources/DireccionRS/listarPorCliente/{idCliente}"
                );

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine(
                        $"DireccionRS/listarPorCliente falló con estado {(int)response.StatusCode}."
                    );

                    return [];
                }

                return await response.Content.ReadFromJsonAsync<List<Direccion>>()
                    ?? [];
            }
            catch (Exception ex)
            {
                Console.WriteLine(
                    $"Error al listar direcciones del cliente: {ex.Message}"
                );

                return [];
            }
        }

        public async Task<Direccion?> RegistrarAsync(Direccion direccion)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/DireccionRS/registrar",
                    direccion
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Direccion>();
            }
            catch
            {
                return null;
            }
        }

        public async Task<Direccion?> ActualizarAsync(Direccion direccion)
        {
            try
            {
                using var response = await _http.PutAsJsonAsync(
                    "webresources/DireccionRS/actualizar",
                    direccion
                );

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Direccion>();
            }
            catch
            {
                return null;
            }
        }

        public async Task<bool> EliminarAsync(int idDireccion)
        {
            try
            {
                using var response = await _http.DeleteAsync(
                    $"webresources/DireccionRS/eliminar/{idDireccion}"
                );

                if (!response.IsSuccessStatusCode)
                {
                    return false;
                }

                var result = await response.Content.ReadFromJsonAsync<int>();
                return result == 1;
            }
            catch
            {
                return false;
            }
        }
    }
}