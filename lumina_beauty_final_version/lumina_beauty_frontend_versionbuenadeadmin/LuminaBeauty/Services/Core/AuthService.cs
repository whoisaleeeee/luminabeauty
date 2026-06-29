using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    public enum TipoSesion
    {
        Ninguna,
        Cliente,
        Admin
    }

    public class AuthService
    {
        private const string LoginStorageKey = "lumina_is_logged_in";
        private const string SessionTypeStorageKey = "lumina_session_type";
        private const string ClientStorageKey = "lumina_current_client";
        private const string EmployeeStorageKey = "lumina_current_employee";

        private readonly LocalStorageService _storage;
        private readonly AuthRestService _authRestService;

        private bool _isLoggedIn;
        private TipoSesion _tipoSesion = TipoSesion.Ninguna;
        private Cliente? _currentClient;
        private Empleado? _currentEmployee;

        public event Action? OnChange;

        public AuthService(
            LocalStorageService storage,
            AuthRestService authRestService)
        {
            _storage = storage;
            _authRestService = authRestService;
        }

        public bool IsLoggedIn => _isLoggedIn;
        public bool IsAdmin => _tipoSesion == TipoSesion.Admin;
        public bool IsCliente => _tipoSesion == TipoSesion.Cliente;

        public TipoSesion TipoSesionActual => _tipoSesion;
        public Cliente? CurrentClient => _currentClient;
        public Empleado? CurrentEmployee => _currentEmployee;

        public async Task InitializeAsync()
        {
            _isLoggedIn = await _storage.GetItemAsync<bool>(LoginStorageKey);

            if (!_isLoggedIn)
            {
                OnChange?.Invoke();
                return;
            }

            var tipoSesionGuardado =
                await _storage.GetItemAsync<string>(SessionTypeStorageKey);

            if (Enum.TryParse<TipoSesion>(
                    tipoSesionGuardado,
                    ignoreCase: true,
                    out var tipoSesion))
            {
                _tipoSesion = tipoSesion;
            }

            if (_tipoSesion == TipoSesion.Admin)
            {
                _currentEmployee =
                    await _storage.GetItemAsync<Empleado>(EmployeeStorageKey);

                if (_currentEmployee == null || _currentEmployee.Id <= 0)
                {
                    await LogoutAsync();
                    return;
                }
            }
            else if (_tipoSesion == TipoSesion.Cliente)
            {
                _currentClient =
                    await _storage.GetItemAsync<Cliente>(ClientStorageKey);

                if (_currentClient == null || _currentClient.Id <= 0)
                {
                    await LogoutAsync();
                    return;
                }
            }
            else
            {
                await LogoutAsync();
                return;
            }

            OnChange?.Invoke();
        }

        public async Task<TipoSesion> LoginAsync(string correo, string contrasena)
        {
            var empleado = await _authRestService.LoginEmpleadoAsync(correo, contrasena);

            if (empleado != null && empleado.Id > 0)
            {
                await SetAdminSessionAsync(empleado);
                return TipoSesion.Admin;
            }

            var cliente = await _authRestService.LoginClienteAsync(correo, contrasena);

            if (cliente != null && cliente.Id > 0)
            {
                await SetClienteSessionAsync(cliente);
                return TipoSesion.Cliente;
            }

            return TipoSesion.Ninguna;
        }

        public async Task LoginAsync(Cliente cliente)
        {
            if (cliente == null || cliente.Id <= 0)
            {
                throw new ArgumentException(
                    "El cliente registrado no tiene un identificador válido."
                );
            }

            await SetClienteSessionAsync(cliente);
        }

        public async Task<bool> RegistrarEIniciarSesionAsync(Cliente? cliente)
        {
            if (cliente == null || cliente.Id <= 0)
            {
                return false;
            }

            await SetClienteSessionAsync(cliente);
            return true;
        }

        public async Task RefreshCurrentClientAsync(Cliente cliente)
        {
            if (!IsCliente)
            {
                return;
            }

            _currentClient = cliente;
            await _storage.SetItemAsync(ClientStorageKey, cliente);
            OnChange?.Invoke();
        }

        private async Task SetClienteSessionAsync(Cliente cliente)
        {
            _isLoggedIn = true;
            _tipoSesion = TipoSesion.Cliente;
            _currentClient = cliente;
            _currentEmployee = null;

            await _storage.SetItemAsync(LoginStorageKey, true);
            await _storage.SetItemAsync(
                SessionTypeStorageKey,
                TipoSesion.Cliente.ToString()
            );
            await _storage.SetItemAsync(ClientStorageKey, cliente);

            await _storage.RemoveItemAsync(EmployeeStorageKey);

            OnChange?.Invoke();
        }

        private async Task SetAdminSessionAsync(Empleado empleado)
        {
            _isLoggedIn = true;
            _tipoSesion = TipoSesion.Admin;
            _currentEmployee = empleado;
            _currentClient = null;

            await _storage.SetItemAsync(LoginStorageKey, true);
            await _storage.SetItemAsync(
                SessionTypeStorageKey,
                TipoSesion.Admin.ToString()
            );
            await _storage.SetItemAsync(EmployeeStorageKey, empleado);

            await _storage.RemoveItemAsync(ClientStorageKey);

            OnChange?.Invoke();
        }

        public async Task LogoutAsync()
        {
            _isLoggedIn = false;
            _tipoSesion = TipoSesion.Ninguna;
            _currentClient = null;
            _currentEmployee = null;

            await _storage.RemoveItemAsync(LoginStorageKey);
            await _storage.RemoveItemAsync(SessionTypeStorageKey);
            await _storage.RemoveItemAsync(ClientStorageKey);
            await _storage.RemoveItemAsync(EmployeeStorageKey);

            OnChange?.Invoke();
        }
    }
}