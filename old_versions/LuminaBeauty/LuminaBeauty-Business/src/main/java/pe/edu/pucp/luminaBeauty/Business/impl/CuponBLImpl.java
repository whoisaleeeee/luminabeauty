package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.CuponBL;
import pe.edu.pucp.luminaBeauty.Model.Cupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuponBLImpl implements CuponBL {

    @Override
    public void validarCupon(Cupon cupon) throws Exception {
        if (cupon == null) {
            throw new Exception("El cupón no puede ser nulo.");
        }

        if (!"ACTIVO".equals(cupon.getEstado())) {
            throw new Exception("El cupón no está activo.");
        }

        LocalDateTime hoy = LocalDateTime.now();

        if (hoy.isBefore(cupon.getFechaInicio()) || hoy.isAfter(cupon.getFechaFin())) {
            throw new Exception("El cupón está fuera de fecha.");
        }

        if (cupon.getLimiteUso() != 0 && cupon.getUsosActuales() >= cupon.getLimiteUso()) {
            throw new Exception("El cupón ya alcanzó su límite de uso.");
        }
    }

    @Override
    public BigDecimal aplicarDescuento(Cupon cupon, BigDecimal total) throws Exception {
        validarCupon(cupon);

        if ("PORCENTAJE".equals(cupon.getTipoDescuento())) {
            BigDecimal descuento = total.multiply(cupon.getValorDescuento()).divide(new BigDecimal("100"));
            return total.subtract(descuento);
        }

        if ("MONTO_FIJO".equals(cupon.getTipoDescuento())) {
            return total.subtract(cupon.getValorDescuento());
        }

        throw new Exception("Tipo de descuento no válido.");
    }
}