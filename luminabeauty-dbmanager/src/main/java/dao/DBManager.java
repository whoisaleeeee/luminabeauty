package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager {

    public static Connection getConnection() throws SQLException {
        ResourceBundle db = ResourceBundle.getBundle("db");

        String host = db.getString("db.host");
        int port = Integer.parseInt(db.getString("db.puerto"));
        String esquema = db.getString("db.esquema");
        String usuario = db.getString("db.usuario");
        String password = db.getString("db.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + esquema;

        return DriverManager.getConnection(url, usuario, password);
    }
}

