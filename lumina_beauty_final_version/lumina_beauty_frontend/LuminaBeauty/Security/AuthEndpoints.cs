using System.Security.Claims;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace LuminaBeauty.Security
{
    public static class AuthEndpoints
    {
        public static IEndpointRouteBuilder MapAuthEndpoints(this IEndpointRouteBuilder endpoints)
        {
            endpoints.MapPost("/auth/login", async (HttpContext context) =>
            {
                var form = await context.Request.ReadFormAsync();
                var email = form["email"].ToString();
                var password = form["password"].ToString();
                var rememberMe = form["rememberMe"].ToString() == "on";

                if (string.IsNullOrWhiteSpace(email) || string.IsNullOrWhiteSpace(password))
                    return Results.LocalRedirect("/?error=1");

                string role;
                string displayName;

                if (email.Equals("andrea.mendoza@lumina.com", StringComparison.OrdinalIgnoreCase) && password == "HASH_PRUEBA_EMPLEADO_ANDREA_2026_F36C9E2B")
                {
                    role = "Admin";
                    displayName = "Administrador Lumina";
                }
                else if (email.Equals("mariana.castillo@lumina.com", StringComparison.OrdinalIgnoreCase) && password == "$2a$10$fghijklmnopqrstuvwxyufghijklmnopqrstuvwxyabcdefghij")
                {
                    role = "Client";
                    displayName = "Cliente Lumina";
                }
                else
                {
                    return Results.LocalRedirect("/?error=1");
                }

                var claims = new List<Claim>
            {
                new(ClaimTypes.Name, email),
                new(ClaimTypes.Role, role),
                new("DisplayName", displayName),
                new("EmployeeId", "56478")
            };

                var principal = new ClaimsPrincipal(
                    new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme));

                await context.SignInAsync(
                    CookieAuthenticationDefaults.AuthenticationScheme,
                    principal,
                    new AuthenticationProperties
                    {
                        // rememberMe=true  → persistent cookie, survives browser restarts, expires in 8 hours
                        // rememberMe=false → short-lived cookie (30 min); avoids browser session-restore keeping
                        //                    the user logged in indefinitely despite not ticking "remember me"
                        IsPersistent = rememberMe,
                        ExpiresUtc = rememberMe
                            ? DateTimeOffset.UtcNow.AddHours(8)
                            : DateTimeOffset.UtcNow.AddMinutes(30)
                    });

                return Results.LocalRedirect("/home");
            });

            endpoints.MapGet("/auth/logout", async (HttpContext context) =>
            {
                await context.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
                return Results.LocalRedirect("/");
            });

            return endpoints;
        }
    }
}
