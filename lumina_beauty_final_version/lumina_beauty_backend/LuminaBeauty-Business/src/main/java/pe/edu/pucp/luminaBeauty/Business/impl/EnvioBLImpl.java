package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.EnvioBL;
import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.EnvioDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EnvioBLImpl implements EnvioBL {

    private final EnvioDAO envioDAO = new EnvioDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    @Override
    public Envio registrarEnvio(Envio envio) throws Exception {
        try {
            validarDatosEnvio(envio);
            validarRelacionesEnvio(envio);

            if (envio.getEstado() == null || envio.getEstado().trim().isEmpty()) {
                envio.setEstado("PREPARANDO");
            }

            envio.setZona_envio(envio.getZona_envio().trim().toUpperCase());
            envio.setEstado(envio.getEstado().trim().toUpperCase());

            if (envio.getPais_envio() == null || envio.getPais_envio().trim().isEmpty()) {
                envio.setPais_envio("Peru");
            }

            validarZonaEnvio(envio.getZona_envio());
            validarEstadoEnvio(envio.getEstado());
            validarFechasSegunEstado(envio);

            Envio envioPedido = buscarEnvioPorPedidoInterno(envio.getPedido().getId_pedido());

            if (envioPedido != null) {
                throw new Exception("Este pedido ya tiene un envío registrado.");
            }

            Envio envioRegistrado = envioDAO.insertar(envio);
            TransactionContext.commit();

            return envioRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar envío: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio actualizarEnvio(Envio envio) throws Exception {
        try {
            if (envio == null || envio.getId_envio() <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            Envio envioExistente = envioDAO.buscarPorId(envio.getId_envio());

            if (envioExistente == null) {
                throw new Exception("El envío no existe.");
            }

            validarDatosEnvio(envio);
            validarRelacionesEnvio(envio);

            if (envio.getEstado() == null || envio.getEstado().trim().isEmpty()) {
                envio.setEstado(envioExistente.getEstado());
            }

            envio.setZona_envio(envio.getZona_envio().trim().toUpperCase());
            envio.setEstado(envio.getEstado().trim().toUpperCase());

            if (envio.getPais_envio() == null || envio.getPais_envio().trim().isEmpty()) {
                envio.setPais_envio("Peru");
            }

            validarZonaEnvio(envio.getZona_envio());
            validarEstadoEnvio(envio.getEstado());
            validarFechasSegunEstado(envio);

            Envio envioPedido = buscarEnvioPorPedidoInterno(envio.getPedido().getId_pedido());

            if (envioPedido != null && envioPedido.getId_envio() != envio.getId_envio()) {
                throw new Exception("Este pedido ya tiene otro envío registrado.");
            }

            Envio envioActualizado = envioDAO.actualizar(envio);
            TransactionContext.commit();

            return envioActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar envío: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarEnvio(int idEnvio) throws Exception {
        try {
            if (idEnvio <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            Envio envio = envioDAO.buscarPorId(idEnvio);

            if (envio == null) {
                throw new Exception("El envío no existe.");
            }

            envioDAO.eliminar(envio);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar envío: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio buscarEnvio(int idEnvio) throws Exception {
        try {
            if (idEnvio <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            return envioDAO.buscarPorId(idEnvio);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Envio> listarEnvios() throws Exception {
        try {
            return envioDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Envio> listarEnviosPorEstado(String estado) throws Exception {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                throw new Exception("El estado del envío es obligatorio.");
            }

            estado = estado.trim().toUpperCase();
            validarEstadoEnvio(estado);

            ArrayList<Envio> envios = envioDAO.listarTodos();
            ArrayList<Envio> resultado = new ArrayList<>();

            for (Envio envio : envios) {
                if (envio.getEstado() != null &&
                        envio.getEstado().equalsIgnoreCase(estado)) {
                    resultado.add(envio);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Envio> listarEnviosPorZona(String zonaEnvio) throws Exception {
        try {
            if (zonaEnvio == null || zonaEnvio.trim().isEmpty()) {
                throw new Exception("La zona de envío es obligatoria.");
            }

            zonaEnvio = zonaEnvio.trim().toUpperCase();
            validarZonaEnvio(zonaEnvio);

            ArrayList<Envio> envios = envioDAO.listarTodos();
            ArrayList<Envio> resultado = new ArrayList<>();

            for (Envio envio : envios) {
                if (envio.getZona_envio() != null &&
                        envio.getZona_envio().equalsIgnoreCase(zonaEnvio)) {
                    resultado.add(envio);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio buscarEnvioPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            return buscarEnvioPorPedidoInterno(idPedido);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio despacharEnvio(int idEnvio, String numeroSeguimiento) throws Exception {
        try {
            if (idEnvio <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            if (numeroSeguimiento == null || numeroSeguimiento.trim().isEmpty()) {
                throw new Exception("El número de seguimiento es obligatorio.");
            }

            Envio envio = envioDAO.buscarPorId(idEnvio);

            if (envio == null) {
                throw new Exception("El envío no existe.");
            }

            if ("ENTREGADO".equalsIgnoreCase(envio.getEstado()) ||
                    "DEVUELTO".equalsIgnoreCase(envio.getEstado())) {
                throw new Exception("No se puede despachar un envío entregado o devuelto.");
            }

            envio.setEstado("DESPACHADO");
            envio.setNumero_seguimiento(numeroSeguimiento.trim());
            envio.setFecha_envio(LocalDateTime.now());
            envio.setFecha_entrega_real(null);

            Envio envioActualizado = envioDAO.actualizar(envio);
            TransactionContext.commit();

            return envioActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al despachar envío: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio marcarEnvioEnTransito(int idEnvio) throws Exception {
        try {
            if (idEnvio <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            Envio envio = envioDAO.buscarPorId(idEnvio);

            if (envio == null) {
                throw new Exception("El envío no existe.");
            }

            if (!"PREPARANDO".equalsIgnoreCase(envio.getEstado()) &&
                    !"DESPACHADO".equalsIgnoreCase(envio.getEstado())) {
                throw new Exception("Solo se puede pasar a EN_TRANSITO desde PREPARANDO o DESPACHADO.");
            }

            envio.setEstado("EN_TRANSITO");

            if (envio.getFecha_envio() == null) {
                envio.setFecha_envio(LocalDateTime.now());
            }

            envio.setFecha_entrega_real(null);

            Envio envioActualizado = envioDAO.actualizar(envio);
            TransactionContext.commit();

            return envioActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al marcar envío en tránsito: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio marcarEnvioEntregado(int idEnvio) throws Exception {
        try {
            if (idEnvio <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            Envio envio = envioDAO.buscarPorId(idEnvio);

            if (envio == null) {
                throw new Exception("El envío no existe.");
            }

            if ("DEVUELTO".equalsIgnoreCase(envio.getEstado())) {
                throw new Exception("No se puede entregar un envío devuelto.");
            }

            envio.setEstado("ENTREGADO");

            if (envio.getFecha_envio() == null) {
                envio.setFecha_envio(LocalDateTime.now());
            }

            envio.setFecha_entrega_real(LocalDateTime.now());

            Envio envioActualizado = envioDAO.actualizar(envio);
            TransactionContext.commit();

            return envioActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al marcar envío como entregado: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Envio marcarEnvioDevuelto(int idEnvio) throws Exception {
        try {
            if (idEnvio <= 0) {
                throw new Exception("El ID del envío no es válido.");
            }

            Envio envio = envioDAO.buscarPorId(idEnvio);

            if (envio == null) {
                throw new Exception("El envío no existe.");
            }

            if ("ENTREGADO".equalsIgnoreCase(envio.getEstado())) {
                throw new Exception("No se puede devolver un envío ya entregado.");
            }

            envio.setEstado("DEVUELTO");
            envio.setFecha_entrega_real(null);

            Envio envioActualizado = envioDAO.actualizar(envio);
            TransactionContext.commit();

            return envioActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al marcar envío como devuelto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosEnvio(Envio envio) throws Exception {
        if (envio == null) {
            throw new Exception("El envío no puede ser nulo.");
        }

        if (envio.getPedido() == null || envio.getPedido().getId_pedido() <= 0) {
            throw new Exception("Debe asignar un pedido válido.");
        }

        if (envio.getZona_envio() == null || envio.getZona_envio().trim().isEmpty()) {
            throw new Exception("La zona de envío es obligatoria.");
        }

        if (envio.getDireccion_envio() == null || envio.getDireccion_envio().trim().isEmpty()) {
            throw new Exception("La dirección de envío es obligatoria.");
        }

        if (envio.getCiudad_envio() == null || envio.getCiudad_envio().trim().isEmpty()) {
            throw new Exception("La ciudad de envío es obligatoria.");
        }
    }

    private void validarRelacionesEnvio(Envio envio) throws Exception {
        Pedido pedido = pedidoDAO.buscarPorId(envio.getPedido().getId_pedido());

        if (pedido == null) {
            throw new Exception("El pedido asociado al envío no existe.");
        }

        envio.setPedido(pedido);
    }

    private void validarZonaEnvio(String zonaEnvio) throws Exception {
        if (!zonaEnvio.equals("LIMA") &&
                !zonaEnvio.equals("PROVINCIA")) {
            throw new Exception("Zona de envío no válida.");
        }
    }

    private void validarEstadoEnvio(String estado) throws Exception {
        if (!estado.equals("PREPARANDO") &&
                !estado.equals("DESPACHADO") &&
                !estado.equals("EN_TRANSITO") &&
                !estado.equals("ENTREGADO") &&
                !estado.equals("DEVUELTO")) {

            throw new Exception("Estado de envío no válido.");
        }
    }

    private void validarFechasSegunEstado(Envio envio) {
        String estado = envio.getEstado();

        if ("PREPARANDO".equals(estado)) {
            envio.setFecha_envio(null);
            envio.setFecha_entrega_real(null);
        }

        if ("DESPACHADO".equals(estado) || "EN_TRANSITO".equals(estado)) {
            if (envio.getFecha_envio() == null) {
                envio.setFecha_envio(LocalDateTime.now());
            }

            envio.setFecha_entrega_real(null);
        }

        if ("ENTREGADO".equals(estado)) {
            if (envio.getFecha_envio() == null) {
                envio.setFecha_envio(LocalDateTime.now());
            }

            if (envio.getFecha_entrega_real() == null) {
                envio.setFecha_entrega_real(LocalDateTime.now());
            }
        }

        if ("DEVUELTO".equals(estado)) {
            envio.setFecha_entrega_real(null);
        }
    }

    private Envio buscarEnvioPorPedidoInterno(int idPedido) throws Exception {
        ArrayList<Envio> envios = envioDAO.listarTodos();

        for (Envio envio : envios) {
            if (envio.getPedido() != null &&
                    envio.getPedido().getId_pedido() == idPedido) {
                return envio;
            }
        }

        return null;
    }
}
