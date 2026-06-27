using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class EnvioAppService
    {
        private readonly EnvioRestService _envioRestService;

        public EnvioAppService(EnvioRestService envioRestService)
        {
            _envioRestService = envioRestService;
        }

        public Task<Envio?> BuscarPorPedidoAsync(int idPedido)
        {
            return _envioRestService.BuscarPorPedidoAsync(idPedido);
        }
    }
}