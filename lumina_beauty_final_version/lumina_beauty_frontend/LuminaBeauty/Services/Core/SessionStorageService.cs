using System.Text.Json;
using Microsoft.JSInterop;

namespace LuminaBeauty.Services
{
    public class SessionStorageService
    {
        private readonly IJSRuntime _jsRuntime;

        public SessionStorageService(IJSRuntime jsRuntime)
        {
            _jsRuntime = jsRuntime;
        }

        public async Task SetItemAsync<T>(string key, T value)
        {
            try
            {
                var json = JsonSerializer.Serialize(value);

                await _jsRuntime.InvokeVoidAsync(
                    "sessionStorage.setItem",
                    key,
                    json
                );
            }
            catch
            {
            }
        }

        public async Task<T?> GetItemAsync<T>(string key)
        {
            try
            {
                var json = await _jsRuntime.InvokeAsync<string?>(
                    "sessionStorage.getItem",
                    key
                );

                if (string.IsNullOrWhiteSpace(json))
                {
                    return default;
                }

                return JsonSerializer.Deserialize<T>(json);
            }
            catch
            {
                return default;
            }
        }

        public async Task RemoveItemAsync(string key)
        {
            try
            {
                await _jsRuntime.InvokeVoidAsync(
                    "sessionStorage.removeItem",
                    key
                );
            }
            catch
            {
            }
        }
    }
}