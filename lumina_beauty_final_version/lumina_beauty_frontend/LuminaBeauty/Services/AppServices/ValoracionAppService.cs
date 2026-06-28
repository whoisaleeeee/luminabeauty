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
    }
}