package pe.edu.pucp.luminaBeauty.DAO;

import java.util.ArrayList;

public interface BaseDAO <E, ID>{
    E insertar(E e) throws Exception;
    void eliminar(E e) throws Exception;
    E buscarPorId(ID id) throws Exception;
    E actualizar(E e) throws Exception;
    ArrayList<E> listarTodos() throws Exception;

}
