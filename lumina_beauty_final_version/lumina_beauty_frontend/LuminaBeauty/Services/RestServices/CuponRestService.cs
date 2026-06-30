using System.Globalization;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using LuminaBeauty.Models;

namespace LuminaBeauty.Servicios.REST
{
    public class CuponRestService
    {
        private readonly HttpClient _http;

        private static readonly JsonSerializerOptions JsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            Converters =
            {
                new JavaLocalDateTimeConverter(),
                new JavaNullableLocalDateTimeConverter()
            }
        };

        public CuponRestService(HttpClient http)
        {
            _http = http;
        }

        public async Task<Cupon?> AplicarAsync(string codigo)
        {
            try
            {
                var codigoSeguro = Uri.EscapeDataString(codigo.Trim().ToUpperInvariant());

                using var response = await _http.GetAsync(
                    $"webresources/CuponRS/aplicar/{codigoSeguro}");

                if (!response.IsSuccessStatusCode)
                {
                    return null;
                }

                return await response.Content.ReadFromJsonAsync<Cupon>(JsonOptions);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al aplicar cupón: {ex.Message}");
                return null;
            }
        }

        public async Task<List<Cupon>> ListarCuponesAsync()
        {
            try
            {
                using var response = await _http.GetAsync("webresources/CuponRS/listar");

                return response.IsSuccessStatusCode
                    ? await response.Content.ReadFromJsonAsync<List<Cupon>>(JsonOptions) ?? new List<Cupon>()
                    : new List<Cupon>();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al listar cupones: {ex.Message}");
                return new List<Cupon>();
            }
        }

        public async Task<Cupon?> RegistrarCuponAsync(Cupon cupon)
        {
            try
            {
                using var response = await _http.PostAsJsonAsync(
                    "webresources/CuponRS/registrar",
                    cupon,
                    JsonOptions);

                return response.IsSuccessStatusCode
                    ? await response.Content.ReadFromJsonAsync<Cupon>(JsonOptions)
                    : null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar cupón: {ex.Message}");
                return null;
            }
        }

        public async Task<Cupon?> ActualizarCuponAsync(Cupon cupon)
        {
            try
            {
                using var response = await _http.PutAsJsonAsync(
                    "webresources/CuponRS/actualizar",
                    cupon,
                    JsonOptions);

                return response.IsSuccessStatusCode
                    ? await response.Content.ReadFromJsonAsync<Cupon>(JsonOptions)
                    : null;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al actualizar cupón: {ex.Message}");
                return null;
            }
        }

        public async Task<bool> EliminarCuponAsync(int idCupon)
        {
            try
            {
                using var response = await _http.DeleteAsync(
                    $"webresources/CuponRS/eliminar/{idCupon}");

                if (!response.IsSuccessStatusCode) return false;

                var resultado = await response.Content.ReadFromJsonAsync<int>();
                return resultado == 1;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al eliminar cupón: {ex.Message}");
                return false;
            }
        }
    }

    internal sealed class JavaLocalDateTimeConverter : JsonConverter<DateTime>
    {
        private const string Format = "yyyy-MM-dd'T'HH:mm:ss";

        public override DateTime Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            var value = reader.GetString();
            if (string.IsNullOrWhiteSpace(value))
            {
                return default;
            }

            var normalized = value.Length >= 19 ? value[..19] : value;
            return DateTime.ParseExact(normalized, Format, CultureInfo.InvariantCulture);
        }

        public override void Write(Utf8JsonWriter writer, DateTime value, JsonSerializerOptions options)
        {
            writer.WriteStringValue(value.ToString(Format, CultureInfo.InvariantCulture));
        }
    }

    internal sealed class JavaNullableLocalDateTimeConverter : JsonConverter<DateTime?>
    {
        private const string Format = "yyyy-MM-dd'T'HH:mm:ss";

        public override DateTime? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            if (reader.TokenType == JsonTokenType.Null)
            {
                return null;
            }

            var value = reader.GetString();
            if (string.IsNullOrWhiteSpace(value))
            {
                return null;
            }

            var normalized = value.Length >= 19 ? value[..19] : value;
            return DateTime.ParseExact(normalized, Format, CultureInfo.InvariantCulture);
        }

        public override void Write(Utf8JsonWriter writer, DateTime? value, JsonSerializerOptions options)
        {
            if (value is null)
            {
                writer.WriteNullValue();
                return;
            }

            writer.WriteStringValue(value.Value.ToString(Format, CultureInfo.InvariantCulture));
        }
    }
}