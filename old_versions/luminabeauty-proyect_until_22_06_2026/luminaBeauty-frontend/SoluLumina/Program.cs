using LuminaBeauty.Components;
using LuminaBeauty.Services;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

var javaRestBaseUrl = builder.Configuration["JavaRest:BaseUrl"]
    ?? "http://localhost:8081/";

if (!javaRestBaseUrl.EndsWith('/'))
{
    javaRestBaseUrl += "/";
}

// Cliente HTTP para consumir los servicios REST publicados por el backend Java/JAX-RS.
builder.Services.AddScoped(_ => new HttpClient
{
    BaseAddress = new Uri(javaRestBaseUrl),
    Timeout = TimeSpan.FromSeconds(10)
});

builder.Services.AddScoped<LocalStorageService>();
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<CartService>();
builder.Services.AddScoped<LanguageService>();
builder.Services.AddScoped<LayoutStateService>();
builder.Services.AddScoped<ProductService>();
builder.Services.AddScoped<ToastService>();
builder.Services.AddScoped<WishlistService>();

var app = builder.Build();

app.Use(async (context, next) =>
{
    if (context.Request.Path.StartsWithSegments("/LuminaBeauty", out var remainingPath))
    {
        var targetPath = remainingPath.HasValue ? remainingPath.Value : "/home";
        context.Response.Redirect($"{targetPath}{context.Request.QueryString}");
        return;
    }

    await next();
});

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();

app.UseStaticFiles();
app.UseAntiforgery();

var productosDemo = new[]
{
    new ProductoDemo(1, "Serum Glow C", "Serum iluminador con vitamina C para uso diario.", 89m, 20, "Todo tipo de piel", "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&w=700&q=80", 1, new NombreDemo(1, "Skincare"), new NombreDemo(1, "Lumina")),
    new ProductoDemo(2, "Crema Hydra Bloom", "Crema hidratante ligera para rostro.", 74m, 14, "Piel normal a seca", "https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=700&q=80", 1, new NombreDemo(1, "Skincare"), new NombreDemo(1, "Lumina")),
    new ProductoDemo(3, "Tint Labial Rosy", "Tinte labial de larga duración con acabado natural.", 39m, 40, "Todo tipo de piel", "https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=700&q=80", 1, new NombreDemo(2, "Maquillaje"), new NombreDemo(1, "Lumina")),
    new ProductoDemo(4, "Protector Solar Velvet SPF50", "Protector solar facial con textura ligera.", 65m, 16, "Todo tipo de piel", "https://images.unsplash.com/photo-1596755389378-c31d21fd1273?auto=format&fit=crop&w=700&q=80", 1, new NombreDemo(1, "Skincare"), new NombreDemo(1, "Lumina")),
    new ProductoDemo(5, "Mist Fresh Balance", "Bruma refrescante para equilibrar la piel.", 49m, 25, "Piel mixta", "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?auto=format&fit=crop&w=700&q=80", 1, new NombreDemo(1, "Skincare"), new NombreDemo(1, "Lumina")),
    new ProductoDemo(6, "Mascara Lash Lift", "Máscara para pestañas con efecto lifting.", 52m, 30, "Todo tipo de piel", "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=700&q=80", 1, new NombreDemo(2, "Maquillaje"), new NombreDemo(1, "Lumina"))
};

app.MapGet("/LuminaBeauty-Servicios/webresources/ProductoRS/listar", () => Results.Json(productosDemo));
app.MapGet("/LuminaBeauty-Servicios/webresources/ProductoRS/{id:int}", (int id) =>
{
    var producto = productosDemo.FirstOrDefault(p => p.Id == id);
    return producto is null ? Results.NotFound() : Results.Json(producto);
});
app.MapGet("/LuminaBeauty-Servicios/webresources/ProductoRS/validarStock/{idProducto:int}/{cantidad:int}", (int idProducto, int cantidad) =>
{
    var producto = productosDemo.FirstOrDefault(p => p.Id == idProducto);
    return Results.Json(producto is not null && producto.Stock >= cantidad ? 1 : 0);
});
app.MapPut("/LuminaBeauty-Servicios/webresources/ProductoRS/descontarStock/{idProducto:int}/{cantidad:int}", () => Results.Json(1));
app.MapPost("/LuminaBeauty-Servicios/webresources/ClienteRS", (ClienteDemo cliente) =>
{
    var correo = string.IsNullOrWhiteSpace(cliente.Correo) ? "cliente@lumina.local" : cliente.Correo;
    var nombre = string.IsNullOrWhiteSpace(cliente.Nombre) ? "Cliente Lumina" : cliente.Nombre;
    return Results.Json(new { idCliente = 1, correo, nombre, estado = 1, puntos = 20 });
});
app.MapPost("/LuminaBeauty-Servicios/webresources/CarroRS/agregarProducto", () => Results.Json(1));

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();

record NombreDemo(int Id, string Nombre);
record ProductoDemo(int Id, string Nombre, string Descripcion, decimal Precio, int Stock, string TipoPiel, string Imagen, int Estado, NombreDemo Categoria, NombreDemo Marca);
record ClienteDemo(string? Correo, string? Nombre);
