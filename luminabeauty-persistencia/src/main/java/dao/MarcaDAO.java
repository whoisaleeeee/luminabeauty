package dao;

import luminabeauty.model.Marca;
import java.util.ArrayList;

public interface MarcaDAO {
    // Estas son las funciones que tu Impl "prometió" cumplir
    int insertar(Marca marca);
    ArrayList<Marca> listarTodas();
}