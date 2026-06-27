using LuminaBeauty.Models;

namespace LuminaBeauty.Services
{
    public class HeaderSearchAppService
    {
        private readonly ProductService _productService;

        public List<Product> Results { get; private set; } = [];

        public bool IsOpen { get; private set; }

        public bool HasQuery => !string.IsNullOrWhiteSpace(Query);

        public string Query { get; private set; } = string.Empty;

        public HeaderSearchAppService(ProductService productService)
        {
            _productService = productService;
        }

        public async Task SearchAsync(string query)
        {
            Query = query?.Trim() ?? string.Empty;

            if (string.IsNullOrWhiteSpace(Query))
            {
                Results = [];
                IsOpen = false;
                return;
            }

            await _productService.EnsureLoadedAsync();

            Results = _productService
                .SearchProducts(Query)
                .Take(5)
                .ToList();

            IsOpen = true;
        }

        public void Close()
        {
            IsOpen = false;
        }

        public void Clear()
        {
            Query = string.Empty;
            Results = [];
            IsOpen = false;
        }
    }
}