    package pe.edu.pucp.luminaBeauty.Model;
    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    public class Pedido {
        private int id;
        private LocalDateTime fecha;
        private BigDecimal total;
        private String estado; // ENUM ('PENDIENTE', 'ENVIADO', etc.)
        // Llaves foráneas necesarias para el DAO
        private CarroDeCompras carroDeCompras;
        private Cupon cupon; // Puede ser null en SQL

        // Relaciones lógicas
        private ArrayList<DetallePedido> detalles;

        public Pedido() {
            this.detalles = new ArrayList<>();
        }

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public LocalDateTime getFecha() { return fecha; }
        public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public CarroDeCompras getCarroDeCompras() {return carroDeCompras;}
        public void setCarroDeCompras(CarroDeCompras carroDeCompras) {this.carroDeCompras = carroDeCompras;}

        public Cupon getCupon() {return cupon;}
        public void setCupon(Cupon cupon) {this.cupon = cupon;}

        public ArrayList<DetallePedido> getDetalles() {return detalles;}
        public void setDetalles(ArrayList<DetallePedido> detalles) {this.detalles = detalles;}
    }
