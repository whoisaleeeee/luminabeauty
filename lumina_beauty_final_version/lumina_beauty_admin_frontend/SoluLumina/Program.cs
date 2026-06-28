using SoluLumina.Components;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Configurar HttpClient base apuntando al servidor de Java (usualmente 8080)
builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri("http://localhost:8080/LuminaBeauty-Servicios/webresources/") });

// Registrar los nuevos servicios
builder.Services.AddScoped<SoluLumina.Services.IProductoService, SoluLumina.Services.ProductoService>();
builder.Services.AddScoped<SoluLumina.Services.ICategoriaService, SoluLumina.Services.CategoriaService>();
builder.Services.AddScoped<SoluLumina.Services.IMarcaService, SoluLumina.Services.MarcaService>();
builder.Services.AddScoped<SoluLumina.Services.ReporteJasperService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();

app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
