namespace LuminaBeauty.Servicios.Modelo
{

    public class Cliente : Usuario
    {
        public int PuntosFidelidad { get; set; } = 0;
        public string NivelCliente { get; set; } = "BRONCE";
    }
}
