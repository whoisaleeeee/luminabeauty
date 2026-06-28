using LuminaBeauty.Models;
using LuminaBeauty.Models;
using LuminaBeauty.Models;

namespace LuminaBeauty.Services.PageStates
{
    public class ProfilePageState
    {
        public string ActiveTab { get; set; } = "detalles";

        public Cliente? CurrentClient { get; set; }

        public List<Direccion> Addresses { get; set; } = [];

        public List<Pedido> Orders { get; set; } = [];

        public Pedido? SelectedOrder { get; set; }

        public Envio? SelectedOrderShipment { get; set; }

        public bool IsOrderDetailOpen { get; set; }

        public bool IsLoadingOrderDetail { get; set; }

        public string ProfileFirstName { get; set; } = string.Empty;

        public string ProfileLastName { get; set; } = string.Empty;

        public string ProfileEmail { get; set; } = string.Empty;

        public string ProfilePhone { get; set; } = string.Empty;

        public Direccion EditingAddress { get; set; } = new();

        public Direccion? AddressPendingDeletion { get; set; }

        public bool IsLoadingProfile { get; set; } = true;

        public bool IsSavingProfile { get; set; }

        public bool IsLoadingAddresses { get; set; }

        public bool IsLoadingOrders { get; set; }

        public List<MovimientoPuntosFidelidad> PointMovements { get; set; } = [];

        public bool IsLoadingPoints { get; set; }

        public bool IsAddressEditorOpen { get; set; }

        public bool IsSavingAddress { get; set; }

        public bool IsDeletingAddress { get; set; }
    }

    public record ProfileActionResult(
        bool Success,
        string Message
    );
}