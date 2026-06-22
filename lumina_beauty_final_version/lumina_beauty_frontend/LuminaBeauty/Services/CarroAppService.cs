using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    /// <summary>
    /// Servicio de aplicación para operaciones del carro de compras.
    /// Delega las llamadas REST al CarroRestService de Servicios/REST/.
    /// </summary>
    public class CarroAppService
    {
        private readonly CarroRestService _carroRestService;
        private readonly ProductoRestService _productoRestService;

        public CarroAppService(CarroRestService carroRestService, ProductoRestService productoRestService)
        {
            _carroRestService = carroRestService;
            _productoRestService = productoRestService;
        }

        /// <summary>
        /// Valida el stock disponible para un producto antes de confirmar la compra.
        /// Retorna true si hay suficiente stock.
        /// </summary>
        public async Task<bool> ValidarStockAsync(string idProducto, int cantidad)
        {
            var resultado = await _productoRestService.ValidarStockAsync(idProducto, cantidad);
            return resultado == 1;
        }

        /// <summary>
        /// Descuenta el stock de un producto después de una compra exitosa.
        /// Retorna true si se descontó correctamente.
        /// </summary>
        public async Task<bool> DescontarStockAsync(string idProducto, int cantidad)
        {
            var resultado = await _productoRestService.DescontarStockAsync(idProducto, cantidad);
            return resultado == 1;
        }

        /// <summary>
        /// Registra que un producto fue agregado al carro en el backend.
        /// </summary>
        public async Task<int> AgregarProductoAsync(int idCarro, string idProducto, int cantidad)
        {
            return await _carroRestService.AgregarProductoAsync(idCarro, idProducto, cantidad);
        }
    }
}
