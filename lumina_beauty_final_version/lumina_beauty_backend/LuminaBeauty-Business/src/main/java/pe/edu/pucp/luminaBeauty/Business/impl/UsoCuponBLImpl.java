package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.UsoCuponBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.CuponDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.UsoCuponDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.CuponDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.UsoCuponDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.UsoCupon;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class UsoCuponBLImpl implements UsoCuponBL {

    private final UsoCuponDAO usoCuponDAO = new UsoCuponDAOImpl();
    private final CuponDAO cuponDAO = new CuponDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    @Override
    public UsoCupon registrarUsoCupon(UsoCupon usoCupon) throws Exception {
        try {
            validarDatosUsoCupon(usoCupon);
            validarRelacionesUsoCupon(usoCupon);
            validarCuponVigente(usoCupon.getCupon());

            if (buscarUsoPorPedidoInterno(usoCupon.getPedido().getId_pedido()) != null) {
                throw new Exception("Este pedido ya tiene un cupón registrado.");
            }

            if (clienteYaUsoCuponInterno(
                    usoCupon.getCliente().getId_usuario(),
                    usoCupon.getCupon().getId_cupon())) {
                throw new Exception("El cliente ya utilizó este cupón.");
            }

            UsoCupon usoRegistrado = usoCuponDAO.insertar(usoCupon);
            TransactionContext.commit();

            return usoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar uso de cupón: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public UsoCupon actualizarUsoCupon(UsoCupon usoCupon) throws Exception {
        try {
            if (usoCupon == null || usoCupon.getId_uso_cupon() <= 0) {
                throw new Exception("El ID del uso de cupón no es válido.");
            }

            UsoCupon usoExistente = usoCuponDAO.buscarPorId(usoCupon.getId_uso_cupon());

            if (usoExistente == null) {
                throw new Exception("El uso de cupón no existe.");
            }

            validarDatosUsoCupon(usoCupon);
            validarRelacionesUsoCupon(usoCupon);
            validarCuponVigente(usoCupon.getCupon());

            UsoCupon usoPorPedido = buscarUsoPorPedidoInterno(usoCupon.getPedido().getId_pedido());

            if (usoPorPedido != null &&
                    usoPorPedido.getId_uso_cupon() != usoCupon.getId_uso_cupon()) {
                throw new Exception("Este pedido ya tiene otro cupón registrado.");
            }

            ArrayList<UsoCupon> usos = usoCuponDAO.listarTodos();

            for (UsoCupon uso : usos) {
                if (uso.getId_uso_cupon() != usoCupon.getId_uso_cupon() &&
                        uso.getCliente() != null &&
                        uso.getCupon() != null &&
                        uso.getCliente().getId_usuario() == usoCupon.getCliente().getId_usuario() &&
                        uso.getCupon().getId_cupon() == usoCupon.getCupon().getId_cupon()) {

                    throw new Exception("El cliente ya utilizó este cupón.");
                }
            }

            UsoCupon usoActualizado = usoCuponDAO.actualizar(usoCupon);
            TransactionContext.commit();

            return usoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar uso de cupón: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarUsoCupon(int idUsoCupon) throws Exception {
        try {
            if (idUsoCupon <= 0) {
                throw new Exception("El ID del uso de cupón no es válido.");
            }

            UsoCupon usoCupon = usoCuponDAO.buscarPorId(idUsoCupon);

            if (usoCupon == null) {
                throw new Exception("El uso de cupón no existe.");
            }

            usoCuponDAO.eliminar(usoCupon);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar uso de cupón: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public UsoCupon buscarUsoCupon(int idUsoCupon) throws Exception {
        try {
            if (idUsoCupon <= 0) {
                throw new Exception("El ID del uso de cupón no es válido.");
            }

            return usoCuponDAO.buscarPorId(idUsoCupon);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<UsoCupon> listarUsosCupon() throws Exception {
        try {
            return usoCuponDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<UsoCupon> listarUsosPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<UsoCupon> usos = usoCuponDAO.listarTodos();
            ArrayList<UsoCupon> resultado = new ArrayList<>();

            for (UsoCupon uso : usos) {
                if (uso.getCliente() != null &&
                        uso.getCliente().getId_usuario() == idCliente) {
                    resultado.add(uso);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<UsoCupon> listarUsosPorCupon(int idCupon) throws Exception {
        try {
            if (idCupon <= 0) {
                throw new Exception("El ID del cupón no es válido.");
            }

            Cupon cupon = cuponDAO.buscarPorId(idCupon);

            if (cupon == null) {
                throw new Exception("El cupón no existe.");
            }

            ArrayList<UsoCupon> usos = usoCuponDAO.listarTodos();
            ArrayList<UsoCupon> resultado = new ArrayList<>();

            for (UsoCupon uso : usos) {
                if (uso.getCupon() != null &&
                        uso.getCupon().getId_cupon() == idCupon) {
                    resultado.add(uso);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public UsoCupon buscarUsoPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            return buscarUsoPorPedidoInterno(idPedido);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public boolean clienteYaUsoCupon(int idCliente, int idCupon) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            if (idCupon <= 0) {
                throw new Exception("El ID del cupón no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            Cupon cupon = cuponDAO.buscarPorId(idCupon);

            if (cupon == null) {
                throw new Exception("El cupón no existe.");
            }

            return clienteYaUsoCuponInterno(idCliente, idCupon);

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosUsoCupon(UsoCupon usoCupon) throws Exception {
        if (usoCupon == null) {
            throw new Exception("El uso de cupón no puede ser nulo.");
        }

        if (usoCupon.getCupon() == null ||
                usoCupon.getCupon().getId_cupon() <= 0) {
            throw new Exception("Debe asignar un cupón válido.");
        }

        if (usoCupon.getCliente() == null ||
                usoCupon.getCliente().getId_usuario() <= 0) {
            throw new Exception("Debe asignar un cliente válido.");
        }

        if (usoCupon.getPedido() == null ||
                usoCupon.getPedido().getId_pedido() <= 0) {
            throw new Exception("Debe asignar un pedido válido.");
        }
    }

    private void validarRelacionesUsoCupon(UsoCupon usoCupon) throws Exception {
        Cupon cupon = cuponDAO.buscarPorId(usoCupon.getCupon().getId_cupon());

        if (cupon == null) {
            throw new Exception("El cupón no existe.");
        }

        Cliente cliente = clienteDAO.buscarPorId(usoCupon.getCliente().getId_usuario());

        if (cliente == null) {
            throw new Exception("El cliente no existe.");
        }

        Pedido pedido = pedidoDAO.buscarPorId(usoCupon.getPedido().getId_pedido());

        if (pedido == null) {
            throw new Exception("El pedido no existe.");
        }

        if (pedido.getCliente() == null ||
                pedido.getCliente().getId_usuario() != cliente.getId_usuario()) {
            throw new Exception("El pedido no pertenece al cliente indicado.");
        }

        if (pedido.getCupon() == null ||
                pedido.getCupon().getId_cupon() != cupon.getId_cupon()) {
            throw new Exception("El cupón no está asociado al pedido indicado.");
        }

        usoCupon.setCupon(cupon);
        usoCupon.setCliente(cliente);
        usoCupon.setPedido(pedido);
    }

    private void validarCuponVigente(Cupon cupon) throws Exception {
        if (cupon.getEstado() != 1) {
            throw new Exception("El cupón no está activo.");
        }

        LocalDateTime ahora = LocalDateTime.now();

        if (cupon.getFecha_inicio() == null || cupon.getFecha_fin() == null) {
            throw new Exception("El cupón no tiene fechas válidas.");
        }

        if (ahora.isBefore(cupon.getFecha_inicio())) {
            throw new Exception("El cupón aún no está vigente.");
        }

        if (ahora.isAfter(cupon.getFecha_fin())) {
            throw new Exception("El cupón ya venció.");
        }
    }

    private UsoCupon buscarUsoPorPedidoInterno(int idPedido) throws Exception {
        ArrayList<UsoCupon> usos = usoCuponDAO.listarTodos();

        for (UsoCupon uso : usos) {
            if (uso.getPedido() != null &&
                    uso.getPedido().getId_pedido() == idPedido) {
                return uso;
            }
        }

        return null;
    }

    private boolean clienteYaUsoCuponInterno(int idCliente, int idCupon) throws Exception {
        ArrayList<UsoCupon> usos = usoCuponDAO.listarTodos();

        for (UsoCupon uso : usos) {
            if (uso.getCliente() != null &&
                    uso.getCupon() != null &&
                    uso.getCliente().getId_usuario() == idCliente &&
                    uso.getCupon().getId_cupon() == idCupon) {
                return true;
            }
        }

        return false;
    }
}
