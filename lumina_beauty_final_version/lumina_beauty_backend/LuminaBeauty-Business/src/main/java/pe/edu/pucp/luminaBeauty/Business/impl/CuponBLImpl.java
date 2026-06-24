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

        if (hoy.isBefore(cupon.getFecha_inicio()) || hoy.isAfter(cupon.getFecha_fin())) {
            throw new Exception("El cupón está fuera de fecha.");
        }

        if (cupon.getLimite_uso() != 0 && cupon.getCantidad_usos() >= cupon.getLimite_uso()) {
            throw new Exception("El cupón ya alcanzó su límite de uso.");
        }
        cupon.setCantidad_usos(cupon.getCantidad_usos()+1);
    }

    @Override
    public BigDecimal aplicarDescuento(Cupon cupon, BigDecimal total) throws Exception {
        validarCupon(cupon);

        if ("PORCENTAJE".equals(cupon.getTipo_descuento())) {
            BigDecimal descuento = total.multiply(cupon.getValor_descuento()).divide(new BigDecimal("100"));
            return total.subtract(descuento);
        }

        if ("MONTO_FIJO".equals(cupon.getTipo_descuento())) {
            return total.subtract(cupon.getValor_descuento());
        }

        throw new Exception("Tipo de descuento no válido.");
    }
}