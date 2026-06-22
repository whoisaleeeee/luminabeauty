using System;
using System.Collections.Generic;

namespace LuminaBeauty.Services
{
    public class LanguageService
    {
        private string _currentLanguage = "es";

        public event Action? OnChange;

        public string CurrentLanguage => _currentLanguage;

        public static readonly Dictionary<string, Dictionary<string, string>> Translations = new()
        {
            ["es"] = new()
            {
                ["inicio"] = "Inicio",
                ["contacto"] = "Contacto",
                ["ingresar"] = "Ingresar",
                ["searchPlaceholder"] = "Buscar secretos de belleza canónicos...",
                ["banner"] = "DESCUENTO DE MADRES: 50% DCTO EN TODO CON EL CÓDIGO",
                ["compraAhora"] = "COMPRAR AHORA",
                ["subscribeTitle"] = "Recibe secretos exclusivos",
                ["subscribeDesc"] = "Únete a nuestro club de bienestar para recibir las mejores promociones y novedades.",
                ["placeholderEmail"] = "Tu correo electrónico",
                ["ayuda"] = "Servicio al Cliente",
                ["cuenta"] = "Mi Cuenta",
                ["terminos"] = "Términos y Condiciones",
                ["downloadApp"] = "Descarga la App",
                ["downloadDesc"] = "Compra fácil desde cualquier lugar. Disponible en iOS y Android.",
                ["copyright"] = "© 2026 Lumina Beauty. Desarrollado con ❤️ y elegancia coquette.",
                ["sculturaTitle"] = "LUMINA® BEAUTY LABS",
                ["heroHeading"] = "Tu esencia, redefinida con ciencia y lujo",
                ["heroSubheading"] = "Cruelty-free, vegano e ingredientes botánicos premium procedentes directamente de laboratorios coreanos.",
                ["loginTitle"] = "Ingresar a Lumina",
                ["loginSubtitle"] = "Accede a tus beneficios exclusivos.",
                ["emailPlaceholder"] = "Ingresar correo electrónico...",
                ["passwordPlaceholder"] = "Contraseña...",
                ["fullNamePlaceholder"] = "Nombre completo",
                ["confirmPasswordPlaceholder"] = "Confirmar contraseña...",
                ["rememberMe"] = "Recordarme siempre",
                ["forgotPassword"] = "¿Olvidaste tu contraseña?",
                ["loginBtn"] = "Iniciar sesión seguro",
                ["socialLogin"] = "O inicia sesión rápido con",
                ["noAccount"] = "¿Nuevo en Lumina Beauty?",
                ["signUpNow"] = "Crear una cuenta gratis",
                ["signupTitle"] = "Crear Cuenta VIP",
                ["signupSubtitle"] = "Comienza tu viaje de belleza premium hoy.",
                ["signupBtn"] = "Regístrate ahora",
                ["signupToastSuccess"] = "¡Cuenta VIP creada correctamente!",
                ["relatedProducts"] = "Secretos Similares"
            },
            ["en"] = new()
            {
                ["inicio"] = "Home",
                ["contacto"] = "Contact",
                ["ingresar"] = "Sign In",
                ["searchPlaceholder"] = "Search beauty secrets...",
                ["banner"] = "MOTHER'S DAY SALE: 50% OFF WITH THE CODE",
                ["compraAhora"] = "SHOP NOW",
                ["subscribeTitle"] = "Get exclusive secrets",
                ["subscribeDesc"] = "Join our wellness club for premium skincare tips and unique deals.",
                ["placeholderEmail"] = "Your email address",
                ["ayuda"] = "Customer Service",
                ["cuenta"] = "My Account",
                ["terminos"] = "Terms & Conditions",
                ["downloadApp"] = "Download App",
                ["downloadDesc"] = "Easy shopping anywhere. Available for iOS and Android.",
                ["copyright"] = "© 2026 Lumina Beauty. Crafted with ❤️ and coquette elegance.",
                ["sculturaTitle"] = "LUMINA® BEAUTY LABS",
                ["heroHeading"] = "Your essence, redefined with science and luxury",
                ["heroSubheading"] = "Cruelty-free, vegan, and premium botanical ingredients sourced directly from Korean labs.",
                ["loginTitle"] = "Sign In to Lumina",
                ["loginSubtitle"] = "Log in to access your exclusive coquette perks.",
                ["emailPlaceholder"] = "Enter email address...",
                ["passwordPlaceholder"] = "Password...",
                ["fullNamePlaceholder"] = "Full name",
                ["confirmPasswordPlaceholder"] = "Confirm password...",
                ["rememberMe"] = "Remember me",
                ["forgotPassword"] = "Forgot password?",
                ["loginBtn"] = "Secure Login",
                ["socialLogin"] = "Or sign in quickly with",
                ["noAccount"] = "New to Lumina Beauty?",
                ["signUpNow"] = "Create an account for free",
                ["signupTitle"] = "Create VIP Account",
                ["signupSubtitle"] = "Start your premium skincare journey today.",
                ["signupBtn"] = "Register now",
                ["signupToastSuccess"] = "VIP Account created successfully!",
                ["relatedProducts"] = "Similar Secrets"
            }
        };

        public void SetLanguage(string lang)
        {
            if (lang == "es" || lang == "en")
            {
                _currentLanguage = lang;
                OnChange?.Invoke();
            }
        }

        public string Translate(string key)
        {
            if (Translations.TryGetValue(_currentLanguage, out var transTable))
            {
                if (transTable.TryGetValue(key, out var translated))
                {
                    return translated;
                }
            }
            return key; // Fallback to key itself
        }
    }
}
