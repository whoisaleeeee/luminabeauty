package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.luminaBeauty.Business.AuthBL;
import pe.edu.pucp.luminaBeauty.Business.impl.AuthBLImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Usuario;

@Path("AuthRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthRS {

    private AuthBL authBL;

    public AuthRS() {
        this.authBL = new AuthBLImpl();
    }

    @POST
    @Path("login")
    public Usuario iniciarSesion(LoginRequest request) {
        Usuario resultado = null;

        try {
            resultado = authBL.iniciarSesion(request.getCorreo(), request.getContrasena());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @POST
    @Path("loginCliente")
    public Cliente iniciarSesionCliente(LoginRequest request) {
        Cliente resultado = null;

        try {
            resultado = authBL.iniciarSesionCliente(request.getCorreo(), request.getContrasena());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @POST
    @Path("loginEmpleado")
    public Empleado iniciarSesionEmpleado(LoginRequest request) {
        Empleado resultado = null;

        try {
            resultado = authBL.iniciarSesionEmpleado(request.getCorreo(), request.getContrasena());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    @POST
    @Path("validarCredenciales")
    public int validarCredenciales(LoginRequest request) {
        int resultado = 0;

        try {
            boolean valido = authBL.validarCredenciales(request.getCorreo(), request.getContrasena());

            if (valido) {
                resultado = 1;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    public static class LoginRequest {

        private String correo;
        private String contrasena;

        public LoginRequest() {
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }
}

