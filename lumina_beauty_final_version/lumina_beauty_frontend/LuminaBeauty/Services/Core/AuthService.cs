using System;
using System.Threading.Tasks;
using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    public class AuthService
    {
        private const string LoginStorageKey = "lumina_is_logged_in";
        private const string ClientStorageKey = "lumina_current_client";

        private readonly LocalStorageService _storage;
        private readonly AuthRestService _authRestService;

        private bool _isLoggedIn;
        private Cliente? _currentClient;

        public event Action? OnChange;

        public AuthService(
            LocalStorageService storage,
            AuthRestService authRestService)
        {
            _storage = storage;
            _authRestService = authRestService;
        }

        public bool IsLoggedIn => _isLoggedIn;

        public Cliente? CurrentClient => _currentClient;

        public async Task InitializeAsync()
        {
            _isLoggedIn = await _storage.GetItemAsync<bool>(LoginStorageKey);

            if (_isLoggedIn)
            {
                _currentClient =
                    await _storage.GetItemAsync<Cliente>(ClientStorageKey);

                if (_currentClient == null || _currentClient.Id <= 0)
                {
                    _isLoggedIn = false;
                    await _storage.RemoveItemAsync(LoginStorageKey);
                    await _storage.RemoveItemAsync(ClientStorageKey);
                }
            }

            OnChange?.Invoke();
        }

        public async Task LoginAsync()
        {
            await SetLoggedInAsync(null);
        }

        public async Task LoginAsync(Cliente cliente)
        {
            if (cliente == null || cliente.Id <= 0)
            {
                throw new ArgumentException("El cliente registrado no tiene un identificador vÃ¡lido.");
            }

            await SetLoggedInAsync(cliente);
        }

        public async Task<bool> LoginAsync(string correo, string contrasena)
        {
            var cliente = await _authRestService.LoginClienteAsync(
                correo,
                contrasena
            );

            if (cliente == null || cliente.Id <= 0)
            {
                return false;
            }

            await SetLoggedInAsync(cliente);
            return true;
        }

        public async Task RefreshCurrentClientAsync(Cliente cliente)
        {
            _currentClient = cliente;
            await _storage.SetItemAsync(ClientStorageKey, cliente);
            OnChange?.Invoke();
        }

        private async Task SetLoggedInAsync(Cliente? cliente)
        {
            _isLoggedIn = true;
            _currentClient = cliente;

            await _storage.SetItemAsync(LoginStorageKey, true);

            if (cliente != null)
            {
                await _storage.SetItemAsync(ClientStorageKey, cliente);
            }

            OnChange?.Invoke();
        }

        public async Task LogoutAsync()
        {
            _isLoggedIn = false;
            _currentClient = null;

            await _storage.RemoveItemAsync(LoginStorageKey);
            await _storage.RemoveItemAsync(ClientStorageKey);

            OnChange?.Invoke();
        }

public async Task<bool> RegistrarEIniciarSesionAsync(Cliente? cliente)
{
    if (cliente == null || cliente.Id <= 0)
    {
        return false;
    }

    await SetLoggedInAsync(cliente);
    return true;
}
    }
}