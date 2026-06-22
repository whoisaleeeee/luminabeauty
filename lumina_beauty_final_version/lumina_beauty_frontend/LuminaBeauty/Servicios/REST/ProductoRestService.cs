using LuminaBeauty.Servicios.Modelo;

namespace LuminaBeauty.Servicios.REST
{
  
    public class ProductoRestService
    {
        private readonly HttpClient http;

        public ProductoRestService(HttpClient http)
        {
            this.http = http;
        }

        // ── Operaciones Sincrónicas ──────────────────────────────────────────

        public List<Producto> ListarProductosTodos()
        {
            return http.GetFromJsonAsync<List<Producto>>("webresources/ProductoRS")
                .GetAwaiter().GetResult() ?? new List<Producto>();
        }

        public Producto? BuscarProductoPorId(string idProducto)
        {
            return http.GetFromJsonAsync<Producto>($"webresources/ProductoRS/{idProducto}")
                .GetAwaiter().GetResult();
        }

        public List<CategoriaProducto> ListarCategorias()
        {
            return http.GetFromJsonAsync<List<CategoriaProducto>>("webresources/ProductoRS/categorias")
                .GetAwaiter().GetResult() ?? new List<CategoriaProducto>();
        }

        public List<Marca> ListarMarcas()
        {
            return http.GetFromJsonAsync<List<Marca>>("webresources/ProductoRS/marcas")
                .GetAwaiter().GetResult() ?? new List<Marca>();
        }

        public int ValidarStock(string idProducto, int cantidad)
        {
            return http.GetFromJsonAsync<int>($"webresources/ProductoRS/validarStock/{idProducto}/{cantidad}")
                .GetAwaiter().GetResult();
        }

        public int DescontarStock(string idProducto, int cantidad)
        {
            var response = http.PutAsync($"webresources/ProductoRS/descontarStock/{idProducto}/{cantidad}", null)
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
                return response.Content.ReadFromJsonAsync<int>().GetAwaiter().GetResult();
            return 0;
        }

        // ── Operaciones Asíncronas ───────────────────────────────────────────

        public async Task<List<Producto>> ListarProductosTodosAsync()
        {
            return await http.GetFromJsonAsync<List<Producto>>("webresources/ProductoRS")
                ?? new List<Producto>();
        }

        public async Task<Producto?> BuscarProductoPorIdAsync(string idProducto)
        {
            return await http.GetFromJsonAsync<Producto>($"webresources/ProductoRS/{idProducto}");
        }

        public async Task<List<CategoriaProducto>> ListarCategoriasAsync()
        {
            return await http.GetFromJsonAsync<List<CategoriaProducto>>("webresources/ProductoRS/categorias")
                ?? new List<CategoriaProducto>();
        }

        public async Task<List<Marca>> ListarMarcasAsync()
        {
            return await http.GetFromJsonAsync<List<Marca>>("webresources/ProductoRS/marcas")
                ?? new List<Marca>();
        }

        public async Task<int> ValidarStockAsync(string idProducto, int cantidad)
        {
            return await http.GetFromJsonAsync<int>($"webresources/ProductoRS/validarStock/{idProducto}/{cantidad}");
        }

        public async Task<int> DescontarStockAsync(string idProducto, int cantidad)
        {
            var response = await http.PutAsync($"webresources/ProductoRS/descontarStock/{idProducto}/{cantidad}", null);
            if (response.IsSuccessStatusCode)
                return await response.Content.ReadFromJsonAsync<int>();
            return 0;
        }
    }
}
