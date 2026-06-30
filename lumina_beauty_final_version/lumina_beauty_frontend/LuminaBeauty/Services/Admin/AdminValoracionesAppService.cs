using LuminaBeauty.Models;

namespace LuminaBeauty.Services.AppServices
{
    public class AdminValoracionesAppService
    {
        private readonly ValoracionAppService _valoracionApp;

        public List<Valoracion> Valoraciones { get; } = [];
        public HashSet<int> ProcesandoIds { get; } = [];

        public bool Cargando { get; private set; } = true;
        public string FiltroActual { get; private set; } = "pendiente";

        public AdminValoracionesAppService(ValoracionAppService valoracionApp)
        {
            _valoracionApp = valoracionApp;
        }

        public List<Valoracion> ValoracionesFiltradas =>
            Valoraciones
                .Where(valoracion =>
                    FiltroActual == "todos" ||
                    NormalizarEstado(valoracion.Estado)
                        .Equals(FiltroActual, StringComparison.OrdinalIgnoreCase))
                .OrderByDescending(valoracion => valoracion.IdValoracion)
                .ToList();

        public async Task CargarAsync()
        {
            try
            {
                Cargando = true;

                var valoraciones = await _valoracionApp.ListarTodasAsync();

                Valoraciones.Clear();
                Valoraciones.AddRange(valoraciones);
            }
            finally
            {
                Cargando = false;
            }
        }

        public void CambiarFiltro(string filtro)
        {
            FiltroActual = filtro;
        }

        public async Task PublicarAsync(Valoracion valoracion)
        {
            if (!PuedeProcesar(valoracion))
            {
                return;
            }

            try
            {
                ProcesandoIds.Add(valoracion.IdValoracion);

                var resultado = await _valoracionApp.PublicarAsync(
                    valoracion.IdValoracion);

                if (resultado is not null)
                {
                    valoracion.Estado = "PUBLICADA";
                }
            }
            finally
            {
                ProcesandoIds.Remove(valoracion.IdValoracion);
            }
        }

        public async Task RechazarAsync(Valoracion valoracion)
        {
            if (!PuedeProcesar(valoracion))
            {
                return;
            }

            try
            {
                ProcesandoIds.Add(valoracion.IdValoracion);

                var resultado = await _valoracionApp.RechazarAsync(
                    valoracion.IdValoracion);

                if (resultado is not null)
                {
                    valoracion.Estado = "RECHAZADA";
                }
            }
            finally
            {
                ProcesandoIds.Remove(valoracion.IdValoracion);
            }
        }

        public static string NormalizarEstado(string? estado)
        {
            return string.IsNullOrWhiteSpace(estado)
                ? "PENDIENTE"
                : estado.Trim().ToUpperInvariant();
        }

        public static string ObtenerNombreCliente(Valoracion valoracion)
        {
            return valoracion.Cliente is null
                ? "Cliente no disponible"
                : $"Cliente #{valoracion.Cliente.Id}";
        }

        private bool PuedeProcesar(Valoracion valoracion)
        {
            return valoracion.IdValoracion > 0 &&
                   !ProcesandoIds.Contains(valoracion.IdValoracion);
        }
    }
}