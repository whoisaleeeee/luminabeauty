package pe.edu.pucp.luminaBeauty.dbManager;

//sirve para leer el archivo db.properties
import java.io.IOException; //permite abrir el archivo como flujo de datos
import java.io.InputStream; //Controla errores al leer el archivo

//Clases JDBC
import java.sql.Connection; //para la conexión abierta con la BD
import java.sql.DriverManager;//Crea la conexión usando la URL, usuario y contraseña
import java.sql.SQLException;//Captura errores de conexión SQL


import java.util.Properties;//Pemite leer el archivo .properties

//Clase para centralizar la conexión a la BD
public class DBManager {

    //Sirve para guardar la única instancia de DBManager
    private static DBManager instance;
    private Properties properties;

    //Datos necesarios para conectarse a MySQL
    private final String url;
    private final String user;
    private final String password;

    //Guarda el nombre del archivo donde están las credenciales
    private final String DB_CREDENTIALS_FILE = "db.properties";

    //Constructor privado para que nadie desde fuera pueda hacer new, y solo pueda usar el getInstance()
    private DBManager() {
        //objeto para cargar los datos del archivo properties
        properties = new Properties();

        //Inicia bloque donde puede ocurrir un error
        try{
            //busca el archivo de propiedades dentro del ClassPath, si el archivo existe lo abre como inputStream
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_CREDENTIALS_FILE);
            //Carga el contenido del archivo en el objeto properties
            properties.load(inputStream);
        }
        //captura errores al leer el archivo
        catch(IOException ex){
            //Error en tiempo de ejecución
            throw new RuntimeException("Error when loading properties file: " + ex.getMessage());
        }

        //Leer los datos del archivo
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String database = properties.getProperty("database");

        //construccion de la url jdbc
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    //Metodo para obtener la única instancia del DB Manager
    public static DBManager getInstance(){
        if(instance == null)
            instance = new DBManager();
        return instance;
    }


    //Metodo que devuelve la conexión a la BD
    public Connection getConnection() {
        try {
            //se abre la conexión a MYSQL
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
