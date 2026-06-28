package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.CuponBL;
import pe.edu.pucp.luminaBeauty.DAO.CuponDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.CuponDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CuponBLImpl implements CuponBL {

    private final CuponDAO cuponDAO = new CuponDAOImpl();

    @Override
    public Cupon registrarCupon(Cupon cupon) throws Exception {
        try {
            validarDatosCupon(cupon);

            if (cupon.getEstado() != 0 && cupon.getEstado() != 1) {
                cupon.setEstado(1);
            }

            cupon.setCodigo(cupon.getCodigo().trim().toUpperCase());
            cupon.setTipo_descuento(cupon.getTipo_descuento().trim().toUpperCase());

            Cupon cuponExistente = buscarCuponPorCodigoInterno(cupon.getCodigo());

            if (cuponExistente != null) {
                throw new Exception("Ya existe un cupón con ese código.");
            }

            Cupon cuponRegistrado = cuponDAO.insertar(cupon);
            TransactionContext.commit();

            return cuponRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar cupón: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cupon actualizarCupon(Cupon cupon) throws Exception {
        try {
            if (cupon == null || cupon.getId_cupon() <= 0) {
                throw new Exception("El ID del cupón no es válido.");
            }

            Cupon cuponExistente = cuponDAO.buscarPorId(cupon.getId_cupon());

            if (cuponExistente == null) {
                throw new Exception("El cupón no existe.");
            }

            validarDatosCupon(cupon);

            cupon.setCodigo(cupon.getCodigo().trim().toUpperCase());
            cupon.setTipo_descuento(cupon.getTipo_descuento().trim().toUpperCase());

            Cupon cuponMismoCodigo = buscarCuponPorCodigoInterno(cupon.getCodigo());

            if (cuponMismoCodigo != null &&
                    cuponMismoCodigo.getId_cupon() != cupon.getId_cupon()) {
                throw new Exception("Ya existe otro cupón con ese código.");
            }

            Cupon cuponActualizado = cuponDAO.actualizar(cupon);
            TransactionContext.commit();

            return cuponActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar cupón: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarCupon(int idCupon) throws Exception {
        try {
            if (idCupon <= 0) {
                throw new Exception("El ID del cupón no es válido.");
            }

            Cupon cupon = cuponDAO.buscarPorId(idCupon);

            if (cupon == null) {
                throw new Exception("El cupón no existe.");
            }

            cuponDAO.eliminar(cupon);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar cupón: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cupon buscarCupon(int idCupon) throws Exception {
        try {
            if (idCupon <= 0) {
                throw new Exception("El ID del cupón no es válido.");
            }

            return cuponDAO.buscarPorId(idCupon);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Cupon> listarCupones() throws Exception {
        try {
            return cuponDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Cupon> listarCuponesActivos() throws Exception {
        try {
            ArrayList<Cupon> cupones = cuponDAO.listarTodos();
            ArrayList<Cupon> resultado = new ArrayList<>();

            LocalDateTime ahora = LocalDateTime.now();

            for (Cupon cupon : cupones) {
                if (cupon.getEstado() == 1 &&
                        cupon.getFecha_inicio() != null &&
                        cupon.getFecha_fin() != null &&
                        !ahora.isBefore(cupon.getFecha_inicio()) &&
                        !ahora.isAfter(cupon.getFecha_fin())) {

                    resultado.add(cupon);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cupon buscarCuponPorCodigo(String codigo) throws Exception {
        try {
            if (codigo == null || codigo.trim().isEmpty()) {
                throw new Exception("El código del cupón es obligatorio.");
            }

            return buscarCuponPorCodigoInterno(codigo.trim().toUpperCase());

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public boolean validarCupon(String codigo) throws Exception {
        try {
            Cupon cupon = buscarCuponPorCodigoInternoValidacion(codigo);

            if (cupon == null) {
                return false;
            }

            validarCuponActivo(cupon);

            return true;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Cupon aplicarCupon(String codigo) throws Exception {
        try {
            Cupon cupon = buscarCuponPorCodigoInternoValidacion(codigo);

            if (cupon == null) {
                throw new Exception("El cupón no existe.");
            }

            validarCuponActivo(cupon);

            return cupon;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosCupon(Cupon cupon) throws Exception {
        if (cupon == null) {
            throw new Exception("El cupón no puede ser nulo.");
        }

        if (cupon.getCodigo() == null || cupon.getCodigo().trim().isEmpty()) {
            throw new Exception("El código del cupón es obligatorio.");
        }

        if (cupon.getTipo_descuento() == null || cupon.getTipo_descuento().trim().isEmpty()) {
            throw new Exception("El tipo de descuento es obligatorio.");
        }

        String tipoDescuento = cupon.getTipo_descuento().trim().toUpperCase();

        if (!tipoDescuento.equals("PORCENTAJE") &&
                !tipoDescuento.equals("MONTO_FIJO") &&
                !tipoDescuento.equals("ENVIO_GRATIS")) {
            throw new Exception("Tipo de descuento no válido. Use: PORCENTAJE, MONTO_FIJO o ENVIO_GRATIS.");
        }

        // Envio gratis no requiere valor de descuento
        if (!tipoDescuento.equals("ENVIO_GRATIS")) {
            if (cupon.getValor_descuento() == null ||
                    cupon.getValor_descuento().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("El valor del descuento debe ser mayor a cero.");
            }

            if (tipoDescuento.equals("PORCENTAJE") &&
                    cupon.getValor_descuento().compareTo(new BigDecimal("100")) > 0) {
                throw new Exception("El descuento porcentual no puede ser mayor a 100.");
            }
        } else {
            // Para envio gratis, forzar valor 0
            cupon.setValor_descuento(BigDecimal.ZERO);
        }

        if (cupon.getFecha_inicio() == null) {
            throw new Exception("La fecha de inicio es obligatoria.");
        }

        if (cupon.getFecha_fin() == null) {
            throw new Exception("La fecha de fin es obligatoria.");
        }

        if (!cupon.getFecha_fin().isAfter(cupon.getFecha_inicio())) {
            throw new Exception("La fecha de fin debe ser posterior a la fecha de inicio.");
        }

        if (cupon.getLimite_uso() != null && cupon.getLimite_uso() <= 0) {
            throw new Exception("El límite de uso debe ser mayor a cero.");
        }

        if (cupon.getEstado() != 0 && cupon.getEstado() != 1) {
            cupon.setEstado(1);
        }
    }

    private void validarCuponActivo(Cupon cupon) throws Exception {
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

    private Cupon buscarCuponPorCodigoInterno(String codigo) throws Exception {
        ArrayList<Cupon> cupones = cuponDAO.listarTodos();

        for (Cupon cupon : cupones) {
            if (cupon.getCodigo() != null &&
                    cupon.getCodigo().equalsIgnoreCase(codigo)) {
                return cupon;
            }
        }

        return null;
    }

    private Cupon buscarCuponPorCodigoInternoValidacion(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new Exception("El código del cupón es obligatorio.");
        }

        return buscarCuponPorCodigoInterno(codigo.trim().toUpperCase());
    }
}

