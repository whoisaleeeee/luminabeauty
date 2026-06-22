namespace LuminaBeauty.Servicios.Modelo
{
 
    public class Producto
    {
        public string Id { get; set; } = string.Empty;
        public string Nombre { get; set; } = string.Empty;
        public string Slug { get; set; } = string.Empty;
        public string Descripcion { get; set; } = string.Empty;
        public decimal Precio { get; set; }
        public int Stock { get; set; }
        public string TipoPiel { get; set; } = string.Empty;
        public string Imagen { get; set; } = string.Empty;
        public int Estado { get; set; }
        public CategoriaProducto? Categoria { get; set; }
        public Marca? Marca { get; set; }
    }
}
