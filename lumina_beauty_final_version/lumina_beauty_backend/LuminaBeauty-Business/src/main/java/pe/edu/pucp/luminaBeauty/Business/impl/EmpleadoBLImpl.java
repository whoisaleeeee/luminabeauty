
package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.EmpleadoBL;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class EmpleadoBLImpl implements EmpleadoBL {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public Empleado registrarEmpleado(Empleado empleado) throws Exception {
        try {
            validarDatosEmpleado(empleado, true);

            empleado.setTipo_usuario("EMPLEADO");
            empleado.setEstado(1);

            if (empleado.getRol() == null || empleado.getRol().trim().isEmpty()) {
                empleado.setRol("SOPORTE");
            }

            validarRol(empleado.getRol());

            Empleado empleadoRegistrado = empleadoDAO.insertar(empleado);
            TransactionContext.commit();

            return empleadoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar empleado: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Empleado actualizarEmpleado(Empleado empleado) throws Exception {
        try {
            if (empleado == null || empleado.getId_usuario() <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleadoExistente = empleadoDAO.buscarPorId(empleado.getId_usuario());

            if (empleadoExistente == null) {
                throw new Exception("El empleado no existe.");
            }

            validarDatosEmpleado(empleado, false);

            empleado.setTipo_usuario("EMPLEADO");

            if (empleado.getEstado() != 0 && empleado.getEstado() != 1) {
                empleado.setEstado(1);
            }

            if (empleado.getContrasena_hash() == null || empleado.getContrasena_hash().trim().isEmpty()) {
                empleado.setContrasena_hash(empleadoExistente.getContrasena_hash());
            }

            if (empleado.getRol() == null || empleado.getRol().trim().isEmpty()) {
                empleado.setRol(empleadoExistente.getRol());
            }

            validarRol(empleado.getRol());

            Empleado empleadoActualizado = empleadoDAO.actualizar(empleado);
            TransactionContext.commit();

            return empleadoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar empleado: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado no existe.");
            }

            empleadoDAO.eliminar(empleado);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar empleado: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Empleado buscarEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            return empleadoDAO.buscarPorId(idEmpleado);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Empleado> listarEmpleados() throws Exception {
        try {
            return empleadoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Empleado> listarEmpleadosPorRol(String rol) throws Exception {
        try {
            ArrayList<Empleado> empleados = empleadoDAO.listarTodos();
            ArrayList<Empleado> resultado = new ArrayList<>();

            if (rol == null || rol.trim().isEmpty()) {
                return empleados;
            }

            String rolBuscado = rol.trim().toUpperCase();

            validarRol(rolBuscado);

            for (Empleado empleado : empleados) {
                if (empleado.getRol() != null &&
                        empleado.getRol().equalsIgnoreCase(rolBuscado)) {
                    resultado.add(empleado);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosEmpleado(Empleado empleado, boolean validarContrasena) throws Exception {
        if (empleado == null) {
            throw new Exception("El empleado no puede ser nulo.");
        }

        if (empleado.getNombres() == null || empleado.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres del empleado son obligatorios.");
        }

        if (empleado.getApellidos() == null || empleado.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos del empleado son obligatorios.");
        }

        if (empleado.getCorreo() == null || empleado.getCorreo().trim().isEmpty()) {
            throw new Exception("El correo del empleado es obligatorio.");
        }

        if (!empleado.getCorreo().contains("@")) {
            throw new Exception("El correo del empleado no tiene un formato válido.");
        }

        if (!empleado.getCorreo().trim().toLowerCase().endsWith("@lumina.com")) {
            throw new Exception("El correo del empleado debe ser institucional: @lumina.com.");
        }

        if (validarContrasena) {
            if (empleado.getContrasena_hash() == null || empleado.getContrasena_hash().trim().isEmpty()) {
                throw new Exception("La contraseña del empleado es obligatoria.");
            }
        }

        if (empleado.getDni() != null && !empleado.getDni().trim().isEmpty()) {
            if (empleado.getDni().trim().length() < 8) {
                throw new Exception("El DNI debe tener al menos 8 caracteres.");
            }
        }

        if (empleado.getEstado() != 0 && empleado.getEstado() != 1) {
            empleado.setEstado(1);
        }
    }

    private void validarRol(String rol) throws Exception {
        if (rol == null ||
                (!rol.equals("ADMIN") &&
                        !rol.equals("VENDEDOR") &&
                        !rol.equals("SOPORTE") &&
                        !rol.equals("BODEGUERO"))) {

            throw new Exception("Rol de empleado no válido.");
        }
    }
}

