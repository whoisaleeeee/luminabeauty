namespace SoluLumina.Services
{
    using SoluLumina.Models;

    public interface IProductoService
    {
        Task<List<Producto>> ListarTodosAsync();
        Task<Producto?> BuscarPorIdAsync(int id);
        Task<Producto?> InsertarAsync(Producto producto);
        Task<Producto?> ActualizarAsync(Producto producto);
        Task<bool> EliminarAsync(int id);
    }
}
