namespace SoluLumina.Services
{
    using System.Net.Http.Json;
    using SoluLumina.Models;

    public class CategoriaService : ICategoriaService
    {
        private readonly HttpClient _httpClient;

        public CategoriaService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<List<CategoriaProducto>> ListarTodosAsync()
        {
            var categorias = await _httpClient.GetFromJsonAsync<List<CategoriaProducto>>("CategoriaRS/listar");
            return categorias ?? new List<CategoriaProducto>();
        }
    }
}
