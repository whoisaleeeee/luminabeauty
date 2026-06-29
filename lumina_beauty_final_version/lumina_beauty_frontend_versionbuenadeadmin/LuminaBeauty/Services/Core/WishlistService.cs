using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace LuminaBeauty.Services
{
    public class WishlistService
    {
        private readonly LocalStorageService _storage;
        private List<string> _wishlist = new();

        public event Action? OnChange;

        public WishlistService(LocalStorageService storage)
        {
            _storage = storage;
        }

        public async Task InitializeAsync()
        {
            var saved = await _storage.GetItemAsync<List<string>>("lumina_wishlist");
            if (saved != null)
            {
                _wishlist = saved;
            }
            OnChange?.Invoke();
        }

        public List<string> GetWishlist() => _wishlist;

        public bool IsInWishlist(string productId) => _wishlist.Contains(productId);

        public async Task ToggleWishlistAsync(string productId)
        {
            if (_wishlist.Contains(productId))
            {
                _wishlist.Remove(productId);
            }
            else
            {
                _wishlist.Add(productId);
            }
            await SaveAsync();
        }

        private async Task SaveAsync()
        {
            await _storage.SetItemAsync("lumina_wishlist", _wishlist);
            OnChange?.Invoke();
        }
    }
}
