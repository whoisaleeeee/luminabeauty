package dao;

import luminabeauty.model.CarroDeCompras;

import java.util.ArrayList;

public interface CarroDeComprasDAO {
    int insertar(CarroDeCompras carro);
    ArrayList<CarroDeCompras> listarTodos();
    CarroDeCompras buscarPorId(int id);
    int actualizar(CarroDeCompras carro);
    int eliminar(int id);
}
