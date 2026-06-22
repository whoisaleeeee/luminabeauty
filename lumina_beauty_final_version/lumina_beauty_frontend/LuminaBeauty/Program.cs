using LuminaBeauty.Components;
using LuminaBeauty.Services;
using LuminaBeauty.Servicios.REST;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System.Net.Http;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// ── Configurar HttpClient con la URL base del backend Java ───────────────────
builder.Services.AddScoped(sp =>
{
    HttpClient client = new HttpClient();
    client.BaseAddress = new Uri("http://localhost:8080/LuminaBeauty-Servicios/");
    return client;
});

// ── Servicios REST (Servicios/REST/) ─────────────────────────────────────────
// Equivalentes a los *RestService.cs del proyecto modelo
builder.Services.AddScoped<ProductoRestService>();
builder.Services.AddScoped<ClienteRestService>();
builder.Services.AddScoped<CarroRestService>();

// ── Servicios de aplicación (Services/) ──────────────────────────────────────
// Orquestan la lógica UI y consumen los servicios REST
builder.Services.AddScoped<LocalStorageService>();
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<CartService>();
builder.Services.AddScoped<WishlistService>();
builder.Services.AddScoped<LanguageService>();
builder.Services.AddScoped<LayoutStateService>();
builder.Services.AddScoped<ToastService>();
builder.Services.AddScoped<ProductService>();
builder.Services.AddScoped<HomeService>();
builder.Services.AddScoped<ClienteAppService>();
builder.Services.AddScoped<CarroAppService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
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
