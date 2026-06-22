using System;
using System.Collections.Generic;
using LuminaBeauty.Models;

namespace LuminaBeauty.Data
{
    public static class BeautyDb
    {
        // Conservamos las Marcas para elementos de diseño (filtros visuales, carrusel de logos)
        public static readonly List<Brand> Brands = new()
        {
            new Brand { Name = "Pixi", Logo = "PIXI" },
            new Brand { Name = "Petrizzio", Logo = "PETRIZZIO" },
            new Brand { Name = "Sheglam", Logo = "SHEGLAM" },
            new Brand { Name = "Laneige", Logo = "LANEIGE" },
            new Brand { Name = "Olaplex", Logo = "OLAPLEX" },
            new Brand { Name = "Revox", Logo = "REVOX" },
            new Brand { Name = "Anastasia", Logo = "ANASTASIA" }
        };

        // Conservamos el menú de Categorías y sus Subcategorías estáticas para la barra de navegación
        public static readonly List<Category> Categories = new()
        {
            new Category { Name = "Makeup", Subcategories = new() { "Lips", "Eyes", "Face", "Brushes" } },
            new Category { Name = "Skincare", Subcategories = new() { "Cleansers", "Toners", "Serums", "Moisturizers" } },
            new Category { Name = "Fragrance", Subcategories = new() { "Eau de Parfum", "Eau de Toilette", "Body Mist" } },
            new Category { Name = "Hair", Subcategories = new() { "Shampoo", "Conditioner", "Treatments" } },
            new Category { Name = "Tools & Brushes", Subcategories = new() { "Makeup Brushes", "Sponges", "Lash Curlers" } },
            new Category { Name = "Bath & Body", Subcategories = new() { "Body Wash", "Lotions", "Scrubs" } },
            new Category { Name = "Mini Size", Subcategories = new() { "Travel Essentials", "Mini Kits" } },
            new Category { Name = "Brands", Subcategories = new() { "All Luxury Brands", "New Brands" } },
            new Category { Name = "New", Subcategories = new() { "This Week", "Trending Secrets" } }
        };

        public static readonly List<Product> BestSells = new()
        {
            new Product { Id = 1, Name = "Serum Glow C", Brand = "Lumina", Category = "Skincare", Price = 89, Image = "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&w=700&q=80", Rating = 4.8, ReviewsCount = 24, Stock = 20 },
            new Product { Id = 2, Name = "Crema Hydra Bloom", Brand = "Lumina", Category = "Skincare", Price = 74, Image = "https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=700&q=80", Rating = 4.7, ReviewsCount = 18, Stock = 14 },
            new Product { Id = 3, Name = "Tint Labial Rosy", Brand = "Lumina", Category = "Makeup", Price = 39, Image = "https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=700&q=80", Rating = 4.6, ReviewsCount = 31, Stock = 40 },
            new Product { Id = 4, Name = "Protector Solar Velvet SPF50", Brand = "Lumina", Category = "Skincare", Price = 65, Image = "https://images.unsplash.com/photo-1596755389378-c31d21fd1273?auto=format&fit=crop&w=700&q=80", Rating = 4.9, ReviewsCount = 42, Stock = 16 }
        };

        // Conservamos las preguntas del cuestionario de Skincare (Quiz interactivo)
        public static readonly List<SkincareQuestion> SkincareQuestions = new()
        {
            new SkincareQuestion
            {
                Id = 1,
                Question = "¿Cuál es tu tipo de piel habitual?",
                Options = new()
                {
                    new QuizOption { Text = "Seca / Deshidratada", Value = "dry" },
                    new QuizOption { Text = "Grasa / Con Brillo", Value = "oily" },
                    new QuizOption { Text = "Mixta", Value = "comb" },
                    new QuizOption { Text = "Normal / Balanceada", Value = "normal" }
                }
            },
            new SkincareQuestion
            {
                Id = 2,
                Question = "¿Cuál es tu principal preocupación?",
                Options = new()
                {
                    new QuizOption { Text = "Líneas de expresión o arrugas", Value = "aging" },
                    new QuizOption { Text = "Bordes opacos / Falta de luminosidad", Value = "radiance" },
                    new QuizOption { Text = "Imperfecciones o acné", Value = "blemish" },
                    new QuizOption { Text = "Sensibilidad o rojeces", Value = "redness" }
                }
            },
            new SkincareQuestion
            {
                Id = 3,
                Question = "¿Qué tipo de acabado prefieres?",
                Options = new()
                {
                    new QuizOption { Text = "Efecto Dewy / Jugoso / Radiante", Value = "dewy" },
                    new QuizOption { Text = "Completamente Mate y Sedoso", Value = "matte" },
                    new QuizOption { Text = "Natural / Sin Sensación Pesada", Value = "natural" }
                }
            }
        };

