package pe.edu.pucp.luminaBeauty.Model;

public class Cliente extends Usuario {
    // El idUsuario lo hereda de Usuario
    private int puntosFidelidad;
    private String nivelCliente; // En SQL es un ENUM, en Java lo manejamos como String

    public Cliente() {
        super(); // Llama al constructor de Usuario
    }

    // Getters y Setters específicos de Cliente
    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    }

    public String getNivelCliente() {
        return nivelCliente;
    }

    public void setNivelCliente(String nivelCliente) {
        this.nivelCliente = nivelCliente;
    }
}
