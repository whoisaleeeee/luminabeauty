namespace LuminaBeauty.Servicios.Modelo
{
  
    public class CategoriaProducto
    {
        public int Id { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string Descripcion { get; set; } = string.Empty;
        public int IdCategoriaPadre { get; set; }
    }
}
