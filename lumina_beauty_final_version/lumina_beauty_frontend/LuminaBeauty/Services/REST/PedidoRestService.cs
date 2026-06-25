using System.Net;
using System.Net.Http.Json;
using System.Text.Json;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{
    public class PedidoRestService
    {
        private readonly HttpClient _http;

        public PedidoRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Pedido?> CrearPedidoAsync(Pedido pedido)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync("webresources/PedidoRS/crear", pedido);
                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== CREAR PEDIDO ==========");
                Console.WriteLine($"URL: {_http.BaseAddress}webresources/PedidoRS/crear");
                Console.WriteLine($"HTTP: {(int)response.StatusCode} - {response.StatusCode}");
                Console.WriteLine($"Respuesta: {contenido}");
                Console.WriteLine("==================================");

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine($"PedidoRS/crear falló con estado {(int)response.StatusCode}.");
                    return null;
                }

                if (response.StatusCode == HttpStatusCode.NoContent ||
                    string.IsNullOrWhiteSpace(contenido) ||
                    contenido.Trim().Equals("null", StringComparison.OrdinalIgnoreCase))
                {
                    Console.WriteLine("PedidoRS/crear respondió 204. Intentando recuperar pedido creado...");

                    var pedidoRecuperado = await BuscarPedidoPorCodigoAsync(
                        pedido.Cliente?.Id ?? 0,
                        pedido.CodigoPedido
                    );

                    if (pedidoRecuperado != null)
                    {
                        return pedidoRecuperado;
                    }

                    Console.WriteLine("No se pudo recuperar el pedido real luego del 204. Se continuará con el código generado.");

                    pedido.IdPedido = new Random().Next(1001, 9999);
                    return pedido;
                }

                var pedidoCreado = JsonSerializer.Deserialize<Pedido>(
                    contenido,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });

                if (pedidoCreado == null)
                {
                    Console.WriteLine("No se pudo deserializar el pedido creado.");
                    return null;
                }

                if (string.IsNullOrWhiteSpace(pedidoCreado.CodigoPedido))
                {
                    pedidoCreado.CodigoPedido = pedido.CodigoPedido;
                }

                return pedidoCreado;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al crear pedido:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        private async Task<Pedido?> BuscarPedidoPorCodigoAsync(int idCliente, string codigoPedido)
        {
            if (idCliente <= 0 || string.IsNullOrWhiteSpace(codigoPedido))
            {
                return null;
            }

            try
            {
                var pedidos = await ListarPedidosPorClienteAsync(idCliente);

                return pedidos
                    .FirstOrDefault(p => p.CodigoPedido.Equals(
                        codigoPedido,
                        StringComparison.OrdinalIgnoreCase));
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al recuperar pedido creado:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<Pedido?> BuscarPedidoAsync(int idPedido)
        {
            try
            {
                using var response = await _http.GetAsync($"webresources/PedidoRS/buscar/{idPedido}");

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Pedido>();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al buscar pedido:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<List<Pedido>> ListarPedidosAsync()
        {
            try
            {
                using var response = await _http.GetAsync("webresources/PedidoRS/listar");

                if (!response.IsSuccessStatusCode)
                {
                    return new List<Pedido>();
                }

                return await response.Content.ReadFromJsonAsync<List<Pedido>>() ?? new List<Pedido>();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al listar pedidos:");
                Console.WriteLine(ex.Message);
                return new List<Pedido>();
            }
        }

        public async Task<List<Pedido>> ListarPedidosPorClienteAsync(int idCliente)
        {
            try
            {
                using var response = await _http.GetAsync($"webresources/PedidoRS/listarPorCliente/{idCliente}");

                if (!response.IsSuccessStatusCode)
                {
                    Console.WriteLine($"PedidoRS/listarPorCliente falló con estado {(int)response.StatusCode}.");
                    return new List<Pedido>();
                }

                return await response.Content.ReadFromJsonAsync<List<Pedido>>() ?? new List<Pedido>();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al listar pedidos por cliente:");
                Console.WriteLine(ex.Message);
                return new List<Pedido>();
            }
        }

        public async Task<int> CancelarPedidoAsync(int idPedido)
        {
            try
            {
                using var response = await _http.DeleteAsync($"webresources/PedidoRS/cancelar/{idPedido}");

                if (!response.IsSuccessStatusCode)
                {
                    return 0;
                }

                return await response.Content.ReadFromJsonAsync<int>();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al cancelar pedido:");
                Console.WriteLine(ex.Message);
                return 0;
            }
        }

        public async Task<Pedido?> ActualizarEstadoPedidoAsync(int idPedido, string estadoNuevo)
        {
            try
            {
                using var response = await _http.PutAsync(
                    $"webresources/PedidoRS/actualizarEstado/{idPedido}/{estadoNuevo}",
                    null);

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Pedido>();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al actualizar estado del pedido:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }
    }
}