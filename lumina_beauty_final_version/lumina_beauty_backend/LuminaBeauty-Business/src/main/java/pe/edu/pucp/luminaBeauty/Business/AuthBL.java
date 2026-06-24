
package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Usuario;

public interface AuthBL {

    Usuario iniciarSesion(String correo, String contrasena) throws Exception;

    Cliente iniciarSesionCliente(String correo, String contrasena) throws Exception;

    Empleado iniciarSesionEmpleado(String correo, String contrasena) throws Exception;

    boolean validarCredenciales(String correo, String contrasena) throws Exception;
}

