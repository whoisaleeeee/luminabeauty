package pe.edu.pucp.luminaBeauty.Model;

public class Cliente extends Usuario {
    private String tipo_usuario;
    private int puntos_fidelidad;
    private String nivel_cliente;

    private Direccion direccion_principal;

    public Cliente() {
        super(); // Llama al constructor de Usuario
    }

    public String getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
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
