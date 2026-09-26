package proyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase responsable de gestionar la conexión a la base de datos MySQL.
 * 
 * Esta clase proporciona un método para establecer una conexión con la base de datos
 * 'one_piece_tcg' en localhost. Maneja la carga del driver JDBC y el manejo de errores.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class Conexion {
    
    /**
     * Establece y devuelve una conexión a la base de datos MySQL.
     * 
     * Este método configura los parámetros de conexión (URL, usuario, contraseña),
     * carga el driver JDBC de MySQL, intenta conectar y devuelve la conexión.
     * Si la conexión falla, imprime el error y devuelve null.
     * 
     * @return La conexión a la base de datos o null si falla
     */
    public Connection mySQLConnect() {
        String url = "jdbc:mysql://127.0.0.1:3306/one_piece_tcg";
        String usuario = "root";
        String password = "";

        Connection conexion = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(url, usuario, password);
            
            System.out.println("Conexión exitosa a la base de datos");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }

        return conexion;
    }
}
