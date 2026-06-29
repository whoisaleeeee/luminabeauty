using static SoluLumina.Components.Pages.Admin.Configuracion;
using static SoluLumina.Components.Pages.Admin.Reclamos;

namespace SoluLumina.Models
{
    public class Devolucion
    {
        private int id_devolucion;
        private String motivo;
        private String detalle_motivo;
        private String estado;
        private DateTime solicitado_en;
        private DateTime aprobado_en;
        private DateTime recibido_en;
        private DateTime fecha_creacion;
        private DateTime fecha_actualizacion;

        private Cliente cliente;
        private Pedido pedido;
        private Reclamo reclamo;
        private Empleado aprobado_por;
        private Empleado recibido_por;

        public int Id_devolucion { get => id_devolucion; set => id_devolucion = value; }
        public string Motivo { get => motivo; set => motivo = value; }
        public string Detalle_motivo { get => detalle_motivo; set => detalle_motivo = value; }
        public string Estado { get => estado; set => estado = value; }
        public DateTime Solicitado_en { get => solicitado_en; set => solicitado_en = value; }
        public DateTime Aprobado_en { get => aprobado_en; set => aprobado_en = value; }
        public DateTime Recibido_en { get => recibido_en; set => recibido_en = value; }
        public DateTime Fecha_creacion { get => fecha_creacion; set => fecha_creacion = value; }
        public DateTime Fecha_actualizacion { get => fecha_actualizacion; set => fecha_actualizacion = value; }
        public Cliente Cliente { get => cliente; set => cliente = value; }
        public Pedido Pedido { get => pedido; set => pedido = value; }
        public Reclamo Reclamo { get => reclamo; set => reclamo = value; }
        public Empleado Aprobado_por { get => aprobado_por; set => aprobado_por = value; }
        public Empleado Recibido_por { get => recibido_por; set => recibido_por = value; }
    }
}
