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

        private readonly LocalStorageService _localStorage;
        private readonly SessionStorageService _sessionStorage;
        private readonly AuthRestService _authRestService;

        private bool _isLoggedIn;
        private TipoSesion _tipoSesion = TipoSesion.Ninguna;
        private Cliente? _currentClient;
        private Empleado? _currentEmployee;

        public event Action? OnChange;

        public AuthService(
            LocalStorageService localStorage,
            SessionStorageService sessionStorage,
            AuthRestService authRestService)
        {
            _localStorage = localStorage;
            _sessionStorage = sessionStorage;
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
            // Primero intenta recuperar una sesión de empleado.
            var employeeIsLoggedIn =
                await _sessionStorage.GetItemAsync<bool>(LoginStorageKey);

            if (employeeIsLoggedIn)
            {
                var employeeSessionType =
                    await _sessionStorage.GetItemAsync<string>(SessionTypeStorageKey);

                if (Enum.TryParse<TipoSesion>(
                        employeeSessionType,
                        ignoreCase: true,
                        out var employeeType) &&
                    employeeType == TipoSesion.Admin)
                {
                    var employee =
                        await _sessionStorage.GetItemAsync<Empleado>(EmployeeStorageKey);

                    if (employee != null && employee.Id > 0)
                    {
                        _isLoggedIn = true;
                        _tipoSesion = TipoSesion.Admin;
                        _currentEmployee = employee;
                        _currentClient = null;

                        OnChange?.Invoke();
                        return;
                    }
                }

                await ClearEmployeeSessionAsync();
            }

            // Luego intenta recuperar una sesión persistente de cliente.
            var clientIsLoggedIn =
                await _localStorage.GetItemAsync<bool>(LoginStorageKey);

            if (clientIsLoggedIn)
            {
                var clientSessionType =
                    await _localStorage.GetItemAsync<string>(SessionTypeStorageKey);

                if (Enum.TryParse<TipoSesion>(
                        clientSessionType,
                        ignoreCase: true,
                        out var clientType) &&
                    clientType == TipoSesion.Cliente)
                {
                    var client =
                        await _localStorage.GetItemAsync<Cliente>(ClientStorageKey);

                    if (client != null && client.Id > 0)
                    {
                        _isLoggedIn = true;
                        _tipoSesion = TipoSesion.Cliente;
                        _currentClient = client;
                        _currentEmployee = null;

                        OnChange?.Invoke();
                        return;
                    }
                }

                await ClearClientSessionAsync();
            }

            _isLoggedIn = false;
            _tipoSesion = TipoSesion.Ninguna;
            _currentClient = null;
            _currentEmployee = null;

            OnChange?.Invoke();
        }

        public async Task<TipoSesion> LoginAsync(string correo, string contrasena)
        {
            var empleado =
                await _authRestService.LoginEmpleadoAsync(correo, contrasena);

            if (empleado != null && empleado.Id > 0)
            {
                await SetAdminSessionAsync(empleado);
                return TipoSesion.Admin;
            }

            var cliente =
                await _authRestService.LoginClienteAsync(correo, contrasena);

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

            await _localStorage.SetItemAsync(ClientStorageKey, cliente);

            OnChange?.Invoke();
        }

        private async Task SetClienteSessionAsync(Cliente cliente)
        {
            _isLoggedIn = true;
            _tipoSesion = TipoSesion.Cliente;
            _currentClient = cliente;
            _currentEmployee = null;

            await ClearEmployeeSessionAsync();

            await _localStorage.SetItemAsync(LoginStorageKey, true);
            await _localStorage.SetItemAsync(
                SessionTypeStorageKey,
                TipoSesion.Cliente.ToString()
            );
            await _localStorage.SetItemAsync(ClientStorageKey, cliente);
            await _localStorage.RemoveItemAsync(EmployeeStorageKey);

            OnChange?.Invoke();
        }

        private async Task SetAdminSessionAsync(Empleado empleado)
        {
            _isLoggedIn = true;
            _tipoSesion = TipoSesion.Admin;
            _currentEmployee = empleado;
            _currentClient = null;

            await ClearClientSessionAsync();

            await _sessionStorage.SetItemAsync(LoginStorageKey, true);
            await _sessionStorage.SetItemAsync(
                SessionTypeStorageKey,
                TipoSesion.Admin.ToString()
            );
            await _sessionStorage.SetItemAsync(EmployeeStorageKey, empleado);

            OnChange?.Invoke();
        }

        public async Task LogoutAsync()
        {
            _isLoggedIn = false;
            _tipoSesion = TipoSesion.Ninguna;
            _currentClient = null;
            _currentEmployee = null;

            await ClearClientSessionAsync();
            await ClearEmployeeSessionAsync();

            OnChange?.Invoke();
        }

        private async Task ClearClientSessionAsync()
        {
            await _localStorage.RemoveItemAsync(LoginStorageKey);
            await _localStorage.RemoveItemAsync(SessionTypeStorageKey);
            await _localStorage.RemoveItemAsync(ClientStorageKey);
            await _localStorage.RemoveItemAsync(EmployeeStorageKey);
        }

        private async Task ClearEmployeeSessionAsync()
        {
            await _sessionStorage.RemoveItemAsync(LoginStorageKey);
            await _sessionStorage.RemoveItemAsync(SessionTypeStorageKey);
            await _sessionStorage.RemoveItemAsync(EmployeeStorageKey);
            await _sessionStorage.RemoveItemAsync(ClientStorageKey);
        }
    }
}