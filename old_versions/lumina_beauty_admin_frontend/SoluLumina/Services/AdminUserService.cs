using SoluLumina.Models;

namespace SoluLumina.Services
{
    /// <summary>
    /// Servicio singleton que carga el empleado administrador una sola vez
    /// y lo expone a todas las páginas del portal admin.
    /// </summary>
    public class AdminUserService
    {
        private readonly IConfiguration _configuration;
        private Empleado? _adminActual;
        private bool _cargado = false;

        public AdminUserService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public Empleado? AdminActual => _adminActual;

        public string NombreCompleto =>
            _adminActual != null
                ? $"{_adminActual.Nombres} {_adminActual.Apellidos}".Trim()
                : "Administrador";

        public string Correo => _adminActual?.Correo ?? "";

        public string Rol => _adminActual?.Rol ?? "ADMIN";

        public async Task CargarAdminAsync()
        {
            if (_cargado) return;

            try
            {
                var urlBase = _configuration["ApiSettings:URL_BASE"] ?? "";
                var empleados = await new HttpClientUtils<List<Empleado>>().get(urlBase + "/EmpleadoRS/listar");

                if (empleados != null && empleados.Any())
                {
                    // Toma el primer empleado con rol ADMIN, o simplemente el primero
                    _adminActual = empleados.FirstOrDefault(e =>
                        string.Equals(e.Rol, "ADMIN", StringComparison.OrdinalIgnoreCase))
                        ?? empleados.First();
                }

                _cargado = true;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[AdminUserService] Error cargando empleado: {ex.Message}");
            }
        }
    }
}
