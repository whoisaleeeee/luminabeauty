using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{

    public class ClienteRestService
    {
        private readonly HttpClient http;

        public ClienteRestService(HttpClient http)
        {
            this.http = http;
        }

        // ── Operaciones Sincrónicas ──────────────────────────────────────────

        public Cliente? RegistrarCliente(Cliente cliente)
        {
            var response = http.PostAsJsonAsync("webresources/ClienteRS", cliente)
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
                return response.Content.ReadFromJsonAsync<Cliente>().GetAwaiter().GetResult();
            return null;
        }

        public int SumarPuntos(int idCliente, int puntos)
        {
            var response = http.PutAsync(
                $"webresources/ClienteRS/sumarPuntos/{idCliente}?puntos={puntos}", null)
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
                return response.Content.ReadFromJsonAsync<int>().GetAwaiter().GetResult();
            return 0;
        }

        // ── Operaciones Asíncronas ───────────────────────────────────────────

        public async Task<Cliente?> RegistrarClienteAsync(Cliente cliente)
        {
            var response = await http.PostAsJsonAsync("webresources/ClienteRS", cliente);
            if (response.IsSuccessStatusCode)
                return await response.Content.ReadFromJsonAsync<Cliente>();
            return null;
        }

        public async Task<int> SumarPuntosAsync(int idCliente, int puntos)
        {
            var response = await http.PutAsync(
                $"webresources/ClienteRS/sumarPuntos/{idCliente}?puntos={puntos}", null);
            if (response.IsSuccessStatusCode)
                return await response.Content.ReadFromJsonAsync<int>();
            return 0;
        }
    }
}
