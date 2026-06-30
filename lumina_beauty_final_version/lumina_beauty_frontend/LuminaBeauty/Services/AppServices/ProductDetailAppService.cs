using LuminaBeauty.Models;

namespace LuminaBeauty.Services.AppServices
{
    public class ProductDetailAppService
    {
        private readonly ProductService _productCache;
        private readonly ValoracionAppService _valoracionApp;
        private readonly CartService _cart;
        private readonly WishlistService _wishlist;
        private readonly LanguageService _language;
        private readonly ToastService _toast;
        private readonly LayoutStateService _layoutState;

        public Product? Product { get; private set; }

        public List<Product> Related { get; private set; } = [];

        public List<string> GalleryImages { get; private set; } = [];

        public List<Valoracion> Reviews { get; private set; } = [];

        public bool IsLoadingReviews { get; private set; }

        public ProductDetailAppService(
            ProductService productCache,
            ValoracionAppService valoracionApp,
            CartService cart,
            WishlistService wishlist,
            LanguageService language,
            ToastService toast,
            LayoutStateService layoutState)
        {
            _productCache = productCache;
            _valoracionApp = valoracionApp;
            _cart = cart;
            _wishlist = wishlist;
            _language = language;
            _toast = toast;
            _layoutState = layoutState;
        }

        public async Task LoadAsync(string id)
        {
            await _productCache.EnsureLoadedAsync();

            Product = _productCache.GetProductById(id);
            Related = [];
            Reviews = [];
            GalleryImages = GetProductImages();

            if (Product is null)
            {
                return;
            }

            await LoadReviewsAsync(Product.IdProducto);

            Related = _productCache.GetAllProducts()
                .Where(product =>
                    product.Id != Product.Id &&
                    product.Category == Product.Category)
                .Take(4)
                .ToList();
        }

        public async Task AddToCartAsync(int quantity)
        {
            if (Product is null)
            {
                return;
            }

            await _cart.AddToCartAsync(Product, quantity);

            _toast.ShowToast(
                _language.CurrentLanguage == "es"
                    ? $"Agregado a la bolsa: {Product.Name}"
                    : $"Added to bag: {Product.Name}"
            );

            _layoutState.SetCartDrawerOpen(true);
        }

        public async Task ToggleWishlistAsync()
        {
            if (Product is null)
            {
                return;
            }

            await _wishlist.ToggleWishlistAsync(Product.Id);

            var isSaved = _wishlist.IsInWishlist(Product.Id);

            _toast.ShowToast(
                isSaved
                    ? "Guardado en favoritos"
                    : "Removido de favoritos"
            );
        }

        private async Task LoadReviewsAsync(int idProducto)
        {
            try
            {
                IsLoadingReviews = true;

                Reviews = await _valoracionApp
                    .ListarPublicadasPorProductoAsync(idProducto);
            }
            catch
            {
                Reviews = [];
            }
            finally
            {
                IsLoadingReviews = false;
            }
        }

        private List<string> GetProductImages()
        {
            if (Product is null || string.IsNullOrWhiteSpace(Product.Image))
            {
                return [];
            }

            var imagePath = Product.Image;
            var extension = Path.GetExtension(imagePath);

            if (string.IsNullOrWhiteSpace(extension))
            {
                return [imagePath];
            }

            var basePath = imagePath[..^extension.Length];

            return
            [
                imagePath,
                $"{basePath}-2{extension}",
                $"{basePath}-3{extension}"
            ];
        }
    }
}