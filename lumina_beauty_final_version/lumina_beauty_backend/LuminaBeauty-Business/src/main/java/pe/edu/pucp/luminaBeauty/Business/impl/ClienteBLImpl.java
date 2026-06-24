package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ClienteBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class ClienteBLImpl implements ClienteBL {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();

    @Override
    public Cliente registrarCliente(Cliente cliente) throws Exception {
        try {
            validarDatosCliente(cliente, true);

            cliente.setTipo_usuario("CLIENTE");
            cliente.setEstado(1);

            if (cliente.getPuntos_fidelidad() < 0) {
                cliente.setPuntos_fidelidad(0);
            }

            if (cliente.getNivel_cliente() == null || cliente.getNivel_cliente().trim().isEmpty()) {
                cliente.setNivel_cliente(calcularNivelCliente(cliente.getPuntos_fidelidad()));
            }

            validarNivelCliente(cliente.getNivel_cliente());

            Cliente clienteRegistrado = clienteDAO.insertar(cliente);
            TransactionContext.commit();

            return clienteRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar cliente: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cliente actualizarCliente(Cliente cliente) throws Exception {
        try {
            if (cliente == null || cliente.getId_usuario() <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente clienteExistente = clienteDAO.buscarPorId(cliente.getId_usuario());

            if (clienteExistente == null) {
                throw new Exception("El cliente no existe.");
            }

            validarDatosCliente(cliente, false);

            cliente.setTipo_usuario("CLIENTE");

            if (cliente.getEstado() != 0 && cliente.getEstado() != 1) {
                cliente.setEstado(1);
            }

            if (cliente.getContrasena_hash() == null || cliente.getContrasena_hash().trim().isEmpty()) {
                cliente.setContrasena_hash(clienteExistente.getContrasena_hash());
            }

            if (cliente.getPuntos_fidelidad() < 0) {
                throw new Exception("Los puntos de fidelidad no pueden ser negativos.");
            }

            if (cliente.getNivel_cliente() == null || cliente.getNivel_cliente().trim().isEmpty()) {
                cliente.setNivel_cliente(calcularNivelCliente(cliente.getPuntos_fidelidad()));
            }

            validarNivelCliente(cliente.getNivel_cliente());

            Cliente clienteActualizado = clienteDAO.actualizar(cliente);
            TransactionContext.commit();

            return clienteActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar cliente: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            clienteDAO.eliminar(cliente);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar cliente: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cliente buscarCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            return clienteDAO.buscarPorId(idCliente);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Cliente> listarClientes() throws Exception {
        try {
            return clienteDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void sumarPuntos(int idCliente, int puntos) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            if (puntos <= 0) {
                throw new Exception("Los puntos a sumar deben ser mayores a cero.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            int nuevosPuntos = cliente.getPuntos_fidelidad() + puntos;

            cliente.setPuntos_fidelidad(nuevosPuntos);
            cliente.setNivel_cliente(calcularNivelCliente(nuevosPuntos));

            clienteDAO.actualizar(cliente);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al sumar puntos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void restarPuntos(int idCliente, int puntos) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            if (puntos <= 0) {
                throw new Exception("Los puntos a restar deben ser mayores a cero.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            if (cliente.getPuntos_fidelidad() < puntos) {
                throw new Exception("El cliente no tiene puntos suficientes.");
            }

            int nuevosPuntos = cliente.getPuntos_fidelidad() - puntos;

            cliente.setPuntos_fidelidad(nuevosPuntos);
            cliente.setNivel_cliente(calcularNivelCliente(nuevosPuntos));

            clienteDAO.actualizar(cliente);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al restar puntos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosCliente(Cliente cliente, boolean validarContrasena) throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente no puede ser nulo.");
        }

        if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres del cliente son obligatorios.");
        }

        if (cliente.getApellidos() == null || cliente.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos del cliente son obligatorios.");
        }

        if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty()) {
            throw new Exception("El correo del cliente es obligatorio.");
        }

        if (!cliente.getCorreo().contains("@")) {
            throw new Exception("El correo del cliente no tiene un formato válido.");
        }

        if (cliente.getCorreo().trim().toLowerCase().endsWith("@lumina.com")) {
            throw new Exception("El correo @lumina.com está reservado para empleados.");
        }

        if (validarContrasena) {
            if (cliente.getContrasena_hash() == null || cliente.getContrasena_hash().trim().isEmpty()) {
                throw new Exception("La contraseña del cliente es obligatoria.");
            }
        }

        if (cliente.getDni() != null && !cliente.getDni().trim().isEmpty()) {
            if (cliente.getDni().trim().length() < 8) {
                throw new Exception("El DNI debe tener al menos 8 caracteres.");
            }
        }
    }

    private String calcularNivelCliente(int puntos) {
        if (puntos >= 1000) {
            return "PLATINO";
        } else if (puntos >= 500) {
            return "ORO";
        } else if (puntos >= 200) {
            return "PLATA";
        } else {
            return "BRONCE";
        }
    }

    private void validarNivelCliente(String nivelCliente) throws Exception {
        if (nivelCliente == null ||
                (!nivelCliente.equals("BRONCE") &&
                        !nivelCliente.equals("PLATA") &&
                        !nivelCliente.equals("ORO") &&
                        !nivelCliente.equals("PLATINO"))) {

            throw new Exception("Nivel de cliente no válido.");
        }
    }
}

