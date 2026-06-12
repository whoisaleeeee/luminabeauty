using SoluLumina.Models;

namespace SoluLumina.Data
{
    public static class SampleProducts
    {
        public static List<Product> GetProducts()
        {
            return new List<Product>
            {
                new Product
                {
                    Id = 2,
                    Nombre = "Stay All Day Waterproof Liquid Eye Liner",
                    SKU = "RS-SER-021",
                    PrecioVenta = 24.00,
                    Stock = 35,
                    TipoPiel = "Todo Tipo",
                    ImagenUrl = "https://images.unsplash.com/photo-1625093742435-6fa192b6fb10?auto=format&fit=crop&q=80&w=600",
                    Marca = "STILA",
                    Categoria = "Makeup"
                },
                new Product
                {
                    Id = 3,
                    Nombre = "Ultra Facial Cream Hydrator",
                    SKU = "RS-SER-022",
                    PrecioVenta = 38.50,
                    Stock = 15,
                    TipoPiel = "Seca",
                    ImagenUrl = "https://images.unsplash.com/photo-1608248597481-496100c8c836?auto=format&fit=crop&q=80&w=600",
                    Marca = "KIEHLS",
                    Categoria = "Skincare"
                },
                new Product
                {
                    Id = 4,
                    Nombre = "Hyaluronic Acid Intensive Serum",
                    SKU = "RS-SER-023",
                    PrecioVenta = 42.00,
                    Stock = 22,
                    TipoPiel = "Mixta",
                    ImagenUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&q=80&w=600",
                    Marca = "ORDINARY",
                    Categoria = "Skincare"
                },
                new Product
                {
                    Id = 5,
                    Nombre = "Matte Clay Skin Clarity Foundation",
                    SKU = "RS-SER-024",
                    PrecioVenta = 18.90,
                    Stock = 18,
                    TipoPiel = "Grasa",
                    ImagenUrl = "https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&q=80&w=600",
                    Marca = "THE BODY SHOP",
                    Categoria = "Makeup"
                },
                new Product
                {
                    Id = 6,
                    Nombre = "Naked Honey Eyeshadow Palette",
                    SKU = "RS-SER-025",
                    PrecioVenta = 49.90,
                    Stock = 12,
                    TipoPiel = "Todo Tipo",
                    ImagenUrl = "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&q=80&w=600",
                    Marca = "URBAN DECAY",
                    Categoria = "Makeup"
                },
                new Product
                {
                    Id = 7,
                    Nombre = "Salicylic Acid Daily Cleanser",
                    SKU = "RS-SER-026",
                    PrecioVenta = 15.00,
                    Stock = 40,
                    TipoPiel = "Grasa",
                    ImagenUrl = "https://images.unsplash.com/photo-1556229010-aa3f7ff66b24?auto=format&fit=crop&q=80&w=600",
                    Marca = "COSRX",
                    Categoria = "Skincare"
                },
                new Product
                {
                    Id = 8,
                    Nombre = "Glow Recipe Watermelon Sleeping Mask",
                    SKU = "RS-SER-027",
                    PrecioVenta = 34.00,
                    Stock = 8,
                    TipoPiel = "Seca",
                    ImagenUrl = "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&q=80&w=600",
                    Marca = "GLOW RECIPE",
                    Categoria = "Skincare"
                },
                new Product
                {
                    Id = 9,
                    Nombre = "Velvet Matte Lip Pencil Extreme",
                    SKU = "RS-SER-028",
                    PrecioVenta = 27.00,
                    Stock = 25,
                    TipoPiel = "Todo Tipo",
                    ImagenUrl = "https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&q=80&w=600",
                    Marca = "NARS",
                    Categoria = "Makeup"
                },
                new Product
                {
                    Id = 10,
                    Nombre = "Argan Oil Repairing Hair Mask",
                    SKU = "RS-SER-029",
                    PrecioVenta = 12.50,
                    Stock = 50,
                    TipoPiel = "Todo Tipo",
                    ImagenUrl = "https://images.unsplash.com/photo-1535585209827-a15fcdbc4c2d?auto=format&fit=crop&q=80&w=600",
                    Marca = "HASK",
                    Categoria = "Haircare"
                },
                new Product
                {
                    Id = 11,
                    Nombre = "Translucent Loose Setting Powder",
                    SKU = "RS-SER-030",
                    PrecioVenta = 39.90,
                    Stock = 30,
                    TipoPiel = "Mixta",
                    ImagenUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&q=80&w=600",
                    Marca = "LAURA MERCIER",
                    Categoria = "Makeup"
                }
            };
        }
    }
}
