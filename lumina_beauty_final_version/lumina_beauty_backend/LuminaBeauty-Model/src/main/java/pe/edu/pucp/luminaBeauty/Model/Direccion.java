package pe.edu.pucp.luminaBeauty.Model;

public class Direccion {
    private int id;
    private String direccion;
    private String ciudad;
    private String pais; // Agregado (NOT NULL en SQL)
    private String referencia;
    private String codigoPostal; // Agregado
    private boolean esPrincipal;
    private Cliente cliente; // Agregado (La FK obligatoria)

    public Direccion() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public boolean isEsPrincipal() { return esPrincipal; } // Cambio de get a is (estándar boolean)
    public void setEsPrincipal(boolean esPrincipal) { this.esPrincipal = esPrincipal; }

    public Cliente getCliente() {return cliente;}
    public void setCliente(Cliente cliente) {this.cliente = cliente;}
}
