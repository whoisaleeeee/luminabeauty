using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using LuminaBeauty.Models;

namespace LuminaBeauty.Services
{
    public class CartService
    {
        private const string CartStorageKey = "lumina_cart_items";
        private const string CouponStorageKey = "lumina_cart_coupon";

        private readonly LocalStorageService _storage;
        private List<CartItem> _items = new();
        private Cupon? _cuponAplicado;

        public event Action? OnChange;

        public CartService(LocalStorageService storage)
        {
            _storage = storage;
        }

        public async Task InitializeAsync()
        {
            var savedItems = await _storage.GetItemAsync<List<CartItem>>(CartStorageKey);
            var savedCoupon = await _storage.GetItemAsync<Cupon>(CouponStorageKey);

            if (savedItems != null)
            {
                _items = savedItems;
            }

            _cuponAplicado = savedCoupon;
            OnChange?.Invoke();
        }

        public List<CartItem> GetItems() => _items;

        public Cupon? GetAppliedCoupon() => _cuponAplicado;

        public async Task SetAppliedCouponAsync(Cupon cupon)
        {
            _cuponAplicado = cupon;
            await _storage.SetItemAsync(CouponStorageKey, cupon);
            OnChange?.Invoke();
        }

        public async Task RemoveAppliedCouponAsync()
        {
            _cuponAplicado = null;
            await _storage.RemoveItemAsync(CouponStorageKey);
            OnChange?.Invoke();
        }

        public async Task AddToCartAsync(Product product, int quantity = 1)
        {
            var existing = _items.FirstOrDefault(i => i.Product.Id == product.Id);

            if (existing != null)
            {
                existing.Quantity += quantity;
            }
            else
            {
                _items.Add(new CartItem
                {
                    Product = product,
                    Quantity = quantity
                });
            }

            await SaveCartAsync();
        }

        public async Task UpdateQuantityAsync(string id, int quantity)
        {
            var existing = _items.FirstOrDefault(i => i.Product.Id == id);

            if (existing != null)
            {
                existing.Quantity = Math.Max(1, quantity);
                await SaveCartAsync();
            }
        }

        public async Task RemoveItemAsync(string id)
        {
            _items.RemoveAll(i => i.Product.Id == id);
            await SaveCartAsync();
        }

        public async Task ClearCartAsync()
        {
            _items.Clear();
            _cuponAplicado = null;

            await _storage.SetItemAsync(CartStorageKey, _items);
            await _storage.RemoveItemAsync(CouponStorageKey);

            OnChange?.Invoke();
        }

        public int GetCount() => _items.Sum(i => i.Quantity);

        public decimal GetSubtotal()
        {
            return _items.Sum(i => i.Product.Price * i.Quantity);
        }

        private async Task SaveCartAsync()
        {
            await _storage.SetItemAsync(CartStorageKey, _items);
            OnChange?.Invoke();
        }
    }
}