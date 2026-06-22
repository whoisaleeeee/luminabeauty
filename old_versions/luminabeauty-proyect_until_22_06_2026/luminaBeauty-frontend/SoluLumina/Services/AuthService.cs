using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

namespace LuminaBeauty.Services
{
    public class AuthService
    {
        private readonly LocalStorageService _storage;
        private readonly HttpClient _httpClient;

        private readonly string _javaUrl = "LuminaBeauty-Servicios/webresources/ClienteRS";
        private bool _isLoggedIn;
        private string? _email;

        public event Action? OnChange;

        public AuthService(LocalStorageService storage, HttpClient httpClient)
        {
            _storage = storage;
            _httpClient = httpClient;
        }

        public async Task InitializeAsync()
        {
            _isLoggedIn = await _storage.GetItemAsync<bool>("lumina_is_logged_in");
            _email = await _storage.GetItemAsync<string>("lumina_user_email");
            OnChange?.Invoke();
        }

        public bool IsLoggedIn => _isLoggedIn;
        public string? Email => _email;
        public bool IsAdmin => IsLuminaAdminEmail(_email);

        public Task LoginAsync()
        {
            return SetSessionAsync("cliente@lumina.local");
        }

        
        public async Task<bool> LoginAsync(string email, string password)
        {
            if (string.IsNullOrWhiteSpace(email))
            {
                return false;
            }

            if (IsLuminaAdminEmail(email))
            {
                await SetSessionAsync(email);
                return true;
            }

            try
            {
                var clienteDto = new
                {
                    correo = email,
                    nombre = "Usuario Logueado"
                };

                var response = await _httpClient.PostAsJsonAsync(_javaUrl, clienteDto);

                if (response.IsSuccessStatusCode)
                {
                    await SetSessionAsync(email);
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al conectar la autenticación con Java: {ex.Message}");
                return false;
            }
        }

        private async Task SetSessionAsync(string email)
        {
            _isLoggedIn = true;
            _email = email.Trim();
            await _storage.SetItemAsync("lumina_is_logged_in", true);
            await _storage.SetItemAsync("lumina_user_email", _email);
            OnChange?.Invoke();
        }

        public static bool IsLuminaAdminEmail(string? email)
        {
            return !string.IsNullOrWhiteSpace(email)
                && email.Trim().EndsWith("@lumina.com", StringComparison.OrdinalIgnoreCase);
        }

        public async Task LogoutAsync()
        {
            _isLoggedIn = false;
            _email = null;
            await _storage.RemoveItemAsync("lumina_is_logged_in");
            await _storage.RemoveItemAsync("lumina_user_email");
            OnChange?.Invoke();
        }
    }
}
