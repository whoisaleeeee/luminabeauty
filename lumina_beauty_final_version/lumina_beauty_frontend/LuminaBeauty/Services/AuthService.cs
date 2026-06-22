using System;
using System.Threading.Tasks;

namespace LuminaBeauty.Services
{
    public class AuthService
    {
        private readonly LocalStorageService _storage;
        private bool _isLoggedIn;

        public event Action? OnChange;

        public AuthService(LocalStorageService storage)
        {
            _storage = storage;
        }

        public async Task InitializeAsync()
        {
            _isLoggedIn = await _storage.GetItemAsync<bool>("lumina_is_logged_in");
            OnChange?.Invoke();
        }

        public bool IsLoggedIn => _isLoggedIn;

        public async Task LoginAsync()
        {
            _isLoggedIn = true;
            await _storage.SetItemAsync("lumina_is_logged_in", true);
            OnChange?.Invoke();
        }

        public async Task LogoutAsync()
        {
            _isLoggedIn = false;
            await _storage.RemoveItemAsync("lumina_is_logged_in");
            OnChange?.Invoke();
        }
    }
}
