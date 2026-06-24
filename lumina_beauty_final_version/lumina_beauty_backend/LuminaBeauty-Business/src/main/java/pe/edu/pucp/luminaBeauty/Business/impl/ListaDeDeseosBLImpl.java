
package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ListaDeDeseosBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.ListaDeDeseosDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ListaDeDeseosDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.ListaDeDeseos;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class ListaDeDeseosBLImpl implements ListaDeDeseosBL {

    private final ListaDeDeseosDAO listaDeDeseosDAO = new ListaDeDeseosDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();

    @Override
    public ListaDeDeseos registrarListaDeDeseos(ListaDeDeseos listaDeDeseos) throws Exception {
        try {
            validarDatosLista(listaDeDeseos);

            Cliente cliente = clienteDAO.buscarPorId(listaDeDeseos.getCliente().getId_usuario());

            if (cliente == null) {
                throw new Exception("El cliente asociado a la lista de deseos no existe.");
            }

            ListaDeDeseos listaRegistrada = listaDeDeseosDAO.insertar(listaDeDeseos);
            TransactionContext.commit();

            return listaRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar lista de deseos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ListaDeDeseos actualizarListaDeDeseos(ListaDeDeseos listaDeDeseos) throws Exception {
        try {
            if (listaDeDeseos == null || listaDeDeseos.getId_lista_deseos() <= 0) {
                throw new Exception("El ID de la lista de deseos no es válido.");
            }

            ListaDeDeseos listaExistente = listaDeDeseosDAO.buscarPorId(listaDeDeseos.getId_lista_deseos());

            if (listaExistente == null) {
                throw new Exception("La lista de deseos no existe.");
            }

            validarDatosLista(listaDeDeseos);

            Cliente cliente = clienteDAO.buscarPorId(listaDeDeseos.getCliente().getId_usuario());

            if (cliente == null) {
                throw new Exception("El cliente asociado a la lista de deseos no existe.");
            }

            ListaDeDeseos listaActualizada = listaDeDeseosDAO.actualizar(listaDeDeseos);
            TransactionContext.commit();

            return listaActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar lista de deseos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarListaDeDeseos(int idListaDeDeseos) throws Exception {
        try {
            if (idListaDeDeseos <= 0) {
                throw new Exception("El ID de la lista de deseos no es válido.");
            }

            ListaDeDeseos listaDeDeseos = listaDeDeseosDAO.buscarPorId(idListaDeDeseos);

            if (listaDeDeseos == null) {
                throw new Exception("La lista de deseos no existe.");
            }

            listaDeDeseosDAO.eliminar(listaDeDeseos);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar lista de deseos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ListaDeDeseos buscarListaDeDeseos(int idListaDeDeseos) throws Exception {
        try {
            if (idListaDeDeseos <= 0) {
                throw new Exception("El ID de la lista de deseos no es válido.");
            }

            return listaDeDeseosDAO.buscarPorId(idListaDeDeseos);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<ListaDeDeseos> listarListasDeDeseos() throws Exception {
        try {
            return listaDeDeseosDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<ListaDeDeseos> listarListasPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<ListaDeDeseos> listas = listaDeDeseosDAO.listarTodos();
            ArrayList<ListaDeDeseos> resultado = new ArrayList<>();

            for (ListaDeDeseos lista : listas) {
                if (lista.getCliente() != null &&
                        lista.getCliente().getId_usuario() == idCliente) {
                    resultado.add(lista);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<ListaDeDeseos> buscarListasPorNombre(String nombre) throws Exception {
        try {
            ArrayList<ListaDeDeseos> listas = listaDeDeseosDAO.listarTodos();
            ArrayList<ListaDeDeseos> resultado = new ArrayList<>();

            if (nombre == null || nombre.trim().isEmpty()) {
                return listas;
            }

            String nombreBuscado = nombre.trim().toLowerCase();

            for (ListaDeDeseos lista : listas) {
                if (lista.getNombre() != null &&
                        lista.getNombre().toLowerCase().contains(nombreBuscado)) {
                    resultado.add(lista);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosLista(ListaDeDeseos listaDeDeseos) throws Exception {
        if (listaDeDeseos == null) {
            throw new Exception("La lista de deseos no puede ser nula.");
        }

        if (listaDeDeseos.getCliente() == null ||
                listaDeDeseos.getCliente().getId_usuario() <= 0) {
            throw new Exception("Debe asignar un cliente válido.");
        }

        if (listaDeDeseos.getNombre() == null ||
                listaDeDeseos.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la lista de deseos es obligatorio.");
        }
    }
}
