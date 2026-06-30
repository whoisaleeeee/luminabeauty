using LuminaBeauty.Components;
using LuminaBeauty.Models;
using LuminaBeauty.Services;
using LuminaBeauty.Services.Admin;
using LuminaBeauty.Services.AppServices;
using LuminaBeauty.Servicios.REST;

var builder = WebApplication.CreateBuilder(args);

// Razor / Blazor Server
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Backend Java GlassFish
builder.Services.AddHttpClient("LuminaBackend", client =>
{
    client.BaseAddress = new Uri("http://localhost:8080/LuminaBeauty-Servicios/");
    client.Timeout = TimeSpan.FromSeconds(10);

    client.DefaultRequestHeaders.Accept.Clear();
    client.DefaultRequestHeaders.Accept.ParseAdd("application/json");
});

// Hace que los servicios REST reciban el HttpClient configurado.
builder.Services.AddScoped(sp =>
{
    IHttpClientFactory factory = sp.GetRequiredService<IHttpClientFactory>();
    return factory.CreateClient("LuminaBackend");
});

// Servicios REST
builder.Services.AddScoped<ProductoRestService>();
builder.Services.AddScoped<CheckoutRestService>();
builder.Services.AddScoped<ClienteRestService>();
builder.Services.AddScoped<CarroRestService>();
builder.Services.AddScoped<AuthRestService>();
builder.Services.AddScoped<PedidoRestService>();
builder.Services.AddScoped<DireccionRestService>();
builder.Services.AddScoped<MovimientoPuntosFidelidadRestService>();
builder.Services.AddScoped<EnvioRestService>();
builder.Services.AddScoped<ValoracionRestService>();
builder.Services.AddScoped<CuponRestService>();
builder.Services.AddScoped<UsoCuponRestService>();
builder.Services.AddScoped<ReclamoRestService>();
builder.Services.AddScoped<PagoRestService>();

// Servicios de aplicación
builder.Services.AddScoped<LocalStorageService>();
builder.Services.AddScoped<SessionStorageService>();
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<CartService>();
builder.Services.AddScoped<WishlistService>();
builder.Services.AddScoped<LanguageService>();
builder.Services.AddScoped<LayoutStateService>();
builder.Services.AddScoped<ToastService>();
builder.Services.AddScoped<ProductService>();
builder.Services.AddScoped<HomeService>();

builder.Services.AddScoped<ClienteAppService>();
builder.Services.AddScoped<ProfileAppService>();
builder.Services.AddScoped<CarroAppService>();
builder.Services.AddScoped<PedidoAppService>();
builder.Services.AddScoped<CheckoutAppService>();
builder.Services.AddScoped<MovimientoPuntosAppService>();
builder.Services.AddScoped<EnvioAppService>();
builder.Services.AddScoped<ValoracionAppService>();
builder.Services.AddScoped<ShopAppService>();
builder.Services.AddScoped<HeaderSearchAppService>();
builder.Services.AddScoped<CuponAppService>();
builder.Services.AddScoped<UsoCuponAppService>();
builder.Services.AddScoped<ReclamoAppService>();
builder.Services.AddScoped<PagoAppService>();
builder.Services.AddScoped<AdminValoracionesAppService>();

// Admin
builder.Services.AddScoped<ReporteJasperService>();

var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseStaticFiles();
app.UseAntiforgery();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
