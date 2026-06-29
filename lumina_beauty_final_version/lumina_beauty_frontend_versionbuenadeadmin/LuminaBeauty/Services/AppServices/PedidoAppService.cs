using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices;

public class PedidoAppService
{
    private readonly PedidoRestService _pedidoRestService;

    public PedidoAppService(PedidoRestService pedidoRestService)
    {
        _pedidoRestService = pedidoRestService;
    }

    public Task<Pedido?> CrearPedidoAsync(Pedido pedido)
    {
        return _pedidoRestService.CrearPedidoAsync(pedido);
    }

    public Task<Pedido?> BuscarPedidoAsync(int idPedido)
    {
        return _pedidoRestService.BuscarPedidoAsync(idPedido);
    }

    public Task<List<Pedido>> ListarPedidosAsync()
    {
        return _pedidoRestService.ListarPedidosAsync();
    }

    public Task<List<Pedido>> ListarPedidosPorClienteAsync(int idCliente)
    {
        return _pedidoRestService.ListarPedidosPorClienteAsync(idCliente);
    }

    public Task<int> CancelarPedidoAsync(int idPedido)
    {
        return _pedidoRestService.CancelarPedidoAsync(idPedido);
    }

    public Task<Pedido?> ActualizarEstadoPedidoAsync(
        int idPedido,
        string estadoNuevo)
    {
        return _pedidoRestService.ActualizarEstadoPedidoAsync(
            idPedido,
            estadoNuevo);
    }
}