package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.AuthBL;
import pe.edu.pucp.luminaBeauty.Business.Util.PasswordUtil;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Usuario;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class AuthBLImpl implements AuthBL {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public Usuario iniciarSesion(String correo, String contrasena) throws Exception {
        try {
            validarDatosLogin(correo, contrasena);

            Cliente cliente = buscarClientePorCredenciales(correo, contrasena);
            if (cliente != null) {
                return cliente;
            }

            Empleado empleado = buscarEmpleadoPorCredenciales(correo, contrasena);
            if (empleado != null) {
                return empleado;
            }

            throw new Exception("Correo o contraseña incorrectos.");
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cliente iniciarSesionCliente(String correo, String contrasena) throws Exception {
        try {
            validarDatosLogin(correo, contrasena);

            Cliente cliente = buscarClientePorCredenciales(correo, contrasena);
            if (cliente == null) {
                throw new Exception("Correo o contraseña incorrectos para cliente.");
            }

            return cliente;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Empleado iniciarSesionEmpleado(String correo, String contrasena) throws Exception {
        try {
            validarDatosLogin(correo, contrasena);

            Empleado empleado = buscarEmpleadoPorCredenciales(correo, contrasena);
            if (empleado == null) {
                throw new Exception("Correo o contraseña incorrectos para empleado.");
            }

            return empleado;
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public boolean validarCredenciales(String correo, String contrasena) throws Exception {
        try {
            validarDatosLogin(correo, contrasena);

            return buscarClientePorCredenciales(correo, contrasena) != null
                    || buscarEmpleadoPorCredenciales(correo, contrasena) != null;
        } finally {
            TransactionContext.close();
        }
    }

    private Cliente buscarClientePorCredenciales(String correo, String contrasena) throws Exception {
        ArrayList<Cliente> clientes = clienteDAO.listarTodos();

        for (Cliente cliente : clientes) {
            if (cliente.getCorreo() != null
                    && cliente.getContrasena_hash() != null
                    && cliente.getCorreo().equalsIgnoreCase(correo.trim())
                    && cliente.getEstado() == 1
                    && PasswordUtil.verify(contrasena, cliente.getContrasena_hash())) {
                return cliente;
            }
        }

        return null;
    }

    private Empleado buscarEmpleadoPorCredenciales(
            String correo,
            String contrasena) throws Exception {

        ArrayList<Empleado> empleados = empleadoDAO.listarTodos();

        for (Empleado empleado : empleados) {
            if (empleado.getCorreo() != null
                    && empleado.getContrasena_hash() != null
                    && empleado.getCorreo().equalsIgnoreCase(correo.trim())
                    && empleado.getEstado() == 1
                    && empleado.getContrasena_hash().equals(contrasena)) {
                return empleado;
            }
        }

        return null;
    }

    private void validarDatosLogin(String correo, String contrasena) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo es obligatorio.");
        }

        if (!correo.contains("@")) {
            throw new Exception("El correo no tiene un formato válido.");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
    }
}
