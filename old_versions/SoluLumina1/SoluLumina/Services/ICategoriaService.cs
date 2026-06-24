namespace SoluLumina.Services
{
    using SoluLumina.Models;

    public interface ICategoriaService
    {
        Task<List<CategoriaProducto>> ListarTodosAsync();
    }
}
