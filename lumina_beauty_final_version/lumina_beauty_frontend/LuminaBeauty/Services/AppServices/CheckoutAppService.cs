using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class CheckoutAppService
    {
        private readonly CarroAppService _carroAppService;
        private readonly CartService _cartService;
        private readonly CuponAppService _cuponAppService;
        private readonly UsoCuponAppService _usoCuponAppService;
        private readonly CheckoutRestService _checkoutRestService;

        public CheckoutAppService(
            CarroAppService carroAppService,
            CartService cartService,
            CuponAppService cuponAppService,
            UsoCuponAppService usoCuponAppService,
            CheckoutRestService checkoutRestService)
        {
            _carroAppService = carroAppService;
            _cartService = cartService;
            _cuponAppService = cuponAppService;
            _usoCuponAppService = usoCuponAppService;
            _checkoutRestService = checkoutRestService;
        }

        public async Task<CheckoutResult> ProcesarPedidoAsync(
            Cliente? cliente,
            IReadOnlyList<CartItem> items,
            DatosDespacho datosDespacho,
            Cupon? cuponAplicado = null,
            string metodoPago = "card")
        {
            if (cliente == null || cliente.Id <= 0)
            {
                return CheckoutResult.Error(
                    "Debes iniciar sesión antes de confirmar tu pedido.");
            }

            if (items == null || items.Count == 0)
            {
                return CheckoutResult.Error("Tu carrito está vacío.");
            }

            if (!datosDespacho.EsValido())
            {
                return CheckoutResult.Error(
                    "Completa los datos de entrega antes de confirmar tu pedido.");
            }

            foreach (var item in items)
            {
                var hayStock = await _carroAppService.ValidarStockAsync(
                    item.Product.IdProducto,
                    item.Quantity);

                if (!hayStock)
                {
                    return CheckoutResult.Error(
                        $"No hay stock suficiente para: {item.Product.Name}.");
                }
            }

            decimal subtotal = items.Sum(
                item => item.Product.Price * item.Quantity);

            var validacionCupon = await ValidarCuponAsync(
                cliente.Id,
                subtotal,
                cuponAplicado);

            if (!validacionCupon.EsValido)
            {
                return CheckoutResult.Error(validacionCupon.Mensaje);
            }

            var cupon = validacionCupon.Cupon;
            decimal descuento = validacionCupon.Descuento;
            decimal costoEnvio = subtotal >= 199m ? 0m : 15m;
            decimal total = Math.Max(0m, subtotal + costoEnvio - descuento);

            var pedido = new Pedido
            {
                CodigoPedido = "P" + DateTime.Now.ToString("yyMMddHHmmss"),
                CodigoCuponAplicado = cupon?.Codigo ?? string.Empty,
                SubtotalProductos = subtotal,
                CostoEnvio = costoEnvio,
                Descuento = descuento,
                Total = total,
                Estado = "PAGADO",
                Cliente = new Cliente
                {
                    Id = cliente.Id
                },
                Cupon = cupon == null
                    ? null
                    : new Cupon
                    {
                        IdCupon = cupon.IdCupon
                    },
                Detalles = items.Select(item => new DetallePedido
                {
                    Producto = new Producto
                    {
                        IdProducto = item.Product.IdProducto
                    },
                    Cantidad = item.Quantity,
                    PrecioUnitario = item.Product.Price,
                    Subtotal = item.Product.Price * item.Quantity
                }).ToList()
            };

            var envio = new Envio
            {
                ZonaEnvio = ResolverZonaEnvio(datosDespacho.Ciudad),
                Estado = "PREPARANDO",
                NumeroSeguimiento = string.Empty,
                DireccionEnvio = datosDespacho.Direccion.Trim(),
                CiudadEnvio = datosDespacho.Ciudad.Trim(),
                PaisEnvio = string.IsNullOrWhiteSpace(datosDespacho.Pais)
                    ? "Peru"
                    : datosDespacho.Pais.Trim(),
                ReferenciaEnvio = datosDespacho.Referencia.Trim(),
                CodigoPostalEnvio = datosDespacho.CodigoPostal.Trim()
            };

            var pago = new Pago
            {
                Monto = total,
                Estado = "COMPLETADO",
                ReferenciaTransaccion = GenerarReferencia(metodoPago),
                MetodoDePago = new MetodoDePago
                {
                    IdMetodoPago = ResolverIdMetodoPago(metodoPago)
                }
            };

            var resultado = await _checkoutRestService.ProcesarAsync(
                new CheckoutApiRequest
                {
                    Pedido = pedido,
                    Envio = envio,
                    Pago = pago
                });

            if (resultado == null || !resultado.Exitoso || resultado.IdPedido <= 0)
            {
                return CheckoutResult.Error(
                    resultado?.Mensaje ?? "No se pudo registrar el pago.");
            }

            await _cartService.ClearCartAsync();

           
            return CheckoutResult.Success(
            resultado.IdPedido,
            resultado.CodigoPedido,
            total);
        }

        private async Task<ValidacionCuponResult> ValidarCuponAsync(
            int idCliente,
            decimal subtotal,
            Cupon? cuponAplicado)
        {
            if (cuponAplicado == null || cuponAplicado.IdCupon <= 0)
            {
                return ValidacionCuponResult.SinCupon();
            }

            var cuponReal = await _cuponAppService.AplicarAsync(cuponAplicado.Codigo);

            if (cuponReal == null || cuponReal.IdCupon != cuponAplicado.IdCupon)
            {
                return ValidacionCuponResult.Error(
                    "El cupón ya no es válido o dejó de estar vigente.");
            }

            var clienteYaUsoCupon = await _usoCuponAppService.ClienteYaUsoAsync(
                idCliente,
                cuponReal.IdCupon);

            if (clienteYaUsoCupon)
            {
                return ValidacionCuponResult.Error(
                    "Ya utilizaste este cupón anteriormente.");
            }

            var descuento = _cuponAppService.CalcularDescuento(cuponReal, subtotal);

            return descuento <= 0
                ? ValidacionCuponResult.Error(
                    "No se pudo calcular el descuento del cupón.")
                : ValidacionCuponResult.Exito(cuponReal, descuento);
        }

        private static int ResolverIdMetodoPago(string metodoPago)
        {
            return metodoPago?.Trim().ToLowerInvariant() switch
            {
                "plin" => 5,
                "cash" => 7,
                "yape" => 3,
                _ => 1
            };
        }

        private static string GenerarReferencia(string metodoPago)
        {
            var prefijo = metodoPago?.Trim().ToUpperInvariant() switch
            {
                "PLIN" => "PLIN",
                "YAPE" => "YAPE",
                _ => "CARD"
            };

            return $"SIM-{prefijo}-{DateTime.Now:yyyyMMddHHmmssfff}";
        }

        private static string ResolverZonaEnvio(string ciudad)
        {
            return ciudad.Trim().Equals("Lima",
                StringComparison.OrdinalIgnoreCase)
                ? "LIMA"
                : "PROVINCIA";
        }
    }

    public class DatosDespacho
    {
        public string NombreContacto { get; set; } = string.Empty;
        public string Telefono { get; set; } = string.Empty;
        public string Direccion { get; set; } = string.Empty;
        public string Ciudad { get; set; } = string.Empty;
        public string Referencia { get; set; } = string.Empty;
        public string CodigoPostal { get; set; } = string.Empty;
        public string Pais { get; set; } = "Peru";

        public bool EsValido() =>
            !string.IsNullOrWhiteSpace(NombreContacto)
            && !string.IsNullOrWhiteSpace(Telefono)
            && !string.IsNullOrWhiteSpace(Direccion)
            && !string.IsNullOrWhiteSpace(Ciudad)
            && !string.IsNullOrWhiteSpace(Referencia);
    }

    internal class ValidacionCuponResult
    {
        public bool EsValido { get; private set; }
        public string Mensaje { get; private set; } = string.Empty;
        public Cupon? Cupon { get; private set; }
        public decimal Descuento { get; private set; }

        public static ValidacionCuponResult SinCupon() => new() { EsValido = true };

        public static ValidacionCuponResult Exito(Cupon cupon, decimal descuento) =>
            new() { EsValido = true, Cupon = cupon, Descuento = descuento };

        public static ValidacionCuponResult Error(string mensaje) =>
            new() { EsValido = false, Mensaje = mensaje };
    }

    public class CheckoutResult
    {
        public bool Exitoso { get; private set; }
        public string Mensaje { get; private set; } = string.Empty;
        public int IdPedido { get; private set; }
        public string CodigoPedido { get; private set; } = string.Empty;
        public decimal Total { get; private set; }

        public static CheckoutResult Success(int idPedido, string codigoPedido, decimal total) =>
            new()
            {
                Exitoso = true,
                IdPedido = idPedido,
                CodigoPedido = codigoPedido,
                Total = total,
                Mensaje = "Pedido registrado correctamente."
            };

        public static CheckoutResult Error(string mensaje) =>
            new() { Exitoso = false, Mensaje = mensaje };
    }
}
