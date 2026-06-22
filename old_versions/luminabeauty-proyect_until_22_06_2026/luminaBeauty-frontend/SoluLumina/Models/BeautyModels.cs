using LuminaBeauty.Models;
using System;
using System.Collections.Generic;

namespace LuminaBeauty.Models
{
    public class Product
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Slug { get; set; } = string.Empty;
        public string Brand { get; set; } = string.Empty;
        public string TipoPiel { get; set; } = string.Empty;
        public string Category { get; set; } = string.Empty;
        public decimal Price { get; set; }
        public decimal? OriginalPrice { get; set; }
        public string Image { get; set; } = string.Empty;
        public double Rating { get; set; }
        public int ReviewsCount { get; set; }
        public string? Discount { get; set; }
        public string? DiscountBadge { get; set; }
        public int? Stock { get; set; }
        public string? Description { get; set; }
        public List<string> Ingredients { get; set; } = new();
        public string? Usage { get; set; }
        public DateTime CreationDate { get; set; }
        public DateTime ActualizationDate { get; set; }
    }

    public class Brand
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Logo { get; set; } = string.Empty;
    }

    public class Category
    {
        public int Id { set; get; }
        public string Name { get; set; } = string.Empty;
        public List<string> Subcategories { get; set; } = new();
    }

    public class CartItem
    {
        public Product Product { get; set; } = new();
        public int Quantity { get; set; }
    }

    public class SkincareQuestion
    {
        public int Id { get; set; }
        public string Question { get; set; } = string.Empty;
        public List<QuizOption> Options { get; set; } = new();
    }

    public class QuizOption
    {
        public string Text { get; set; } = string.Empty;
        public string Value { get; set; } = string.Empty;
    }

    public class Review
    {
        public string Name { get; set; } = string.Empty;
        public int Rating { get; set; }
        public string Comment { get; set; } = string.Empty;
        public string Date { get; set; } = string.Empty;
    }

    public class Order
    {
        public int Id { get; set; }
        public DateTime Fecha { get; set; }
        public string Estado { get; set; } = string.Empty;
        public decimal Total { get; set; }
        public List<OrderItem> Items { get; set; } = new();
    }

    public class OrderItem
    {
        public string Name { get; set; } = string.Empty;
        public int Qty { get; set; }
        public decimal Price { get; set; }
    }

    public class UserProfile
    {
        public string Name { get; set; } = "Alexandra Arrobas";
        public string Email { get; set; } = "alexandra.arrobas@pucp.edu.pe";
        public string Phone { get; set; } = "+51 987 654 321";
        public string Address { get; set; } = "Av. Camino Real 450, Dpto 502";
        public string City { get; set; } = "San Isidro";
        public string Region { get; set; } = "Lima";
        public string PostalCode { get; set; } = "15073";
    }
}