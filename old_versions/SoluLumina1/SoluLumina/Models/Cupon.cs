using Newtonsoft.Json;

namespace SoluLumina.Models
{
    public class Cupon
    {
        private int id_cupon;
        private string codigo;
        private string tipo_descuento;
        private decimal valor_descuento;
        private DateTime? fecha_inicio;
        private DateTime? fecha_fin;
        private int? cantidad_usos;
        private int? limite_uso;
        private int estado;
        private DateTime? fecha_creacion;
        private DateTime? fecha_actualizacion;

        [JsonProperty("id_cupon")]
        public int Id_cupon
        {
            get => id_cupon;
            set => id_cupon = value;
        }

        [JsonProperty("codigo")]
        public string Codigo
        {
            get => codigo;
            set => codigo = value;
        }

        [JsonProperty("tipo_descuento")]
        public string Tipo_descuento
        {
            get => tipo_descuento;
            set => tipo_descuento = value;
        }

        [JsonProperty("valor_descuento")]
        public decimal Valor_descuento
        {
            get => valor_descuento;
            set => valor_descuento = value;
        }

        [JsonProperty("fecha_inicio")]
        public DateTime? Fecha_inicio
        {
            get => fecha_inicio;
            set => fecha_inicio = value;
        }

        [JsonProperty("fecha_fin")]
        public DateTime? Fecha_fin
        {
            get => fecha_fin;
            set => fecha_fin = value;
        }

        [JsonProperty("cantidad_usos")]
        public int? Cantidad_usos
        {
            get => cantidad_usos;
            set => cantidad_usos = value;
        }

        [JsonProperty("limite_uso")]
        public int? Limite_uso
        {
            get => limite_uso;
            set => limite_uso = value;
        }

        [JsonProperty("estado")]
        public int Estado
        {
            get => estado;
            set => estado = value;
        }

        [JsonProperty("fecha_creacion")]
        public DateTime? Fecha_creacion
        {
            get => fecha_creacion;
            set => fecha_creacion = value;
        }

        [JsonProperty("fecha_actualizacion")]
        public DateTime? Fecha_actualizacion
        {
            get => fecha_actualizacion;
            set => fecha_actualizacion = value;
        }
    }
}