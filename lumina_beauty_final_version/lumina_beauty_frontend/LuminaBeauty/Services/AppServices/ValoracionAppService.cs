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

        public Task<List<Valoracion>> ListarPorClienteAsync(int idCliente)
        {
            return _valoracionRestService
                .ListarPorClienteAsync(idCliente);
        }

        public Task<Valoracion?> RegistrarAsync(Valoracion valoracion)
        {
            return _valoracionRestService.RegistrarAsync(valoracion);
        }
    }
}