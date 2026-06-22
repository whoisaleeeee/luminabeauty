using SoluLumina.Models;

namespace SoluLumina.Data
{
    public static class SampleBrands
    {
        public static List<Marca> GetMarcas()
        {
            return new List<Marca>
            {
                new Marca
                {
                    Id = 1,
                    Nombre = "L'Oréal Paris",
                    Descripcion = "Líder mundial en cosmética y belleza",
                },
                new Marca
                {
                    Id = 2,
                    Nombre = "Lumina Brand",
                    Descripcion = "Marca ficticia de belleza",
                }
            };
        }
    }
}
