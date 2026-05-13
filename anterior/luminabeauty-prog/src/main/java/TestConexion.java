import dao.DBManager;
import java.sql.Connection;

public class TestConexion {
    private static Object DBManager;

    public static void main(String[] args) {
        try {
            DBManager DBManager = null;
            Connection con = dao.DBManager.getConnection();
            if (con != null) {
                System.out.println("¡Holi! Ya estamos CONECTADOS con la base de datos lumina_beauty.");
                con.close();
            }
        } catch (Exception e) {
            System.out.println("ERROR DE CONEXIÓN: " + e.getMessage());
            System.out.println("Revisa si tu contraseña es correcta o si MySQL está encendido.");
        }
    }
}