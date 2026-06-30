using System.Net.Http.Json;
using System.Text.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class ValoracionRestService
    {
        private readonly HttpClient _http;

        public ValoracionRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<List<Valoracion>> ListarPublicadasPorProductoAsync(
            int idProducto)
        {
            using var response = await _http.GetAsync(
                $"webresources/ValoracionRS/listarPublicadasPorProducto/{idProducto}"
            );

            if (!response.IsSuccessStatusCode)
            {
                return [];
            }

            return await response.Content
                .ReadFromJsonAsync<List<Valoracion>>() ?? [];
        }

        public async Task<List<Valoracion>> ListarPorClienteAsync(
            int idCliente)
        {
            using var response = await _http.GetAsync(
                $"webresources/ValoracionRS/listarPorCliente/{idCliente}"
            );

            if (!response.IsSuccessStatusCode)
            {
                return [];
            }

            return await response.Content
                .ReadFromJsonAsync<List<Valoracion>>() ?? [];
        }

        public async Task<Valoracion> RegistrarAsync(
            int idCliente,
            int idProducto,
            int idDetallePedido,
            int calificacion,
            string comentario)
        {
            var payload = new
            {
                cliente = new
                {
                    id_usuario = idCliente
                },
                producto = new
                {
                    id_producto = idProducto
                },
                detallePedido = new
                {
                    id_detalle_pedido = idDetallePedido
                },
                calificacion,
                comentario,
                estado = "PENDIENTE"
            };

            using var response = await _http.PostAsJsonAsync(
                "webresources/ValoracionRS/registrar",
                payload
            );

            string contenido = await response.Content.ReadAsStringAsync();

            Console.WriteLine("========== REGISTRAR VALORACION ==========");
            Console.WriteLine(
                $"URL: {_http.BaseAddress}webresources/ValoracionRS/registrar"
            );
            Console.WriteLine(
                $"HTTP: {(int)response.StatusCode} - {response.StatusCode}"
            );
            Console.WriteLine($"Respuesta: {contenido}");
            Console.WriteLine("==========================================");

            if (!response.IsSuccessStatusCode)
            {
                string mensaje = "No se pudo registrar la valoración.";

                try
                {
                    using var documento = JsonDocument.Parse(contenido);

                    if (documento.RootElement.TryGetProperty(
                        "error",
                        out var error))
                    {
                        mensaje = error.GetString() ?? mensaje;
                    }
                }
                catch
                {
                    if (!string.IsNullOrWhiteSpace(contenido))
                    {
                        mensaje = contenido;
                    }
                }

                throw new InvalidOperationException(mensaje);
            }

            var resultado = JsonSerializer.Deserialize<Valoracion>(
                contenido,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                });

            if (resultado is null || resultado.IdValoracion <= 0)
            {
                throw new InvalidOperationException(
                    "La valoración fue recibida, pero el servidor no devolvió un identificador válido."
                );
            }

            return resultado;
        }
    }
}