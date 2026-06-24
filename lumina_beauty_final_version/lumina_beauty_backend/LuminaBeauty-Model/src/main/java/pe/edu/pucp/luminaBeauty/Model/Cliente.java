package pe.edu.pucp.luminaBeauty.Model;

public class Cliente extends Usuario {

    private int puntos_fidelidad;
    private String nivel_cliente;
    private Direccion direccion_principal;

    public Cliente() {
        super();
        this.setTipo_usuario("CLIENTE");
        this.puntos_fidelidad = 0;
        this.nivel_cliente = "BRONCE";
    }

    public int getPuntos_fidelidad() {
        return puntos_fidelidad;
    }

    public void setPuntos_fidelidad(int puntos_fidelidad) {
        this.puntos_fidelidad = puntos_fidelidad;
    }

    public String getNivel_cliente() {
        return nivel_cliente;
    }

    public void setNivel_cliente(String nivel_cliente) {
        this.nivel_cliente = nivel_cliente;
    }

    public Direccion getDireccion_principal() {
        return direccion_principal;
    }

    public void setDireccion_principal(Direccion direccion_principal) {
        this.direccion_principal = direccion_principal;
    }
}