using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    /// <summary>
    /// Servicio de aplicación para operaciones relacionadas con el Cliente.
    /// Delega las llamadas REST al ClienteRestService de Servicios/REST/.
    /// </summary>
    public class ClienteAppService
    {
        private readonly ClienteRestService _clienteRestService;

        public ClienteAppService(ClienteRestService clienteRestService)
        {
            _clienteRestService = clienteRestService;
        }

        /// <summary>
        /// Registra un nuevo cliente en la base de datos via REST.
        /// Retorna el cliente registrado o null si falla.
        /// </summary>
        public async Task<Cliente?> RegistrarClienteAsync(
            string nombre,
            string apellido,
            string dni,
            string correo,
            string contrasena)
        {
            var nuevoCliente = new Cliente
            {
                Nombre = nombre,
                Apellido = apellido,
                Dni = dni,
                Correo = correo,
                Contrasena = contrasena,
                Estado = 1,
                PuntosFidelidad = 0,
                NivelCliente = "BRONCE"
            };

            return await _clienteRestService.RegistrarClienteAsync(nuevoCliente);
        }

        /// <summary>
        /// Suma puntos de fidelidad al cliente tras una compra exitosa.
        /// </summary>
        public async Task<int> SumarPuntosAsync(int idCliente, int puntos)
        {
            return await _clienteRestService.SumarPuntosAsync(idCliente, puntos);
        }
    }
}