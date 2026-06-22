using System;
using LuminaBeauty.Models;

namespace LuminaBeauty.Services
{
    public class LayoutStateService
    {
        private bool _isCartDrawerOpen;
        private bool _isWishlistDrawerOpen;
        private bool _isAdvisorOpen;
        private Product? _quickViewProduct;
        private string _searchQuery = string.Empty;

        public event Action? OnChange;

        public bool IsCartDrawerOpen => _isCartDrawerOpen;
        public bool IsWishlistDrawerOpen => _isWishlistDrawerOpen;
        public bool IsAdvisorOpen => _isAdvisorOpen;
        public Product? QuickViewProduct => _quickViewProduct;
        public string SearchQuery => _searchQuery;

        public void SetCartDrawerOpen(bool isOpen)
        {
            _isCartDrawerOpen = isOpen;
            NotifyStateChanged();
        }

        public void SetWishlistDrawerOpen(bool isOpen)
        {
            _isWishlistDrawerOpen = isOpen;
            NotifyStateChanged();
        }

        public void SetAdvisorOpen(bool isOpen)
        {
            _isAdvisorOpen = isOpen;
            NotifyStateChanged();
        }

        public void SetQuickViewProduct(Product? product)
        {
            _quickViewProduct = product;
            NotifyStateChanged();
        }

        public void SetSearchQuery(string query)
        {
            _searchQuery = query;
            NotifyStateChanged();
        }

        private void NotifyStateChanged() => OnChange?.Invoke();
    }
}
