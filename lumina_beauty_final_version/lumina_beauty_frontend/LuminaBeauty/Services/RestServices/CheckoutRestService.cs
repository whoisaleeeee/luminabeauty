using System.Net.Http.Json;
using System.Text.Json;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class CheckoutRestService
    {
        private readonly HttpClient _http;

        private static readonly JsonSerializerOptions JsonOptions = new()
        {
            PropertyNameCaseInsensitive = true
        };

        public CheckoutRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<CheckoutApiResponse?> ProcesarAsync(
            CheckoutApiRequest solicitud)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/CheckoutRS/procesar",
                    solicitud);

                string contenido = await response.Content.ReadAsStringAsync();

                Console.WriteLine("========== CHECKOUT ATÓMICO ==========");
                Console.WriteLine($"HTTP: {(int)response.StatusCode} - {response.StatusCode}");
                Console.WriteLine($"Respuesta: {contenido}");
                Console.WriteLine("======================================");

                if (string.IsNullOrWhiteSpace(contenido))
                {
                    return CheckoutApiResponse.Error(
                        $"El servidor respondió {(int)response.StatusCode} sin detalle."
                    );
                }

                if (contenido.TrimStart().StartsWith("<"))
                {
                    return CheckoutApiResponse.Error(
                        $"El backend devolvió un error interno ({(int)response.StatusCode}). "
                        + "Revisa la consola de GlassFish."
                    );
                }

                var resultado = JsonSerializer.Deserialize<CheckoutApiResponse>(
                    contenido,
                    JsonOptions
                );

                if (!response.IsSuccessStatusCode)
                {
                    return resultado ?? CheckoutApiResponse.Error(
                        $"No se pudo completar el checkout. HTTP {(int)response.StatusCode}."
                    );
                }

                return resultado ?? CheckoutApiResponse.Error(
                    "El backend no devolvió una respuesta válida."
                );
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al procesar checkout atómico:");
                Console.WriteLine(ex);

                return CheckoutApiResponse.Error(
                    "No se pudo procesar el pago. Revisa la consola del backend."
                );
            }
        }
    }

    public class CheckoutApiRequest
    {
        public Pedido Pedido { get; set; } = new();
        public Envio Envio { get; set; } = new();
        public Pago Pago { get; set; } = new();
    }

    public class CheckoutApiResponse
    {
        public bool Exitoso { get; set; }
        public string Mensaje { get; set; } = string.Empty;
        public int IdPedido { get; set; }
        public string CodigoPedido { get; set; } = string.Empty;
        public int IdEnvio { get; set; }
        public int IdPago { get; set; }

        public static CheckoutApiResponse Error(string mensaje)
        {
            return new CheckoutApiResponse
            {
                Exitoso = false,
                Mensaje = mensaje
            };
        }
    }
}