        // Conservamos el diccionario internacional para el soporte Multiidioma (Cosa fija del UI)
        public static readonly Dictionary<string, Dictionary<string, string>> Translations = new()
        {
            ["es"] = new()
            {
                ["inicio"] = "Inicio",
                ["contacto"] = "Contacto",
                ["ingresar"] = "Ingresar",
                ["searchPlaceholder"] = "Buscar productos...",
                ["sculturaTitle"] = "Lumina Beauty",
                ["heroHeading"] = "Belleza que ilumina tu rutina",
                ["heroSubheading"] = "Descubre cuidado personal, skincare y maquillaje seleccionados para ti.",
                ["loginTitle"] = "Iniciar sesion",
                ["loginSubtitle"] = "Accede a tu cuenta",
                ["rememberMe"] = "Recordarme",
                ["forgotPassword"] = "Olvide mi contrasena",
                ["loginBtn"] = "Ingresar",
                ["socialLogin"] = "O continua con",
                ["noAccount"] = "No tienes cuenta?",
                ["signUpNow"] = "Registrate ahora",
                ["signupTitle"] = "Crear cuenta",
                ["signupSubtitle"] = "Unete a LuminaBeauty",
                ["fullNamePlaceholder"] = "Nombre completo",
                ["emailPlaceholder"] = "Correo electronico",
                ["passwordPlaceholder"] = "Contrasena",
                ["confirmPasswordPlaceholder"] = "Confirmar contrasena",
                ["signupBtn"] = "Crear cuenta",
                ["signupToastSuccess"] = "Cuenta creada correctamente.",
                ["subscribeTitle"] = "Suscribete",
                ["subscribeDesc"] = "Recibe novedades y ofertas de belleza.",
                ["placeholderEmail"] = "tu@email.com",
                ["ayuda"] = "Ayuda",
                ["cuenta"] = "Cuenta",
                ["terminos"] = "Terminos",
                ["downloadApp"] = "Descarga la app",
                ["downloadDesc"] = "Compra desde cualquier lugar.",
                ["copyright"] = "LuminaBeauty. Todos los derechos reservados.",
                ["banner"] = "Oferta especial con codigo",
                ["compraAhora"] = "Compra ahora"
            },
            ["en"] = new()
            {
                ["inicio"] = "Inicio",
                ["contacto"] = "Contacto",
                ["ingresar"] = "Ingresar",
                ["searchPlaceholder"] = "Buscar productos...",
                ["sculturaTitle"] = "Lumina Beauty",
                ["heroHeading"] = "Belleza que ilumina tu rutina",
                ["heroSubheading"] = "Descubre skincare, maquillaje y cuidado personal seleccionados para ti.",
                ["loginTitle"] = "Ingresar",
                ["loginSubtitle"] = "Accede a tu cuenta",
                ["rememberMe"] = "Recordarme",
                ["forgotPassword"] = "Olvidé mi contraseña",
                ["loginBtn"] = "Ingresar",
                ["socialLogin"] = "O continúa con",
                ["noAccount"] = "¿No tienes cuenta?",
                ["signUpNow"] = "Regístrate ahora",
                ["signupTitle"] = "Crear cuenta",
                ["signupSubtitle"] = "Únete a LuminaBeauty",
                ["fullNamePlaceholder"] = "Nombre completo",
                ["emailPlaceholder"] = "Correo electrónico",
                ["passwordPlaceholder"] = "Contraseña",
                ["confirmPasswordPlaceholder"] = "Confirmar contraseña",
                ["signupBtn"] = "Crear cuenta",
                ["signupToastSuccess"] = "Cuenta creada correctamente.",
                ["subscribeTitle"] = "Suscríbete",
                ["subscribeDesc"] = "Recibe novedades y ofertas de belleza.",
                ["placeholderEmail"] = "your@email.com",
                ["ayuda"] = "Ayuda",
                ["cuenta"] = "Cuenta",
                ["terminos"] = "Términos",
                ["downloadApp"] = "Descarga la app",
                ["downloadDesc"] = "Compra desde cualquier lugar.",
                ["copyright"] = "LuminaBeauty. Todos los derechos reservados.",
                ["banner"] = "Oferta especial con código",
                ["compraAhora"] = "Comprar ahora"
            }
        };
    }
}
