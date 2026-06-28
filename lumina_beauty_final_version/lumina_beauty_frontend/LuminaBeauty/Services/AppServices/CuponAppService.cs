using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class CuponAppService
    {
        private readonly CuponRestService _cuponRestService;

        public CuponAppService(CuponRestService cuponRestService)
        {
            _cuponRestService = cuponRestService;
        }

        public Task<Cupon?> AplicarAsync(string codigo)
        {
            return _cuponRestService.AplicarAsync(codigo);
        }

        public decimal CalcularDescuento(Cupon cupon, decimal subtotal)
        {
            if (cupon == null || subtotal <= 0)
            {
                return 0m;
            }

            var descuento = cupon.TipoDescuento.Equals(
                "PORCENTAJE",
                StringComparison.OrdinalIgnoreCase)
                ? subtotal * cupon.ValorDescuento / 100m
                : cupon.ValorDescuento;

            return Math.Min(descuento, subtotal);
        }
    }
}