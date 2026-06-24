namespace SoluLumina.Services
{
    using System.Net.Http.Json;
    using SoluLumina.Models;

    public class ProductoService : IProductoService
    {
        private readonly HttpClient _httpClient;

        public ProductoService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<List<Producto>> ListarTodosAsync()
        {
            var productos = await _httpClient.GetFromJsonAsync<List<Producto>>("ProductoRS/listar");
            return productos ?? new List<Producto>();
        }

        public async Task<Producto?> BuscarPorIdAsync(int id)
        {
            return await _httpClient.GetFromJsonAsync<Producto>($"ProductoRS/{id}");
        }

        public async Task<Producto?> InsertarAsync(Producto producto)
        {
            var response = await _httpClient.PostAsJsonAsync("ProductoRS/insertar", producto);
            if (response.IsSuccessStatusCode)
            {
                return await response.Content.ReadFromJsonAsync<Producto>();
            }
            return null;
        }

        public async Task<Producto?> ActualizarAsync(Producto producto)
        {
            var response = await _httpClient.PutAsJsonAsync("ProductoRS/actualizar", producto);
            if (response.IsSuccessStatusCode)
            {
                return await response.Content.ReadFromJsonAsync<Producto>();
            }
            return null;
        }

        public async Task<bool> EliminarAsync(int id)
        {
            var response = await _httpClient.DeleteAsync($"ProductoRS/eliminar/{id}");
            return response.IsSuccessStatusCode;
        }
    }
}
