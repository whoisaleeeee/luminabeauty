using SoluLumina.Models;

namespace SoluLumina.Data
{
    public static class SampleCategories
    {
        public static List<CategoriaProducto> GetCategories()
        {
            return new List<CategoriaProducto>
            {
                new CategoriaProducto
                {
                    Id = 1,
                    Nombre = "Cuidado Facial",
                    Descripcion = "Serums, cremas hidratantes y protectores solares",
                },
                new CategoriaProducto
                {
                    Id = 2,
                    Nombre = "Skincare",
                    Descripcion = "Productos de cuidado facial",
                },
                new CategoriaProducto
                {
                    Id = 3,
                    Nombre = "Makeup",
                    Descripcion = "Productos de belleza",
                }
            };
        }
    }
}