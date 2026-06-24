using System.Net.Http.Json;

namespace LuminaBeauty.Servicios.REST
{
    public class CarroRestService
    {
        private readonly HttpClient _http;

        public CarroRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<int> AgregarProductoAsync(int idCarro, int idProducto, int cantidad)
        {
            var requestBody = new
            {
                carro = new
                {
                    id_carrito = idCarro
                },
                producto = new
                {
                    id_producto = idProducto
                },
                cantidad
            };

            using var response = await _http.PostAsJsonAsync("webresources/CarroRS/agregarProducto", requestBody);
            if (!response.IsSuccessStatusCode)
            {
                Console.WriteLine($"CarroRS/agregarProducto fallo con estado {(int)response.StatusCode}.");
                return 0;
            }

            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
