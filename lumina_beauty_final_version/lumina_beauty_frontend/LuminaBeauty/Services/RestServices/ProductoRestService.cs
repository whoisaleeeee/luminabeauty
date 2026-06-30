using System.Net.Http.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class ProductoRestService
    {
        private readonly HttpClient _http;

        public ProductoRestService(HttpClient http)
        {
            _http = http;
        }

        public Uri? BaseAddress => _http.BaseAddress;

        public Task<List<Producto>> ListarProductosTodosAsync()
        {
            return GetListAsync<Producto>("webresources/ProductoRS/listar");
        }

        public async Task<Producto?> BuscarProductoPorIdAsync(int idProducto)
        {
            using var response = await _http.GetAsync($"webresources/ProductoRS/buscar/{idProducto}");
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"ProductoRS/buscar fallo con estado {(int)response.StatusCode}.");
                return null;
            }

            return await response.Content.ReadFromJsonAsync<Producto>();
        }

        public async Task<Producto?> RegistrarProductoAsync(Producto producto)
        {
            using var response = await _http.PostAsJsonAsync("webresources/ProductoRS/registrar", producto);
            if (!response.IsSuccessStatusCode)
            {
                var errorBody = await response.Content.ReadAsStringAsync();
                Console.WriteLine($"ProductoRS/registrar fallo con estado {(int)response.StatusCode}: {errorBody}");
                // Extract error message from JSON body if possible
                try
                {
                    var errorObj = System.Text.Json.JsonDocument.Parse(errorBody);
                    if (errorObj.RootElement.TryGetProperty("error", out var msg))
                        throw new HttpRequestException(msg.GetString());
                }
                catch (System.Text.Json.JsonException) { }
                throw new HttpRequestException($"Error del servidor ({(int)response.StatusCode}): {errorBody}");
            }

            var content = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrWhiteSpace(content))
                return null;

            return System.Text.Json.JsonSerializer.Deserialize<Producto>(content,
                new System.Text.Json.JsonSerializerOptions { PropertyNameCaseInsensitive = true });
        }

        public async Task<Producto?> ActualizarProductoAsync(Producto producto)
        {
            using var response = await _http.PutAsJsonAsync("webresources/ProductoRS/actualizar", producto);
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"ProductoRS/actualizar fallo con estado {(int)response.StatusCode}.");
                return null;
            }

            return await response.Content.ReadFromJsonAsync<Producto>();
        }

        public async Task<int> EliminarProductoAsync(int idProducto)
        {
            using var response = await _http.DeleteAsync($"webresources/ProductoRS/eliminar/{idProducto}");
            return await ReadIntResultAsync(response, "ProductoRS/eliminar");
        }

        public Task<List<Producto>> FiltrarPorTipoPielAsync(string tipoPiel)
        {
            return GetListAsync<Producto>($"webresources/ProductoRS/filtrarTipoPiel/{Uri.EscapeDataString(tipoPiel)}");
        }

        public Task<List<CategoriaProducto>> ListarCategoriasAsync()
        {
            return GetListAsync<CategoriaProducto>("webresources/CategoriaProductoRS/listar");
        }

        public Task<List<Marca>> ListarMarcasAsync()
        {
            return GetListAsync<Marca>("webresources/MarcaRS/listar");
        }

        public async Task<int> ValidarStockAsync(int idProducto, int cantidad)
        {
            using var response = await _http.GetAsync($"webresources/ProductoRS/validarStock/{idProducto}/{cantidad}");
            return await ReadIntResultAsync(response, "ProductoRS/validarStock");
        }

        public async Task<int> DescontarStockAsync(int idProducto, int cantidad)
        {
            using var response = await _http.PutAsync($"webresources/ProductoRS/descontarStock/{idProducto}/{cantidad}", null);
            return await ReadIntResultAsync(response, "ProductoRS/descontarStock");
        }

        private async Task<List<T>> GetListAsync<T>(string endpoint)
        {
            using var response = await _http.GetAsync(endpoint);
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"{endpoint} fallo con estado {(int)response.StatusCode}.");
                return new List<T>();
            }

            return await response.Content.ReadFromJsonAsync<List<T>>() ?? new List<T>();
        }

        private static async Task<int> ReadIntResultAsync(HttpResponseMessage response, string operation)
        {
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"{operation} fallo con estado {(int)response.StatusCode}.");
                return 0;
            }

            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
