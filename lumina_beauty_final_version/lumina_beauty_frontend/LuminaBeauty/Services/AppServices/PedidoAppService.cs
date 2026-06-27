using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class PedidoAppService
    {
        private readonly PedidoRestService _pedidoRestService;

        public PedidoAppService(PedidoRestService pedidoRestService)
        {
            _pedidoRestService = pedidoRestService;
        }

        public async Task<Pedido?> CrearPedidoAsync(Pedido pedido)
        {
            return await _pedidoRestService.CrearPedidoAsync(pedido);
        }

        public async Task<Pedido?> BuscarPedidoAsync(int idPedido)
        {
            return await _pedidoRestService.BuscarPedidoAsync(idPedido);
        }

        public async Task<List<Pedido>> ListarPedidosAsync()
        {
            return await _pedidoRestService.ListarPedidosAsync();
        }

        public async Task<List<Pedido>> ListarPedidosPorClienteAsync(int idCliente)
        {
            return await _pedidoRestService.ListarPedidosPorClienteAsync(idCliente);
        }

        public async Task<int> CancelarPedidoAsync(int idPedido)
        {
            return await _pedidoRestService.CancelarPedidoAsync(idPedido);
        }

        public async Task<Pedido?> ActualizarEstadoPedidoAsync(int idPedido, string estadoNuevo)
        {
            return await _pedidoRestService.ActualizarEstadoPedidoAsync(idPedido, estadoNuevo);
        }
    }
}