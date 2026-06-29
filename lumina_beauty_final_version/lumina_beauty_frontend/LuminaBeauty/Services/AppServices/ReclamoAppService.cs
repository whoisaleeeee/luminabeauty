using LuminaBeauty.Models;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices;

public class ReclamoAppService
{
    private readonly ReclamoRestService _reclamoRestService;

    public ReclamoAppService(ReclamoRestService reclamoRestService)
    {
        _reclamoRestService = reclamoRestService;
    }

    public Task<List<Reclamo>> ListarPorClienteAsync(int idCliente)
    {
        return _reclamoRestService.ListarPorClienteAsync(idCliente);
    }

    public Task<Reclamo?> RegistrarAsync(Reclamo reclamo)
    {
        return _reclamoRestService.RegistrarReclamoAsync(reclamo);
    }
}
