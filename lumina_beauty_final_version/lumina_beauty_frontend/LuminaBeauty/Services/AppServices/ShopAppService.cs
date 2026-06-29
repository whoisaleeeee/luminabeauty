using LuminaBeauty.Models;
using LuminaBeauty.Services.PageStates;

namespace LuminaBeauty.Services
{
    public class ShopAppService
    {
        private readonly ProductService _productService;

        public ShopPageState State { get; } = new();

        public ShopAppService(ProductService productService)
        {
            _productService = productService;
        }

        public async Task InitializeAsync(string? categoryName = null)
        {
            await _productService.EnsureLoadedAsync();

            State.AllProducts = _productService.GetAllProducts();

            var categoryNames = _productService.GetCategories()
                .Select(category => category.Name)
                .Where(name => !string.IsNullOrWhiteSpace(name))
                .Union(
                    State.AllProducts
                        .Select(product => product.Category)
                        .Where(name => !string.IsNullOrWhiteSpace(name)),
                    StringComparer.OrdinalIgnoreCase
                )
                .OrderBy(name => name)
                .ToList();

            State.Categories = categoryNames
                .Select(name => new CatalogCategoryItem
                {
                    Name = name,
                    ProductCount = State.AllProducts.Count(product =>
                        product.Category.Equals(
                            name,
                            StringComparison.OrdinalIgnoreCase
                        )
                    )
                })
                .ToList();

            State.ActiveCategory = ResolveCategory(categoryName);

            State.AvailableBrands = GetProductsForActiveCategory()
                .Select(product => product.Brand)
                .Where(brand => !string.IsNullOrWhiteSpace(brand))
                .Distinct(StringComparer.OrdinalIgnoreCase)
                .OrderBy(brand => brand)
                .ToList();

            State.MaximumPrice = State.AllProducts.Count == 0
                ? 0
                : State.AllProducts.Max(product => product.Price);

            State.SelectedMaximumPrice = State.MaximumPrice;
            State.SelectedBrands.Clear();
            State.MinimumRating = null;
            State.OnlyInStock = false;
            State.SortBy = "relevance";
            State.IsReady = true;
        }

        public List<Product> GetVisibleProducts()
        {
            var products = GetProductsForActiveCategory();

            if (State.SelectedBrands.Count > 0)
            {
                products = products
                    .Where(product => State.SelectedBrands.Contains(product.Brand))
                    .ToList();
            }

            products = products
                .Where(product => product.Price <= State.SelectedMaximumPrice)
                .ToList();

            if (State.MinimumRating.HasValue)
            {
                products = products
                    .Where(product => product.Rating >= State.MinimumRating.Value)
                    .ToList();
            }

            if (State.OnlyInStock)
            {
                products = products
                    .Where(product => product.Stock > 0)
                    .ToList();
            }

            return State.SortBy switch
            {
                "best_sellers" => products
                    .OrderByDescending(product => product.ReviewsCount)
                    .ThenByDescending(product => product.Rating)
                    .ThenBy(product => product.Name)
                    .ToList(),

                "new_arrivals" => products
                    .OrderByDescending(IsNewArrival)
                    .ThenByDescending(product => product.Id)
                    .ThenBy(product => product.Name)
                    .ToList(),

                "rating" => products
                    .OrderByDescending(product => product.Rating)
                    .ThenByDescending(product => product.ReviewsCount)
                    .ToList(),

                "price_asc" => products
                    .OrderBy(product => product.Price)
                    .ToList(),

                "price_desc" => products
                    .OrderByDescending(product => product.Price)
                    .ToList(),

                _ => products
                    .OrderByDescending(product => product.ReviewsCount)
                    .ThenByDescending(product => product.Rating)
                    .ToList()
            };
        }

        public void SelectCategory(string? categoryName)
        {
            State.CurrentPage = 1;

            State.ActiveCategory = ResolveCategory(categoryName);

            State.AvailableBrands = GetProductsForActiveCategory()
                .Select(product => product.Brand)
                .Where(brand => !string.IsNullOrWhiteSpace(brand))
                .Distinct(StringComparer.OrdinalIgnoreCase)
                .OrderBy(brand => brand)
                .ToList();

            ResetFilters();
        }

        public List<Product> GetPagedProducts()
        {
            var products = GetVisibleProducts();

            var totalPages = GetTotalPages();

            if (State.CurrentPage > totalPages)
            {
                State.CurrentPage = totalPages;
            }

            return products
                .Skip((State.CurrentPage - 1) * State.PageSize)
                .Take(State.PageSize)
                .ToList();
        }

        public int GetTotalPages()
        {
            var totalProducts = GetVisibleProducts().Count;

            return Math.Max(
                1,
                (int)Math.Ceiling(totalProducts / (double)State.PageSize)
            );
        }

        public void GoToPage(int page)
        {
            var totalPages = GetTotalPages();

            State.CurrentPage = Math.Clamp(page, 1, totalPages);
        }

        public void ToggleBrand(string brand)
        {
            State.CurrentPage = 1;

            if (!State.SelectedBrands.Add(brand))
            {
                State.SelectedBrands.Remove(brand);
            }
        }

        public void ToggleMinimumRating(double rating)
        {
            State.CurrentPage = 1;

            State.MinimumRating = State.MinimumRating == rating
                ? null
                : rating;
        }

        public void ResetFilters()
        {
            State.CurrentPage = 1;

            State.SelectedBrands.Clear();
            State.SelectedMaximumPrice = State.MaximumPrice;
            State.MinimumRating = null;
            State.OnlyInStock = false;
            State.SortBy = "relevance";
        }

        private List<Product> GetProductsForActiveCategory()
        {
            if (string.IsNullOrWhiteSpace(State.ActiveCategory))
            {
                return State.AllProducts.ToList();
            }

            return State.AllProducts
                .Where(product => product.Category.Equals(
                    State.ActiveCategory,
                    StringComparison.OrdinalIgnoreCase
                ))
                .ToList();
        }

        private static bool IsNewArrival(Product product)
        {
            return !string.IsNullOrWhiteSpace(product.Id) &&
                   product.Id.StartsWith(
                       "na",
                       StringComparison.OrdinalIgnoreCase
                   );
        }

        private string? ResolveCategory(string? categoryName)
        {
            if (string.IsNullOrWhiteSpace(categoryName))
            {
                return null;
            }

            var decodedName = Uri.UnescapeDataString(categoryName);

            return State.Categories
                .FirstOrDefault(category => category.Name.Equals(
                    decodedName,
                    StringComparison.OrdinalIgnoreCase
                ))
                ?.Name;
        }
    }
}
