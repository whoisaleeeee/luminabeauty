package pe.edu.pucp.luminaBeauty.dbManager;

//Clases JDBC
import java.sql.Connection; //Conexión a BD
import java.sql.SQLException; //Errores SQL

//Clase que se encarga de manejar la conexión usada durante una transaccion
public class TransactionContext {
    //Cada hilo tendrá su propia conexion, guardará temporalmente la conexión de la transacción actual
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();


    //Metodo que devuelve la conexion actual
    public static Connection getConnection() throws SQLException {

        //busca si el hilo actual ya tiene una conexión guardada
        Connection conn = connectionHolder.get();
        if (conn == null) {
            conn = DBManager.getInstance().getConnection(); //pide nueva conexion a DBManager
            conn.setAutoCommit(false); //desactiva el guardado automático
            connectionHolder.set(conn); //guarda la conexion en el threadLocal
        }
        return conn;
    }

    //Metodo para confirmar los cambios en la BD
    public static void commit() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            conn.commit(); //guarda definitivamente los cambios si hay conexion
        }
    }

    //Metodo que deshace los cambios hechos en la transacción
    public static void rollback() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                //realiza el rollback
                conn.rollback();
            } catch (SQLException e) {
                //si hubo un error
                e.printStackTrace();
            }
        }
    }

    //Metodo para cerrar la conexion
    public static void close() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                //cierra la conexion con la base de datos
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // CRITICAL: Always remove to prevent memory leaks in thread pools
            //Elimina la conexion del ThreadLocal
            connectionHolder.remove();
        }
    }
}
