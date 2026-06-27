using LuminaBeauty.Services.PageStates;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Services.AppServices
{
    public class ProfileAppService
    {
        private readonly ClienteAppService _clienteAppService;
        private readonly PedidoAppService _pedidoAppService;
        private readonly EnvioAppService _envioAppService;
        private readonly MovimientoPuntosAppService _movimientoPuntosAppService;
        private readonly AuthService _authService;

        public ProfilePageState State { get; } = new();

        public ProfileAppService(
                    ClienteAppService clienteAppService,
                    PedidoAppService pedidoAppService,
                    EnvioAppService envioAppService,
                    MovimientoPuntosAppService movimientoPuntosAppService,
                    AuthService authService)
        {
            _clienteAppService = clienteAppService;
            _pedidoAppService = pedidoAppService;
            _envioAppService = envioAppService;
            _movimientoPuntosAppService = movimientoPuntosAppService;
            _authService = authService;
        }

        public async Task<ProfileActionResult> InitializeAsync(
            Cliente? sessionClient
        )
        {
            if (sessionClient is null || sessionClient.Id <= 0)
            {
                State.IsLoadingProfile = false;

                return new ProfileActionResult(
                    false,
                    "No se encontró una sesión de cliente válida."
                );
            }

            try
            {
                State.IsLoadingProfile = true;
                State.IsLoadingAddresses = true;

                State.CurrentClient = await CargarClienteAsync(
                    sessionClient.Id
                );

                if (State.CurrentClient is null)
                {
                    return new ProfileActionResult(
                        false,
                        "No se pudo cargar la información de tu perfil."
                    );
                }

                CargarCamposPerfil(State.CurrentClient);

                State.Addresses = await CargarDireccionesAsync(
                    State.CurrentClient.Id
                );

                return new ProfileActionResult(true, string.Empty);
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudo cargar la información de tu perfil."
                );
            }
            finally
            {
                State.IsLoadingProfile = false;
                State.IsLoadingAddresses = false;
            }
        }

        public async Task<ProfileActionResult> GuardarPerfilAsync()
        {
            if (State.CurrentClient is null)
            {
                return new ProfileActionResult(
                    false,
                    "No se encontró una sesión de cliente válida."
                );
            }

            try
            {
                State.IsSavingProfile = true;

                State.CurrentClient.Nombre =
                    State.ProfileFirstName.Trim();

                State.CurrentClient.Apellido =
                    State.ProfileLastName.Trim();

                State.CurrentClient.Correo =
                    State.ProfileEmail.Trim();

                State.CurrentClient.Telefono =
                    State.ProfilePhone.Trim();

                var actualizado = await ActualizarPerfilAsync(
                    State.CurrentClient
                );

                if (actualizado is null)
                {
                    return new ProfileActionResult(
                        false,
                        "No se pudieron guardar los cambios de perfil."
                    );
                }

                State.CurrentClient = actualizado;
                CargarCamposPerfil(actualizado);

                return new ProfileActionResult(
                    true,
                    "Cambios de perfil guardados correctamente."
                );
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudieron guardar los cambios de perfil."
                );
            }
            finally
            {
                State.IsSavingProfile = false;
            }
        }

        public async Task SetActiveTabAsync(string code)
        {
            State.ActiveTab = code;

            if (code == "pedidos" && State.Orders.Count == 0)
            {
                await CargarPedidosAsync();
            }

            if (code == "puntos" && State.PointMovements.Count == 0)
            {
                await CargarMovimientosPuntosAsync();
            }
        }

        public void AbrirNuevaDireccion()
        {
            State.EditingAddress = new Direccion
            {
                Pais = "Peru"
            };

            State.IsAddressEditorOpen = true;
        }

        public void AbrirEdicionDireccion(Direccion address)
        {
            State.EditingAddress = new Direccion
            {
                IdDireccion = address.IdDireccion,
                DireccionTexto = address.DireccionTexto,
                Ciudad = address.Ciudad,
                Pais = address.Pais,
                Referencia = address.Referencia,
                CodigoPostal = address.CodigoPostal
            };

            State.IsAddressEditorOpen = true;
        }

        public void CerrarEditorDireccion()
        {
            State.IsAddressEditorOpen = false;
            State.EditingAddress = new Direccion();
        }

        public async Task<ProfileActionResult> GuardarDireccionAsync()
        {
            if (State.CurrentClient is null)
            {
                return new ProfileActionResult(
                    false,
                    "No se encontró una sesión de cliente válida."
                );
            }

            try
            {
                State.IsSavingAddress = true;

                bool esEdicion = State.EditingAddress.IdDireccion > 0;

                var direccionGuardada = await GuardarDireccionInternaAsync(
                    State.EditingAddress,
                    State.CurrentClient
                );

                if (direccionGuardada is null)
                {
                    return new ProfileActionResult(
                        false,
                        "No se pudo guardar la dirección."
                    );
                }

                await RecargarDireccionesAsync();
                CerrarEditorDireccion();

                return new ProfileActionResult(
                    true,
                    esEdicion
                        ? "Dirección actualizada correctamente."
                        : "Dirección registrada correctamente."
                );
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudo guardar la dirección."
                );
            }
            finally
            {
                State.IsSavingAddress = false;
            }
        }

        public async Task<ProfileActionResult> MarcarPrincipalAsync(
            Direccion address
        )
        {
            if (State.CurrentClient is null)
            {
                return new ProfileActionResult(
                    false,
                    "No se encontró una sesión de cliente válida."
                );
            }

            try
            {
                State.IsSavingAddress = true;

                var actualizado = await MarcarDireccionPrincipalInternaAsync(
                    State.CurrentClient,
                    address
                );

                if (actualizado is null)
                {
                    return new ProfileActionResult(
                        false,
                        "No se pudo marcar la dirección como principal."
                    );
                }

                State.CurrentClient = actualizado;

                await RecargarDireccionesAsync();

                return new ProfileActionResult(
                    true,
                    "Dirección principal actualizada."
                );
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudo marcar la dirección como principal."
                );
            }
            finally
            {
                State.IsSavingAddress = false;
            }
        }

        public void PrepararEliminacionDireccion(Direccion address)
        {
            State.AddressPendingDeletion = address;
        }

        public void CancelarEliminacionDireccion()
        {
            State.AddressPendingDeletion = null;
        }

        public async Task<ProfileActionResult> EliminarDireccionAsync()
        {
            if (State.AddressPendingDeletion is null)
            {
                return new ProfileActionResult(
                    false,
                    "No se encontró una dirección para eliminar."
                );
            }

            try
            {
                State.IsDeletingAddress = true;

                bool eliminada = await _clienteAppService.EliminarDireccionAsync(
                    State.AddressPendingDeletion.IdDireccion
                );

                if (!eliminada)
                {
                    return new ProfileActionResult(
                        false,
                        "No se pudo eliminar la dirección."
                    );
                }

                State.AddressPendingDeletion = null;

                await RecargarDireccionesAsync();

                return new ProfileActionResult(
                    true,
                    "Dirección eliminada correctamente."
                );
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudo eliminar la dirección."
                );
            }
            finally
            {
                State.IsDeletingAddress = false;
            }
        }

        public async Task<ProfileActionResult> CargarPedidosAsync()
        {
            if (State.CurrentClient is null)
            {
                return new ProfileActionResult(
                    false,
                    "No se encontró una sesión de cliente válida."
                );
            }

            try
            {
                State.IsLoadingOrders = true;

                State.Orders = await _pedidoAppService
                    .ListarPedidosPorClienteAsync(State.CurrentClient.Id);

                return new ProfileActionResult(true, string.Empty);
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudieron cargar tus pedidos."
                );
            }
            finally
            {
                State.IsLoadingOrders = false;
            }
        }

        public async Task<ProfileActionResult> AbrirDetallePedidoAsync(
    Pedido order
)
        {
            try
            {
                State.IsLoadingOrderDetail = true;
                State.IsOrderDetailOpen = true;
                State.SelectedOrder = order;

                var pedidoCompleto = await _pedidoAppService.BuscarPedidoAsync(
                    order.IdPedido
                );

                if (pedidoCompleto is not null)
                {
                    State.SelectedOrder = pedidoCompleto;
                }

                State.SelectedOrderShipment =
                    await _envioAppService.BuscarPorPedidoAsync(order.IdPedido);

                return new ProfileActionResult(true, string.Empty);
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudo cargar el detalle y seguimiento del pedido."
                );
            }
            finally
            {
                State.IsLoadingOrderDetail = false;
            }
        }

        public void CerrarDetallePedido()
        {
            State.IsOrderDetailOpen = false;
            State.SelectedOrder = null;
            State.SelectedOrderShipment = null;
        }

        public async Task<ProfileActionResult> CargarMovimientosPuntosAsync()
        {
            if (State.CurrentClient is null)
            {
                return new ProfileActionResult(
                    false,
                    "No se encontró una sesión de cliente válida."
                );
            }

            try
            {
                State.IsLoadingPoints = true;

                State.PointMovements =
                    await _movimientoPuntosAppService.ListarPorClienteAsync(
                        State.CurrentClient.Id
                    );

                return new ProfileActionResult(true, string.Empty);
            }
            catch
            {
                return new ProfileActionResult(
                    false,
                    "No se pudo cargar el historial de puntos."
                );
            }
            finally
            {
                State.IsLoadingPoints = false;
            }
        }

        public bool EsDireccionPrincipal(Direccion address)
        {
            return State.CurrentClient?.DireccionPrincipal?.IdDireccion ==
                   address.IdDireccion;
        }

        private async Task RecargarDireccionesAsync()
        {
            if (State.CurrentClient is null)
            {
                return;
            }

            State.IsLoadingAddresses = true;

            try
            {
                State.CurrentClient = await CargarClienteAsync(
                    State.CurrentClient.Id
                );

                if (State.CurrentClient is not null)
                {
                    State.Addresses = await CargarDireccionesAsync(
                        State.CurrentClient.Id
                    );
                }
            }
            finally
            {
                State.IsLoadingAddresses = false;
            }
        }

        private void CargarCamposPerfil(Cliente client)
        {
            State.ProfileFirstName = client.Nombre ?? string.Empty;
            State.ProfileLastName = client.Apellido ?? string.Empty;
            State.ProfileEmail = client.Correo ?? string.Empty;
            State.ProfilePhone = client.Telefono ?? string.Empty;
        }

        private async Task<Cliente?> CargarClienteAsync(int idCliente)
        {
            var cliente = await _clienteAppService.ObtenerClienteAsync(
                idCliente
            );

            if (cliente is not null)
            {
                await _authService.RefreshCurrentClientAsync(cliente);
            }

            return cliente;
        }

        private Task<List<Direccion>> CargarDireccionesAsync(int idCliente)
        {
            return _clienteAppService.ObtenerDireccionesAsync(idCliente);
        }

        private async Task<Cliente?> ActualizarPerfilAsync(Cliente cliente)
        {
            var actualizado = await _clienteAppService.ActualizarClienteAsync(
                cliente
            );

            if (actualizado is not null)
            {
                await _authService.RefreshCurrentClientAsync(actualizado);
            }

            return actualizado;
        }

        private async Task<Direccion?> GuardarDireccionInternaAsync(
            Direccion direccion,
            Cliente cliente
        )
        {
            direccion.Cliente = new Cliente
            {
                Id = cliente.Id
            };

            return await _clienteAppService.GuardarDireccionAsync(direccion);
        }

        private async Task<Cliente?> MarcarDireccionPrincipalInternaAsync(
            Cliente cliente,
            Direccion direccion
        )
        {
            cliente.DireccionPrincipal = direccion;

            var actualizado = await _clienteAppService.ActualizarClienteAsync(
                cliente
            );

            if (actualizado is not null)
            {
                await _authService.RefreshCurrentClientAsync(actualizado);
            }

            return actualizado;
        }

        public string ObtenerInicialesCliente()
        {
            var nombres = State.ProfileFirstName
                .Trim()
                .Split(' ', StringSplitOptions.RemoveEmptyEntries);

            var apellidos = State.ProfileLastName
                .Trim()
                .Split(' ', StringSplitOptions.RemoveEmptyEntries);

            string inicialNombre = nombres.Length > 0
                ? nombres[0][0].ToString().ToUpperInvariant()
                : string.Empty;

            string inicialApellido = apellidos.Length > 0
                ? apellidos[0][0].ToString().ToUpperInvariant()
                : string.Empty;

            string iniciales = $"{inicialNombre}{inicialApellido}";

            return string.IsNullOrWhiteSpace(iniciales)
                ? "LB"
                : iniciales;
        }
    }
}