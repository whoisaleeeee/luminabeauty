using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class CarroAppService
    {
        private readonly CarroRestService _carroRestService;
        private readonly ProductoRestService _productoRestService;

        public CarroAppService(CarroRestService carroRestService, ProductoRestService productoRestService)
        {
            _carroRestService = carroRestService;
            _productoRestService = productoRestService;
        }

        // ── VALIDAR STOCK ─────────────────────────────────────────────

        public async Task<bool> ValidarStockAsync(int idProducto, int cantidad)
        {
            var resultado = await _productoRestService.ValidarStockAsync(idProducto, cantidad);
            return resultado == 1;
        }

        public async Task<bool> ValidarStockAsync(string idProducto, int cantidad)
        {
            int idProductoConvertido = ConvertirIdProducto(idProducto);

            if (idProductoConvertido <= 0)
            {
                return false;
            }

            return await ValidarStockAsync(idProductoConvertido, cantidad);
        }

        // ── DESCONTAR STOCK ──────────────────────────────────────────

        public async Task<bool> DescontarStockAsync(int idProducto, int cantidad)
        {
            var resultado = await _productoRestService.DescontarStockAsync(idProducto, cantidad);
            return resultado == 1;
        }

        public async Task<bool> DescontarStockAsync(string idProducto, int cantidad)
        {
            int idProductoConvertido = ConvertirIdProducto(idProducto);

            if (idProductoConvertido <= 0)
            {
                return false;
            }

            return await DescontarStockAsync(idProductoConvertido, cantidad);
        }

        // ── AGREGAR PRODUCTO AL CARRO ────────────────────────────────

        public async Task<int> AgregarProductoAsync(int idCarro, int idProducto, int cantidad)
        {
            return await _carroRestService.AgregarProductoAsync(idCarro, idProducto, cantidad);
        }

        public async Task<int> AgregarProductoAsync(int idCarro, string idProducto, int cantidad)
        {
            int idProductoConvertido = ConvertirIdProducto(idProducto);

            if (idProductoConvertido <= 0)
            {
                return 0;
            }

            return await AgregarProductoAsync(idCarro, idProductoConvertido, cantidad);
        }

        // ── MÉTODO AUXILIAR ──────────────────────────────────────────

        private int ConvertirIdProducto(string idProducto)
        {
            if (string.IsNullOrWhiteSpace(idProducto))
            {
                return 0;
            }

            if (int.TryParse(idProducto, out int idConvertido))
            {
                return idConvertido;
            }

            return 0;
        }
    }
}