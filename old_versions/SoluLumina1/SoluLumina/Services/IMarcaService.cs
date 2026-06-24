namespace SoluLumina.Services
{
    using SoluLumina.Models;

    public interface IMarcaService
    {
        Task<List<Marca>> ListarTodosAsync();
    }
}
