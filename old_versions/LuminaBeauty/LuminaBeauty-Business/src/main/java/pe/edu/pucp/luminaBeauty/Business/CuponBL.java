package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Cupon;

import java.math.BigDecimal;

public interface CuponBL {
    void validarCupon(Cupon cupon) throws Exception;
    BigDecimal aplicarDescuento(Cupon cupon, BigDecimal total) throws Exception;
}