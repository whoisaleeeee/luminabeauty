using System;
using System.Collections.Generic;
using System.Linq;
using LuminaBeauty.Models;
using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services
{
    /// <summary>
    /// Servicio de aplicación que orquesta los datos de productos para el frontend Blazor.
    /// Internamente usa ProductoRestService para llamar al backend Java via REST.
    /// </summary>
    public class ProductService
    {
        private readonly ProductoRestService _productoRestService;

        public ProductService(ProductoRestService productoRestService)
        {
            _productoRestService = productoRestService;
        }

        // ── Metadata extra para enriquecer la UI (no va en BD) ───────────────
        private static readonly Dictionary<string, (decimal originalPrice, string? discount, string? badge, string usage, List<string> ingredients)> ProductMetadata =
            new(StringComparer.OrdinalIgnoreCase)
        {
            ["fs-1"] = (61.90m, "-40%", "SALE", "Aplicar sobre los labios limpios y secos de forma uniforme. Esperar 30 segundos a que se fije.", new() { "Paraffinum Liquidum", "Ozokerite", "Cera Microcristallina", "Mentha Piperita Oil" }),
            ["fs-2"] = (116.00m, "-35%", "SALE", "Deslizar directamente sobre los labios. Puede usarse solo o sobre tu labial favorito.", new() { "Ricinus Communis Seed Oil", "Candelilla Cera", "Shea Butter", "Tocopheryl Acetate" }),
            ["fs-3"] = (140.20m, "-30%", "SALE", "Aplicar el sérum sobre el rostro limpio, seguido de las cremas de día y noche con masajes circulares.", new() { "Aqua", "Glycerin", "Sodium Hyaluronate", "Phenoxyethanol", "Adenosine" }),
            ["fs-4"] = (40.00m, "-25%", "SALE", "Rociar en puntos de pulso como muñecas, cuello y detrás de las orejas a una distancia de 15 cm.", new() { "Alcohol Denat.", "Parfum", "Limonene", "Linalool", "Benzotriazolyl Dodecyl P-Cresol" }),
            ["bs-1"] = (35.00m, null, null, "Deslizar el cepillo desde la raíz de las pestañas hacia las puntas en movimientos zigzag suave.", new() { "Aqua", "Paraffin", "Potassium Cetyl Phosphate", "Cera Alba", "Copernicia Cerifera Cera" }),
            ["bs-2"] = (15.00m, null, null, "Aplicar una cantidad mínima con un pincel angular para cejas, difuminar con el cepillo de espiral para suavizar.", new() { "Cyclopentasiloxane", "Trimethylsiloxysilicate", "Cyclohexasiloxane", "Silica" }),
            ["bs-3"] = (170.00m, null, null, "Deslizar sobre las mejillas y difuminar suavemente con las yemas de los dedos o brocha.", new() { "Caprylic/Capric Triglyceride", "Aloe Barbadensis Leaf Extract", "Ginseng Root Extract" }),
            ["chubby-stick"] = (11.20m, null, null, "Girar la base para extraer el producto y deslizar libremente en tus labios cuando sientas sequedad.", new() { "Mango Seed Butter", "Shea Butter", "Castor Seed Oil", "Olive Fruit Oil" }),
            ["na-1"] = (480.00m, null, null, "Aplicar en zonas deseadas.", new() { "Alcohol", "Parfum", "Water" }),
            ["na-2"] = (240.00m, null, null, "Aplicar suavemente sobre el rostro.", new() { "Mica", "Talc", "Silica" })
        };

        private static readonly Dictionary<string, List<string>> CategorySubcategories =
            new(StringComparer.OrdinalIgnoreCase)
        {
            ["Makeup"]        = new() { "Lips", "Eyes", "Face", "Brushes" },
            ["Skincare"]      = new() { "Cleansers", "Toners", "Serums", "Moisturizers" },
            ["Fragrance"]     = new() { "Eau de Parfum", "Eau de Toilette", "Body Mist" },
            ["Hair"]          = new() { "Shampoo", "Conditioner", "Treatments" },
            ["Tools & Brushes"] = new() { "Makeup Brushes", "Sponges", "Lash Curlers" },
            ["Bath & Body"]   = new() { "Body Wash", "Lotions", "Scrubs" },
            ["Mini Size"]     = new() { "Travel Essentials", "Mini Kits" },
            ["Brands"]        = new() { "All Luxury Brands", "New Brands" },
            ["New"]           = new() { "This Week", "Trending Secrets" }
        };

        // ── Mapeo: DTO backend → modelo frontend ────────────────────────────
        private Product MapToFrontendProduct(Producto bp)
        {
            ProductMetadata.TryGetValue(bp.Id, out var meta);

            return new Product
            {
                Id           = bp.Id,
                Name         = bp.Nombre,
                Brand        = bp.Marca?.Nombre ?? "Lumina",
                Category     = bp.Categoria?.Nombre ?? "Skincare",
                Price        = bp.Precio,
                OriginalPrice = meta.originalPrice > 0 ? meta.originalPrice : (bp.Precio * 1.3m),
                Image        = bp.Imagen,
                Rating       = bp.Id.StartsWith("fs-") || bp.Id.StartsWith("bs-") ? 4.8 : 4.5,
                ReviewsCount = bp.Id.StartsWith("fs-") ? 88 : 65,
                Discount     = meta.discount,
                DiscountBadge = meta.badge,
                Stock        = bp.Stock,
                Description  = bp.Descripcion,
                Ingredients  = meta.ingredients ?? new List<string>(),
                Usage        = meta.usage
            };
        }

        // ── Métodos de consulta ───────────────────────────────────────────────

        public List<Product> GetAllProducts()
        {
            try
            {
                var list = _productoRestService.ListarProductosTodos();
                return list.Select(MapToFrontendProduct).ToList();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error en ProductService.GetAllProducts: " + ex.Message);
            }
            return new List<Product>();
        }

        public Product? GetProductById(string id)
        {
            try
            {
                var bp = _productoRestService.BuscarProductoPorId(id);
                if (bp != null) return MapToFrontendProduct(bp);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error en ProductService.GetProductById({id}): " + ex.Message);
            }
            return GetAllProducts().FirstOrDefault(p => p.Id == id);
        }

        public List<Product> GetFlashSaleProducts() =>
            GetAllProducts().Where(p => p.Id.StartsWith("fs-") || p.Discount != null).ToList();

        public List<Product> GetBestSellers() =>
            GetAllProducts().Where(p => p.Id.StartsWith("bs-") || p.Id.Equals("chubby-stick", StringComparison.OrdinalIgnoreCase)).ToList();

        public List<Product> GetNewArrivals() =>
            GetAllProducts().Where(p => p.Id.StartsWith("na-")).ToList();

        public List<Category> GetCategories()
        {
            try
            {
                var list = _productoRestService.ListarCategorias();
                return list.Select(bc => new Category
                {
                    Name = bc.Nombre,
                    Subcategories = CategorySubcategories.TryGetValue(bc.Nombre, out var sub)
                        ? sub : new List<string>()
                }).ToList();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error en ProductService.GetCategories: " + ex.Message);
            }
            return new List<Category>();
        }

        public List<Brand> GetBrands()
        {
            try
            {
                var list = _productoRestService.ListarMarcas();
                return list.Select(bm => new Brand
                {
                    Name = bm.Nombre,
                    Logo = string.IsNullOrEmpty(bm.Logo) ? bm.Nombre.ToUpper() : bm.Logo
                }).ToList();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error en ProductService.GetBrands: " + ex.Message);
            }
            return new List<Brand>();
        }
    }
}
