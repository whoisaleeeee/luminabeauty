using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{

    public class CarroRestService
    {
        private readonly HttpClient http;

        public CarroRestService(HttpClient http)
        {
            this.http = http;
        }

        // ── Operaciones Sincrónicas ──────────────────────────────────────────

        public int AgregarProducto(int idCarro, string idProducto, int cantidad)
        {
            var requestBody = new
            {
                carro = new { id = idCarro },
                producto = new { id = idProducto },
                cantidad = cantidad
            };
            var response = http.PostAsJsonAsync("webresources/CarroRS/agregarProducto", requestBody)
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
                return response.Content.ReadFromJsonAsync<int>().GetAwaiter().GetResult();
            return 0;
        }

        // ── Operaciones Asíncronas ───────────────────────────────────────────

        public async Task<int> AgregarProductoAsync(int idCarro, string idProducto, int cantidad)
        {
            var requestBody = new
            {
                carro = new { id = idCarro },
                producto = new { id = idProducto },
                cantidad = cantidad
            };
            var response = await http.PostAsJsonAsync("webresources/CarroRS/agregarProducto", requestBody);
            if (response.IsSuccessStatusCode)
                return await response.Content.ReadFromJsonAsync<int>();
            return 0;
        }
    }
}
