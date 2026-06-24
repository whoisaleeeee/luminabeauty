package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Cupon;

import java.util.ArrayList;

public interface CuponBL {

    Cupon registrarCupon(Cupon cupon) throws Exception;

    Cupon actualizarCupon(Cupon cupon) throws Exception;

    void eliminarCupon(int idCupon) throws Exception;

    Cupon buscarCupon(int idCupon) throws Exception;

    ArrayList<Cupon> listarCupones() throws Exception;

    ArrayList<Cupon> listarCuponesActivos() throws Exception;

    Cupon buscarCuponPorCodigo(String codigo) throws Exception;

    boolean validarCupon(String codigo) throws Exception;

    Cupon aplicarCupon(String codigo) throws Exception;
}

