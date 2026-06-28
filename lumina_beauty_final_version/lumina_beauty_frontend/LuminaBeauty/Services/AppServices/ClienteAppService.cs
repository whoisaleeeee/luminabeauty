using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices;

public class ClienteAppService
{
    private readonly ClienteRestService _clienteRestService;
    private readonly DireccionRestService _direccionRestService;

    public ClienteAppService(
        ClienteRestService clienteRestService,
        DireccionRestService direccionRestService)
    {
        _clienteRestService = clienteRestService;
        _direccionRestService = direccionRestService;
    }

    public Task<Cliente?> RegistrarClienteAsync(
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

        return _clienteRestService.RegistrarClienteAsync(nuevoCliente);
    }

    public Task<Cliente?> ObtenerClienteAsync(int idCliente)
    {
        return _clienteRestService.BuscarClienteAsync(idCliente);
    }

    public Task<Cliente?> ActualizarClienteAsync(Cliente cliente)
    {
        return _clienteRestService.ActualizarClienteAsync(cliente);
    }

    public Task<List<Direccion>> ObtenerDireccionesAsync(int idCliente)
    {
        return _direccionRestService.ListarPorClienteAsync(idCliente);
    }

    public Task<Direccion?> GuardarDireccionAsync(Direccion direccion)
    {
        return direccion.IdDireccion > 0
            ? _direccionRestService.ActualizarAsync(direccion)
            : _direccionRestService.RegistrarAsync(direccion);
    }

    public Task<bool> EliminarDireccionAsync(int idDireccion)
    {
        return _direccionRestService.EliminarAsync(idDireccion);
    }

    public async Task<bool> MarcarDireccionPrincipalAsync(
        Cliente cliente,
        Direccion direccion)
    {
        cliente.DireccionPrincipal = direccion;

        var actualizado = await _clienteRestService.ActualizarClienteAsync(cliente);

        return actualizado is not null;
    }

    public Task<int> SumarPuntosAsync(int idCliente, int puntos)
    {
        return _clienteRestService.SumarPuntosAsync(idCliente, puntos);
    }
}