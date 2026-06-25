using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    public class MovimientoPuntosAppService
    {
        private readonly MovimientoPuntosFidelidadRestService _movimientoRestService;

        public MovimientoPuntosAppService(
            MovimientoPuntosFidelidadRestService movimientoRestService)
        {
            _movimientoRestService = movimientoRestService;
        }

        public Task<List<MovimientoPuntosFidelidad>> ListarPorClienteAsync(
            int idCliente)
        {
            return _movimientoRestService.ListarPorClienteAsync(idCliente);
        }
    }
}