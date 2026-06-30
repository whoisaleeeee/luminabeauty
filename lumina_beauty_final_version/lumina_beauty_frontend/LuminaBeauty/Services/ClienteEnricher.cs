using LuminaBeauty.Models;

namespace LuminaBeauty.Services;

public static class ClienteEnricher
{
    public static Dictionary<int, Cliente> CrearIndice(IEnumerable<Cliente> clientes) =>
        clientes
            .Where(c => c.Id > 0)
            .GroupBy(c => c.Id)
            .ToDictionary(g => g.Key, g => g.First());

    public static void EnriquecerCliente(Cliente? cliente, IReadOnlyDictionary<int, Cliente> indice)
    {
        if (cliente is not { Id: > 0 }) return;
        if (!indice.TryGetValue(cliente.Id, out var completo)) return;

        cliente.Nombre = completo.Nombre;
        cliente.Apellido = completo.Apellido;
        cliente.Correo = completo.Correo;
    }

    public static void EnriquecerPedidos(IEnumerable<Pedido> pedidos, IEnumerable<Cliente> clientes)
    {
        var indice = CrearIndice(clientes);
        foreach (var pedido in pedidos)
            EnriquecerCliente(pedido.Cliente, indice);
    }

    public static void EnriquecerReclamos(IEnumerable<Reclamo> reclamos, IEnumerable<Cliente> clientes)
    {
        var indice = CrearIndice(clientes);
        foreach (var reclamo in reclamos)
            EnriquecerCliente(reclamo.Cliente, indice);
    }

    public static string FormatearNombre(Cliente? cliente)
    {
        if (cliente == null) return "Sin Cliente";

        var nombre = $"{cliente.Nombre} {cliente.Apellido}".Trim();
        if (!string.IsNullOrWhiteSpace(nombre)) return nombre;

        return cliente.Id > 0 ? $"Cliente #{cliente.Id}" : "Sin Cliente";
    }
}
