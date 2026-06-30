using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class ValoracionAppService
    {
        private readonly ValoracionRestService _valoracionRestService;

        public ValoracionAppService(
            ValoracionRestService valoracionRestService)
        {
            _valoracionRestService = valoracionRestService;
        }

        public Task<List<Valoracion>> ListarPublicadasPorProductoAsync(
            int idProducto)
        {
            return _valoracionRestService
                .ListarPublicadasPorProductoAsync(idProducto);
        }

        public Task<List<Valoracion>> ListarPorClienteAsync(
            int idCliente)
        {
            return _valoracionRestService
                .ListarPorClienteAsync(idCliente);
        }

        public Task<Valoracion> RegistrarAsync(
            int idCliente,
            int idProducto,
            int idDetallePedido,
            int calificacion,
            string comentario)
        {
            return _valoracionRestService.RegistrarAsync(
                idCliente,
                idProducto,
                idDetallePedido,
                calificacion,
                comentario
            );
        }

        public Task<List<Valoracion>> ListarTodasAsync()
        {
            return _valoracionRestService.ListarTodasAsync();
        }

        public Task<Valoracion?> PublicarAsync(int idValoracion)
        {
            return _valoracionRestService.PublicarAsync(idValoracion);
        }

        public Task<Valoracion?> RechazarAsync(int idValoracion)
        {
            return _valoracionRestService.RechazarAsync(idValoracion);
        }
    }
}