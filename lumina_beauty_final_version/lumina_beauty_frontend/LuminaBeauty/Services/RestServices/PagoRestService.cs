using System.Net;
using System.Net.Http.Json;
using System.Text.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class PagoRestService
    {
        private readonly HttpClient _http;

        private static readonly JsonSerializerOptions JsonOptions = new()
        {
            PropertyNameCaseInsensitive = true
        };

        public PagoRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Pago?> RegistrarPagoAsync(Pago pago)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/PagoRS/registrar",
                    pago);

                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== REGISTRAR PAGO ==========");
                Console.WriteLine($"URL: {_http.BaseAddress}webresources/PagoRS/registrar");
                Console.WriteLine($"HTTP: {(int)response.StatusCode} - {response.StatusCode}");
                Console.WriteLine($"Respuesta: {contenido}");
                Console.WriteLine("====================================");

                if (!response.IsSuccessStatusCode ||
                    string.IsNullOrWhiteSpace(contenido) ||
                    contenido.Trim().Equals("null", StringComparison.OrdinalIgnoreCase))
                {
                    return null;
                }

                return JsonSerializer.Deserialize<Pago>(contenido, JsonOptions);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al registrar pago:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<Pago?> CompletarPagoAsync(
            int idPago,
            string referenciaTransaccion)
        {
            if (idPago <= 0 || string.IsNullOrWhiteSpace(referenciaTransaccion))
            {
                return null;
            }

            try
            {
                string referencia = Uri.EscapeDataString(referenciaTransaccion);

                using var response = await _http.PutAsync(
                    $"webresources/PagoRS/completar/{idPago}?referencia={referencia}",
                    null);

                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== COMPLETAR PAGO ==========");
                Console.WriteLine($"HTTP: {(int)response.StatusCode} - {response.StatusCode}");
                Console.WriteLine($"Respuesta: {contenido}");
                Console.WriteLine("====================================");

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                // El backend responde 204 cuando completa correctamente,
                // pero no devuelve el objeto Pago.
                if (response.StatusCode == HttpStatusCode.NoContent ||
                    string.IsNullOrWhiteSpace(contenido) ||
                    contenido.Trim().Equals("null", StringComparison.OrdinalIgnoreCase))
                {
                    return new Pago
                    {
                        IdPago = idPago,
                        Estado = "COMPLETADO",
                        ReferenciaTransaccion = referenciaTransaccion,
                        FechaPago = DateTime.Now
                    };
                }

                return JsonSerializer.Deserialize<Pago>(contenido, JsonOptions);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al completar pago:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<Pago?> MarcarPagoFallidoAsync(int idPago)
        {
            if (idPago <= 0)
            {
                return null;
            }

            try
            {
                using var response = await _http.PutAsync(
                    $"webresources/PagoRS/fallido/{idPago}",
                    null);

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                if (response.StatusCode == HttpStatusCode.NoContent)
                {
                    return new Pago
                    {
                        IdPago = idPago,
                        Estado = "FALLIDO"
                    };
                }

                return await response.Content.ReadFromJsonAsync<Pago>(JsonOptions);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al marcar pago fallido:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public async Task<Pago?> BuscarPagoPorPedidoAsync(int idPedido)
        {
            if (idPedido <= 0)
            {
                return null;
            }

            try
            {
                using var response = await _http.GetAsync(
                    $"webresources/PagoRS/buscarPorPedido/{idPedido}");

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Pago>(JsonOptions);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al buscar pago por pedido:");
                Console.WriteLine(ex.Message);
                return null;
            }
        }
    }
}