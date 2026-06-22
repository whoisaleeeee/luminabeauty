using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using LuminaBeauty.Models;
using Microsoft.AspNetCore.Components;

namespace LuminaBeauty.Services
{
    public class HomeService : IDisposable
    {
        private readonly ProductService _productService;
        private readonly LayoutStateService _layoutState;
        private readonly NavigationManager _navigationManager;

        private readonly SynchronizationContext? _syncContext = SynchronizationContext.Current;

        private Timer? _timer;
        private DateTime _targetTime = DateTime.Now.AddDays(2).AddHours(5).AddMinutes(12).AddSeconds(4);

        public string ActiveCategory { get; private set; } = string.Empty;
        public string ActiveBrand { get; private set; } = string.Empty;
        public TimeSpan TimeLeft => _targetTime > DateTime.Now ? _targetTime - DateTime.Now : TimeSpan.Zero;

        public event Action? OnChange;

        public HomeService(ProductService productService, LayoutStateService layoutState, NavigationManager navigationManager)
        {
            _productService = productService;
            _layoutState = layoutState;
            _navigationManager = navigationManager;
        }

        public void OnMobileSearch(string query)
        {
            _layoutState.SetSearchQuery(query);
            _navigationManager.NavigateTo("#catalog");
            NotifyStateChanged();
        }

        public void FilterByBrand(string brand)
        {
            ActiveBrand = brand;
            ActiveCategory = string.Empty;
            _navigationManager.NavigateTo("#catalog");
            NotifyStateChanged();
        }

        public void ResetCatalogue()
        {
            ActiveCategory = string.Empty;
            ActiveBrand = string.Empty;
            _layoutState.SetSearchQuery(string.Empty);
            NotifyStateChanged();
        }

        public void SelectCategory(string category)
        {
            ActiveCategory = category;
            ActiveBrand = string.Empty;
            NotifyStateChanged();
        }

        public List<Product> GetFilteredCatalogue()
        {
            var products = _productService.GetAllProducts();

            // Filter by search query if any
            if (!string.IsNullOrEmpty(_layoutState.SearchQuery))
            {
                products = products.Where(p =>
                    p.Name.Contains(_layoutState.SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                    p.Brand.Contains(_layoutState.SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                    p.Category.Contains(_layoutState.SearchQuery, StringComparison.OrdinalIgnoreCase)
                ).ToList();
            }

            // Filter by active category
            if (!string.IsNullOrEmpty(ActiveCategory))
            {
                products = products.Where(p => p.Category.Equals(ActiveCategory, StringComparison.OrdinalIgnoreCase)).ToList();
            }

            // Filter by active brand
            if (!string.IsNullOrEmpty(ActiveBrand))
            {
                products = products.Where(p => p.Brand.Equals(ActiveBrand, StringComparison.OrdinalIgnoreCase)).ToList();
            }

            return products;
        }

        public void OpenConsultation()
        {
            _layoutState.SetAdvisorOpen(true);
        }

        public void StartCountdown(Action onTick)
        {
            _timer?.Dispose();
            _timer = new Timer(_ =>
            {
                onTick();
                NotifyStateChanged();
            }, null, 0, 1000);
        }

        // 2. Modificamos el método para redirigir la invocación de forma segura al hilo principal
        private void NotifyStateChanged()
        {
            if (_syncContext != null)
            {
                // Si estamos en el entorno web de Blazor, enviamos el evento al hilo principal de la UI
                _syncContext.Post(_ => OnChange?.Invoke(), null);
            }
            else
            {
                // Respaldo por si no se detecta el contexto visual
                OnChange?.Invoke();
            }
        }

        public void Dispose()
        {
            _timer?.Dispose();
        }
    }
}