using LuminaBeauty.Models;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Services.AppServices
{
    public class CheckoutAppService
    {
        private readonly PedidoAppService _pedidoAppService;
        private readonly CarroAppService _carroAppService;
        private readonly CartService _cartService;
        private readonly EnvioAppService _envioAppService;

        public CheckoutAppService(
            PedidoAppService pedidoAppService,
            CarroAppService carroAppService,
            CartService cartService,
            EnvioAppService envioAppService)
        {
            _pedidoAppService = pedidoAppService;
            _carroAppService = carroAppService;
            _cartService = cartService;
            _envioAppService = envioAppService;
        }

        public async Task<CheckoutResult> ProcesarPedidoAsync(
            Cliente? cliente,
            IReadOnlyList<CartItem> items,
            DatosDespacho datosDespacho)
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
                    "Completa los datos de entrega antes de confirmar el pedido.");
            }

            if (items.Any(item => item.Product.IdProducto <= 0))
            {
                return CheckoutResult.Error(
                    "Uno o más productos no tienen un identificador válido.");
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

            decimal costoEnvio = subtotal >= 199m ? 0m : 15m;
            decimal descuento = 0m;
            decimal total = subtotal + costoEnvio - descuento;

            var pedido = new Pedido
            {
                CodigoPedido = "PED-" + DateTime.Now.ToString("yyyyMMddHHmmss"),
                CodigoCuponAplicado = string.Empty,
                SubtotalProductos = subtotal,
                CostoEnvio = costoEnvio,
                Descuento = descuento,
                Total = total,
                Estado = "PENDIENTE",
                Cliente = new Cliente
                {
                    Id = cliente.Id
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

            var pedidoCreado = await _pedidoAppService.CrearPedidoAsync(pedido);

            if (pedidoCreado == null || pedidoCreado.IdPedido <= 0)
            {
                return CheckoutResult.Error("No se pudo registrar el pedido.");
            }

            var envio = new Envio
            {
                ZonaEnvio = ResolverZonaEnvio(datosDespacho.Ciudad),
                Estado = "PREPARANDO",
                DireccionEnvio = datosDespacho.Direccion.Trim(),
                CiudadEnvio = datosDespacho.Ciudad.Trim(),
                PaisEnvio = string.IsNullOrWhiteSpace(datosDespacho.Pais)
                    ? "Peru"
                    : datosDespacho.Pais.Trim(),
                ReferenciaEnvio = datosDespacho.Referencia.Trim(),
                CodigoPostalEnvio = datosDespacho.CodigoPostal.Trim(),
                Pedido = new Pedido
                {
                    IdPedido = pedidoCreado.IdPedido
                }
            };

            var envioCreado = await _envioAppService.RegistrarAsync(envio);

            if (envioCreado == null || envioCreado.IdEnvio <= 0)
            {
                return CheckoutResult.Error(
                    "El pedido fue registrado, pero no se pudo crear el envío. "
                    + "Comunícate con soporte antes de intentar realizar otra compra.");
            }

            foreach (var item in items)
            {
                var descontado = await _carroAppService.DescontarStockAsync(
                    item.Product.IdProducto,
                    item.Quantity);

                if (!descontado)
                {
                    Console.WriteLine(
                        $"Advertencia: no se pudo descontar stock de {item.Product.Name}.");
                }
            }

            await _cartService.ClearCartAsync();

            return CheckoutResult.Success(
                pedidoCreado.IdPedido,
                pedidoCreado.CodigoPedido,
                total);
        }

        private static string ResolverZonaEnvio(string ciudad)
        {
            return ciudad.Trim().Equals(
                "Lima",
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

        public bool EsValido()
        {
            return !string.IsNullOrWhiteSpace(NombreContacto)
                && !string.IsNullOrWhiteSpace(Telefono)
                && !string.IsNullOrWhiteSpace(Direccion)
                && !string.IsNullOrWhiteSpace(Ciudad)
                && !string.IsNullOrWhiteSpace(Referencia);
        }
    }

    public class CheckoutResult
    {
        public bool Exitoso { get; private set; }
        public string Mensaje { get; private set; } = string.Empty;
        public int IdPedido { get; private set; }
        public string CodigoPedido { get; private set; } = string.Empty;
        public decimal Total { get; private set; }

        public static CheckoutResult Success(
            int idPedido,
            string codigoPedido,
            decimal total)
        {
            return new CheckoutResult
            {
                Exitoso = true,
                IdPedido = idPedido,
                CodigoPedido = codigoPedido,
                Total = total,
                Mensaje = "Pedido registrado correctamente."
            };
        }

        public static CheckoutResult Error(string mensaje)
        {
            return new CheckoutResult
            {
                Exitoso = false,
                Mensaje = mensaje
            };
        }
    }
}