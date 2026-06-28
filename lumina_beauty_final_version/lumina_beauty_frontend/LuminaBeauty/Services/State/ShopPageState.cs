using LuminaBeauty.Models;

namespace LuminaBeauty.Services.PageStates
{
    public class ShopPageState
    {
        public List<Product> AllProducts { get; set; } = [];

        public List<CatalogCategoryItem> Categories { get; set; } = [];

        public List<string> AvailableBrands { get; set; } = [];

        public HashSet<string> SelectedBrands { get; set; } =
            new(StringComparer.OrdinalIgnoreCase);

        public string? ActiveCategory { get; set; }

        public decimal MaximumPrice { get; set; }

        public decimal SelectedMaximumPrice { get; set; }

        public double? MinimumRating { get; set; }

        public bool OnlyInStock { get; set; }

        public string SortBy { get; set; } = "relevance";

        public int CurrentPage { get; set; } = 1;

        public int PageSize { get; set; } = 21;

        public bool IsReady { get; set; }
    }

    public class CatalogCategoryItem
    {
        public string Name { get; set; } = string.Empty;

        public int ProductCount { get; set; }
    }
}