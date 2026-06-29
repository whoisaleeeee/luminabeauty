using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class PagoAppService
    {
        private readonly PagoRestService _pagoRestService;

        public PagoAppService(PagoRestService pagoRestService)
        {
            _pagoRestService = pagoRestService;
        }

        public async Task<Pago?> RegistrarPagoCompletadoAsync(
            int idPedido,
            decimal monto,
            string metodoPago)
        {
            if (idPedido <= 0 || monto <= 0)
            {
                return null;
            }

            var pago = new Pago
            {
                Pedido = new Pedido
                {
                    IdPedido = idPedido
                },
                MetodoDePago = new MetodoDePago
                {
                    IdMetodoPago = ResolverIdMetodoPago(metodoPago)
                },
                Monto = monto,
                Estado = "PENDIENTE",
                ReferenciaTransaccion = string.Empty
            };

            var pagoRegistrado = await _pagoRestService.RegistrarPagoAsync(pago);

            if (pagoRegistrado == null || pagoRegistrado.IdPago <= 0)
            {
                return null;
            }

            string referencia = GenerarReferenciaTransaccion(metodoPago);

            return await _pagoRestService.CompletarPagoAsync(
                pagoRegistrado.IdPago,
                referencia);
        }

        public int ResolverIdMetodoPago(string metodoPago)
        {
            return metodoPago?.Trim().ToLowerInvariant() switch
            {
                "plin" => 5,
                "yape" => 3,
                "card" => 1,
                "tarjeta" => 1,
                _ => 1
            };
        }

        public string ObtenerNombreMetodoPago(string metodoPago)
        {
            return metodoPago?.Trim().ToLowerInvariant() switch
            {
                "plin" => "Plin",
                "yape" => "Yape",
                "card" => "Tarjeta de crédito o débito",
                "tarjeta" => "Tarjeta de crédito o débito",
                _ => "Tarjeta de crédito o débito"
            };
        }

        private static string GenerarReferenciaTransaccion(string metodoPago)
        {
            string prefijo = metodoPago?.Trim().ToUpperInvariant() switch
            {
                "PLIN" => "PLIN",
                "YAPE" => "YAPE",
                "CARD" => "CARD",
                "TARJETA" => "CARD",
                _ => "CARD"
            };

            return $"SIM-{prefijo}-{DateTime.Now:yyyyMMddHHmmssfff}";
        }
    }
}
