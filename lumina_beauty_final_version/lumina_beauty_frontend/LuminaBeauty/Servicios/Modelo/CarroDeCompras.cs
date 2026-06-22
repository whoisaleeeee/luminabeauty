namespace LuminaBeauty.Servicios.Modelo
{
   
    public class CarroDeCompras
    {
        public int Id { get; set; }
        public Cliente? Cliente { get; set; }
        public string? FechaCreacion { get; set; }
        public List<DetalleCarro> Detalles { get; set; } = new();
    }

    
    public class DetalleCarro
    {
        public int Id { get; set; }
        public Producto? Producto { get; set; }
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
    }
}
