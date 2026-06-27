using System;
using System.Collections.Generic;
using System.Linq;
using LuminaBeauty.Models;
using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    public class ProductService
    {
        private readonly ProductoRestService _productoRestService;
        private readonly SemaphoreSlim _loadLock = new(1, 1);

        private bool _loaded;
        private List<Product> _products = new();
        private List<Category> _categories = new();
        private List<Brand> _brands = new();

        public ProductService(ProductoRestService productoRestService)
        {
            _productoRestService = productoRestService;
        }

        private static readonly Dictionary<string, (
            decimal originalPrice,
            string? discount,
            string? badge,
            string usage,
            List<string> ingredients
        )> ProductMetadata = new(StringComparer.OrdinalIgnoreCase)
        {
            ["fs-1"] = (
                61.90m,
                "-40%",
                "SALE",
                "Aplicar sobre los labios limpios y secos de forma uniforme.",
                new() { "Paraffinum Liquidum", "Ozokerite", "Cera Microcristallina" }
            ),
            ["fs-2"] = (
                116.00m,
                "-35%",
                "SALE",
                "Deslizar directamente sobre los labios.",
                new() { "Ricinus Communis Seed Oil", "Shea Butter", "Tocopheryl Acetate" }
            ),
            ["fs-3"] = (
                140.20m,
                "-30%",
                "SALE",
                "Aplicar el sérum sobre el rostro limpio.",
                new() { "Aqua", "Glycerin", "Sodium Hyaluronate" }
            ),
            ["fs-4"] = (
                40.00m,
                "-25%",
                "SALE",
                "Rociar en puntos de pulso.",
                new() { "Alcohol Denat.", "Parfum", "Limonene" }
            ),
            ["bs-1"] = (
                35.00m,
                null,
                null,
                "Aplicar desde la raíz de las pestañas hacia las puntas.",
                new() { "Aqua", "Paraffin", "Cera Alba" }
            ),
            ["bs-2"] = (
                15.00m,
                null,
                null,
                "Aplicar con pincel angular para cejas.",
                new() { "Cyclopentasiloxane", "Silica" }
            ),
            ["bs-3"] = (
                170.00m,
                null,
                null,
                "Deslizar sobre las mejillas y difuminar.",
                new() { "Caprylic/Capric Triglyceride", "Aloe Barbadensis Leaf Extract" }
            ),
            ["chubby-stick"] = (
                11.20m,
                null,
                null,
                "Deslizar en los labios cuando sientas sequedad.",
                new() { "Mango Seed Butter", "Shea Butter", "Castor Seed Oil" }
            ),
            ["na-1"] = (
                480.00m,
                null,
                null,
                "Aplicar en zonas deseadas.",
                new() { "Alcohol", "Parfum", "Water" }
            ),
            ["na-2"] = (
                240.00m,
                null,
                null,
                "Aplicar suavemente sobre el rostro.",
                new() { "Mica", "Talc", "Silica" }
            )
        };

        private static readonly Dictionary<string, List<string>> CategorySubcategories =
            new(StringComparer.OrdinalIgnoreCase)
            {
                ["Makeup"] = new() { "Lips", "Eyes", "Face", "Brushes" },
                ["Skincare"] = new() { "Cleansers", "Toners", "Serums", "Moisturizers" },
                ["Fragrance"] = new() { "Eau de Parfum", "Eau de Toilette", "Body Mist" },
                ["Hair"] = new() { "Shampoo", "Conditioner", "Treatments" },
                ["Tools & Brushes"] = new() { "Makeup Brushes", "Sponges", "Lash Curlers" },
                ["Bath & Body"] = new() { "Body Wash", "Lotions", "Scrubs" },
                ["Mini Size"] = new() { "Travel Essentials", "Mini Kits" },
                ["Brands"] = new() { "All Luxury Brands", "New Brands" },
                ["New"] = new() { "This Week", "Trending Secrets" }
            };

        private Product MapToFrontendProduct(
            Producto bp,
            IReadOnlyDictionary<int, CategoriaProducto> categorias,
            IReadOnlyDictionary<int, Marca> marcas)
        {
            string id = !string.IsNullOrWhiteSpace(bp.Id)
                ? bp.Id
                : bp.IdProducto.ToString();

            ProductMetadata.TryGetValue(id, out var meta);

            CategoriaProducto? categoria = bp.Categoria;
            Marca? marca = bp.Marca;

            if (categoria?.IdCategoria > 0 &&
                categorias.TryGetValue(categoria.IdCategoria, out var categoriaCompleta))
            {
                categoria = categoriaCompleta;
            }

            if (marca?.IdMarca > 0 &&
                marcas.TryGetValue(marca.IdMarca, out var marcaCompleta))
            {
                marca = marcaCompleta;
            }

            return new Product
            {
                Id = id,
                IdProducto = bp.IdProducto,
                Name = bp.Nombre ?? string.Empty,
                Brand = marca?.Nombre ?? "Lumina",
                Category = categoria?.Nombre ?? "Sin categoría",
                Price = bp.Precio,
                OriginalPrice = meta.originalPrice > 0
                    ? meta.originalPrice
                    : Math.Round(bp.Precio * 1.3m, 2),
                Image = ResolveProductImage(bp.Imagen),
                Rating = bp.PromedioCalificacion,
                ReviewsCount = bp.CantidadValoraciones,
                Discount = meta.discount,
                DiscountBadge = meta.badge,
                Stock = bp.Stock,
                Description = bp.Descripcion ?? string.Empty,
                Ingredients = meta.ingredients ?? new List<string>(),
                Usage = meta.usage ?? "Aplicar según indicación del producto."
            };
        }

        private string ResolveProductImage(string imageUrl)
        {
            if (string.IsNullOrWhiteSpace(imageUrl))
            {
                return string.Empty;
            }

            if (Uri.TryCreate(imageUrl, UriKind.Absolute, out _))
            {
                return imageUrl;
            }

            var baseAddress = _productoRestService.BaseAddress;

            if (baseAddress == null)
            {
                return imageUrl;
            }

            return new Uri(baseAddress, imageUrl.TrimStart('/')).ToString();
        }

        public async Task EnsureLoadedAsync()
        {
            if (_loaded)
            {
                return;
            }

            await _loadLock.WaitAsync();

            try
            {
                if (_loaded)
                {
                    return;
                }

                await ReloadAsync();
                _loaded = true;
            }
            finally
            {
                _loadLock.Release();
            }
        }

        public async Task ReloadAsync()
        {
            var productosTask = LoadListAsync(
                () => _productoRestService.ListarProductosTodosAsync(),
                "ListarProductosTodosAsync"
            );

            var categoriasTask = LoadListAsync(
                () => _productoRestService.ListarCategoriasAsync(),
                "ListarCategoriasAsync"
            );

            var marcasTask = LoadListAsync(
                () => _productoRestService.ListarMarcasAsync(),
                "ListarMarcasAsync"
            );

            await Task.WhenAll(productosTask, categoriasTask, marcasTask);

            var productos = await productosTask;
            var categorias = await categoriasTask;
            var marcas = await marcasTask;

            var categoriasPorId = categorias
                .Where(c => c.IdCategoria > 0)
                .GroupBy(c => c.IdCategoria)
                .ToDictionary(g => g.Key, g => g.First());

            var marcasPorId = marcas
                .Where(m => m.IdMarca > 0)
                .GroupBy(m => m.IdMarca)
                .ToDictionary(g => g.Key, g => g.First());

            _products = productos
                .Select(p => MapToFrontendProduct(p, categoriasPorId, marcasPorId))
                .ToList();

            _categories = categorias
                .Select(c => new Category
                {
                    Name = c.Nombre ?? string.Empty,
                    Subcategories = CategorySubcategories.TryGetValue(
                        c.Nombre ?? string.Empty,
                        out var sub
                    )
                        ? sub
                        : new List<string>()
                })
                .ToList();

            _brands = marcas
                .Select(m => new Brand
                {
                    Name = m.Nombre ?? string.Empty,
                    Logo = string.IsNullOrEmpty(m.Logo)
                        ? (m.Nombre ?? string.Empty).ToUpper()
                        : m.Logo
                })
                .ToList();
        }

        private static async Task<List<T>> LoadListAsync<T>(
            Func<Task<List<T>>> load,
            string operation)
        {
            try
            {
                return await load();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error en ProductService.{operation}: {ex.Message}");
                return new List<T>();
            }
        }

        public List<Product> GetAllProducts()
        {
            return _products.ToList();
        }

        public Product? GetProductById(string id)
        {
            return _products.FirstOrDefault(
                p => p.Id.Equals(id, StringComparison.OrdinalIgnoreCase)
            );
        }

        public List<Product> GetFlashSaleProducts()
        {
            var products = _products
                .Where(p =>
                    p.Id.StartsWith("fs-", StringComparison.OrdinalIgnoreCase) ||
                    p.Discount != null
                )
                .ToList();

            return products.Count > 0
                ? products
                : _products.Take(4).ToList();
        }

        public List<Product> GetBestSellers()
        {
            var products = _products
                .Where(p =>
                    p.Id.StartsWith("bs-", StringComparison.OrdinalIgnoreCase) ||
                    p.Id.Equals("chubby-stick", StringComparison.OrdinalIgnoreCase)
                )
                .ToList();

            return products.Count > 0
                ? products
                : _products.Skip(4).Take(4).ToList();
        }

        public List<Product> GetNewArrivals()
        {
            var products = _products
                .Where(p => p.Id.StartsWith("na-", StringComparison.OrdinalIgnoreCase))
                .ToList();

            return products.Count > 0
                ? products
                : _products.OrderByDescending(p => p.Id).Take(4).ToList();
        }

        public List<Product> GetProductsByCategory(string category)
        {
            return _products
                .Where(p => p.Category.Equals(category, StringComparison.OrdinalIgnoreCase))
                .ToList();
        }

        public List<Product> SearchProducts(string texto)
        {
            if (string.IsNullOrWhiteSpace(texto))
            {
                return GetAllProducts();
            }

            texto = texto.Trim().ToLower();

            return _products
                .Where(p =>
                    p.Name.ToLower().Contains(texto) ||
                    p.Brand.ToLower().Contains(texto) ||
                    p.Category.ToLower().Contains(texto) ||
                    (p.Description ?? string.Empty).ToLower().Contains(texto)
                )
                .ToList();
        }

        public List<Category> GetCategories()
        {
            return _categories.ToList();
        }

        public List<Brand> GetBrands()
        {
            return _brands.ToList();
        }
    }
}