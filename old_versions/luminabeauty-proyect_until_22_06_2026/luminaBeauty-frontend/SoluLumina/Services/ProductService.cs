using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading;
using System.Threading.Tasks;
using LuminaBeauty.Data;
using LuminaBeauty.Models;

namespace LuminaBeauty.Services
{
    public class ProductService
    {
        private const string ProductoEndpoint = "LuminaBeauty-Servicios/webresources/ProductoRS";
        private readonly HttpClient _httpClient;
        private List<Product>? _products;

        public ProductService(HttpClient httpClient)
        {
            _httpClient = httpClient;
        }

        public List<Brand> GetBrands() => BeautyDb.Brands;

        public List<Category> GetCategories() => BeautyDb.Categories;

        public List<Product> GetAllProducts() => EnsureProducts();

        public List<Product> GetFlashSaleProducts() => EnsureProducts().Take(4).ToList();

        public List<Product> GetBestSellers() => EnsureProducts().Skip(4).Take(4).ToList();

        public List<Product> GetNewArrivals() => EnsureProducts().Skip(8).Take(4).ToList();

        public Product? GetProductById(string id)
        {
            return int.TryParse(id, out var numericId) ? GetProductById(numericId) : null;
        }

        public Product? GetProductById(int id)
        {
            return EnsureProducts().FirstOrDefault(p => p.Id == id);
        }

        public async Task<List<Product>> GetAllProductsAsync()
        {
            _products = await FetchProductsFromJavaAsync();
            return _products;
        }

        public async Task<Product?> GetProductByIdAsync(int id)
        {
            try
            {
                var producto = await _httpClient.GetFromJsonAsync<JavaProductoDto>($"{ProductoEndpoint}/{id}");
                return producto == null ? null : MapJavaProduct(producto);
            }
            catch
            {
                return GetProductById(id);
            }
        }

        public async Task<bool> ValidarStockAsync(int idProducto, int cantidad)
        {
            try
            {
                var resultado = await _httpClient.GetFromJsonAsync<int>($"{ProductoEndpoint}/validarStock/{idProducto}/{cantidad}");
                return resultado == 1;
            }
            catch
            {
                return false;
            }
        }

        public async Task<bool> DescontarStockAsync(int idProducto, int cantidad)
        {
            try
            {
                var response = await _httpClient.PutAsJsonAsync<object?>($"{ProductoEndpoint}/descontarStock/{idProducto}/{cantidad}", null);
                if (!response.IsSuccessStatusCode)
                {
                    return false;
                }

                var resultado = await response.Content.ReadFromJsonAsync<int>();
                return resultado == 1;
            }
            catch
            {
                return false;
            }
        }

        private List<Product> EnsureProducts()
        {
            if (_products != null)
            {
                return _products;
            }

            try
            {
                var loadTask = Task.Run(FetchProductsFromJavaAsync);
                _products = loadTask.Wait(TimeSpan.FromSeconds(3))
                    ? loadTask.Result
                    : BuildFallbackProducts();
            }
            catch
            {
                _products = BuildFallbackProducts();
            }

            if (_products.Count == 0)
            {
                _products = BuildFallbackProducts();
            }

            return _products;
        }

        private async Task<List<Product>> FetchProductsFromJavaAsync()
        {
            try
            {
                var productos = await _httpClient.GetFromJsonAsync<List<JavaProductoDto>>($"{ProductoEndpoint}/listar");
                return productos?
                    .Where(p => p.Estado is null or 1)
                    .Select(MapJavaProduct)
                    .ToList() ?? new List<Product>();
            }
            catch
            {
                return BuildFallbackProducts();
            }
        }

        private static Product MapJavaProduct(JavaProductoDto producto)
        {
            var category = producto.Categoria?.Nombre ?? "Beauty";
            var brand = producto.Marca?.Nombre ?? "LuminaBeauty";

            return new Product
            {
                Id = producto.Id,
                Name = producto.Nombre ?? $"Producto {producto.Id}",
                Brand = brand,
                Category = category,
                Price = producto.Precio,
                OriginalPrice = null,
                Image = string.IsNullOrWhiteSpace(producto.Imagen)
                    ? "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&w=700&q=80"
                    : producto.Imagen,
                Rating = 4.8,
                ReviewsCount = 0,
                Stock = producto.Stock,
                Description = producto.Descripcion,
                Ingredients = new List<string>(),
                Usage = producto.TipoPiel
            };
        }

        private static List<Product> BuildFallbackProducts()
        {
            return new List<Product>
            {
                new() { Id = 1, Name = "Serum Glow C", Brand = "Lumina", Category = "Skincare", Price = 89, Image = "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&w=700&q=80", Rating = 4.8, ReviewsCount = 24, Stock = 20 },
                new() { Id = 2, Name = "Crema Hydra Bloom", Brand = "Lumina", Category = "Skincare", Price = 74, Image = "https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=700&q=80", Rating = 4.7, ReviewsCount = 18, Stock = 14 },
                new() { Id = 3, Name = "Tint Labial Rosy", Brand = "Lumina", Category = "Makeup", Price = 39, Image = "https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=700&q=80", Rating = 4.6, ReviewsCount = 31, Stock = 40 },
                new() { Id = 4, Name = "Protector Solar Velvet SPF50", Brand = "Lumina", Category = "Skincare", Price = 65, Image = "https://images.unsplash.com/photo-1596755389378-c31d21fd1273?auto=format&fit=crop&w=700&q=80", Rating = 4.9, ReviewsCount = 42, Stock = 16 },
                new() { Id = 5, Name = "Mist Fresh Balance", Brand = "Lumina", Category = "Skincare", Price = 49, Image = "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?auto=format&fit=crop&w=700&q=80", Rating = 4.5, ReviewsCount = 12, Stock = 25 },
                new() { Id = 6, Name = "Mascara Lash Lift", Brand = "Lumina", Category = "Makeup", Price = 52, Image = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=700&q=80", Rating = 4.7, ReviewsCount = 20, Stock = 30 }
            };
        }

        private sealed class JavaProductoDto
        {
            public int Id { get; set; }
            public string? Nombre { get; set; }
            public string? Descripcion { get; set; }
            public decimal Precio { get; set; }
            public int Stock { get; set; }
            public string? TipoPiel { get; set; }
            public string? Imagen { get; set; }
            public int? Estado { get; set; }
            public JavaNamedDto? Categoria { get; set; }
            public JavaNamedDto? Marca { get; set; }
        }

        private sealed class JavaNamedDto
        {
            public int Id { get; set; }
            public string? Nombre { get; set; }
        }
    }
}
