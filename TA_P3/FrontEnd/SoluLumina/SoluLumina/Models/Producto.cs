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
    }
}
