using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using LuminaBeauty.Models;
using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Services.AppServices
{
    public class CheckoutAppService
    {
        private readonly PedidoAppService _pedidoAppService;
        private readonly CarroAppService _carroAppService;
        private readonly CartService _cartService;

        public CheckoutAppService(PedidoAppService pedidoAppService, CarroAppService carroAppService, CartService cartService)
        {
            _pedidoAppService = pedidoAppService;
            _carroAppService = carroAppService;
            _cartService = cartService;
        }

        public async Task<CheckoutResult> ProcesarPedidoAsync(Cliente? cliente, IReadOnlyList<CartItem> items)
        {
            if (cliente == null || cliente.Id <= 0)
                return CheckoutResult.Error("Debes iniciar sesión antes de confirmar tu pedido.");

            if (items == null || items.Count == 0)
                return CheckoutResult.Error("Tu carrito está vacío.");

            if (items.Any(item => item.Product.IdProducto <= 0))
                return CheckoutResult.Error("Uno o más productos no tienen un identificador válido para registrar el pedido.");

            foreach (var item in items)
            {
                var hayStock = await _carroAppService.ValidarStockAsync(item.Product.IdProducto, item.Quantity);

                if (!hayStock)
                    return CheckoutResult.Error($"No hay stock suficiente para: {item.Product.Name}.");
            }

            decimal subtotal = items.Sum(item => item.Product.Price * item.Quantity);
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
                return CheckoutResult.Error("No se pudo registrar el pedido.");

            foreach (var item in items)
            {
                var descontado = await _carroAppService.DescontarStockAsync(item.Product.IdProducto, item.Quantity);

                if (!descontado)
                    Console.WriteLine($"Advertencia: no se pudo descontar stock de {item.Product.Name}.");
            }

            await _cartService.ClearCartAsync();

            return CheckoutResult.Success(pedidoCreado.IdPedido, pedidoCreado.CodigoPedido, total);
        }
    }

    public class CheckoutResult
    {
        public bool Exitoso { get; private set; }
        public string Mensaje { get; private set; } = string.Empty;
        public int IdPedido { get; private set; }
        public string CodigoPedido { get; private set; } = string.Empty;
        public decimal Total { get; private set; }

        public static CheckoutResult Success(int idPedido, string codigoPedido, decimal total)
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