using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace LuminaBeauty.Services
{
    public class WishlistService
    {
        private readonly LocalStorageService _storage;
        private List<int> _wishlist = new();

        public event Action? OnChange;

        public WishlistService(LocalStorageService storage)
        {
            _storage = storage;
        }

        public async Task InitializeAsync()
        {
            var saved = await _storage.GetItemAsync<List<int>>("lumina_wishlist");
            if (saved != null)
            {
                _wishlist = saved;
            }
            OnChange?.Invoke();
        }

    
        public List<int> GetWishlist() => _wishlist;

        public bool IsInWishlist(int productId) => _wishlist.Contains(productId);

        public bool IsInWishlist(string productId)
        {
            return int.TryParse(productId, out var id) && IsInWishlist(id);
        }

        public async Task ToggleWishlistAsync(int productId)
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

        public async Task ToggleWishlistAsync(string productId)
        {
            if (int.TryParse(productId, out var id))
            {
                await ToggleWishlistAsync(id);
            }
        }

        private async Task SaveAsync()
        {
            await _storage.SetItemAsync("lumina_wishlist", _wishlist);
            OnChange?.Invoke();
        }
    }
}
