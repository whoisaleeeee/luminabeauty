namespace SoluLumina.Models
{
    public class Product
    {
        public int Id { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string SKU { get; set; } = string.Empty;
        public int Stock { get; set; }
        public string Categoria { get; set; } = string.Empty;
        public string Subcategoria { get; set; } = string.Empty;
        public string Marca { get; set; } = string.Empty;
        public string CodigoBarras { get; set; } = string.Empty;
        public string TipoPiel { get; set; } = string.Empty;
        public double PrecioVenta { get; set; }
        public double PrecioCosto { get; set; }
        public int StockMinimo { get; set; }
        public string ImagenUrl { get; set; } = string.Empty;
        public string CatBgColor { get; set; } = "#fcf0f4";
        public string CatColor { get; set; } = "#6a3849";

        // Computed properties for stock badge colors
        public string StockBgColor => Stock > 0 ? "#e2f3eb" : "#fdf5f5";
        public string StockColor => Stock > 0 ? "#198754" : "#d9557b";
    }
}
