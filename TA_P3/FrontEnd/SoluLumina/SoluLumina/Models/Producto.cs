namespace SoluLumina.Models
{
    public class Producto
    {
        public int Id { get; set; }
        public string? Nombre { get; set; }
        public string? Slug { get; set; }
        public string? Descripcion { get; set; }
        public decimal Precio { get; set; }
        public int Stock { get; set; }
        public string? TipoPiel { get; set; }
        public string? Imagen { get; set; }
        public int Estado { get; set; }
        public CategoriaProducto? Categoria { get; set; }
        public Marca? Marca { get; set; }

        // Computed properties para la UI
        public string CatBgColor => "#fcf0f4";
        public string CatColor => "#6a3849";
        public string StockBgColor => Stock > 0 ? "#e2f3eb" : "#fdf5f5";
        public string StockColor => Stock > 0 ? "#198754" : "#d9557b";
    }
}
