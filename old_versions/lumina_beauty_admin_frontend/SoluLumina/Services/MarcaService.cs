namespace SoluLumina.Services
{
    using System.Net.Http.Json;
    using SoluLumina.Models;

    public class MarcaService : IMarcaService
    {
        private readonly HttpClient _httpClient;

        public MarcaService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public async Task<List<Marca>> ListarTodosAsync()
        {
            var marcas = await _httpClient.GetFromJsonAsync<List<Marca>>("MarcaRS/listar");
            return marcas ?? new List<Marca>();
        }
    }
}
