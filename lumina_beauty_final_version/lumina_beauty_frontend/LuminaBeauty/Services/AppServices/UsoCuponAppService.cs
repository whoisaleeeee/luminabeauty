using LuminaBeauty.Servicios.Modelo;
using LuminaBeauty.Servicios.REST;

namespace LuminaBeauty.Services.AppServices
{
    public class UsoCuponAppService
    {
        private readonly UsoCuponRestService _usoCuponRestService;

        public UsoCuponAppService(UsoCuponRestService usoCuponRestService)
        {
            _usoCuponRestService = usoCuponRestService;
        }

        public Task<UsoCupon?> RegistrarAsync(UsoCupon usoCupon)
        {
            return _usoCuponRestService.RegistrarAsync(usoCupon);
        }

        public Task<bool> ClienteYaUsoAsync(int idCliente, int idCupon)
        {
            return _usoCuponRestService.ClienteYaUsoAsync(idCliente, idCupon);
        }
    }
}