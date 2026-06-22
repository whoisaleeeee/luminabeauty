using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using LuminaBeauty.Models;

namespace LuminaBeauty.Services
{
    public class CartService
    {
        private readonly LocalStorageService _storage;
        private readonly HttpClient _httpClient;
        private readonly string _javaUrl = "LuminaBeauty-Servicios/webresources/CarroRS";
        private List<CartItem> _items = new();

        public event Action? OnChange;

        public CartService(LocalStorageService storage, HttpClient httpClient)
        {
            _storage = storage;
            _httpClient = httpClient;
        }

        public async Task InitializeAsync()
        {
            var saved = await _storage.GetItemAsync<List<CartItem>>("lumina_cart_items");
            if (saved != null)
            {
                _items = saved;
            }
            OnChange?.Invoke();
        }

        public List<CartItem> GetItems() => _items;

        public async Task AddToCartAsync(Product product, int quantity = 1)
        {
            var existing = _items.FirstOrDefault(i => i.Product.Id == product.Id);
            if (existing != null)
            {
                existing.Quantity += quantity;
            }
            else
            {
                _items.Add(new CartItem { Product = product, Quantity = quantity });
            }

            await SaveAsync();

            try
            {
                var requestBody = new
                {
                    carro = new { idCarro = 1 },
                    producto = new { idProducto = product.Id, nombre = product.Name, precio = product.Price },
                    cantidad = quantity
                };

                await _httpClient.PostAsJsonAsync($"{_javaUrl}/agregarProducto", requestBody);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error al registrar producto en el carrito de Java: {ex.Message}");
            }
        }

        public async Task UpdateQuantityAsync(int id, int quantity)
        {
            var existing = _items.FirstOrDefault(i => i.Product.Id == id);
            if (existing != null)
            {
                existing.Quantity = Math.Max(1, quantity);
                await SaveAsync();
            }
        }

        public async Task RemoveItemAsync(int id)
        {
            _items.RemoveAll(i => i.Product.Id == id);
            await SaveAsync();
        }

        public async Task ClearCartAsync()
        {
            _items.Clear();
            await SaveAsync();
        }

        public int GetCount() => _items.Sum(i => i.Quantity);

        public decimal GetSubtotal() => _items.Sum(i => i.Product.Price * i.Quantity);

        private async Task SaveAsync()
        {
            await _storage.SetItemAsync("lumina_cart_items", _items);
            OnChange?.Invoke();
        }
    }
}
