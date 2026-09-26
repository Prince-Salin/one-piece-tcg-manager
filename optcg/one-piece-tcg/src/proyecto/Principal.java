package proyecto;

import java.sql.Connection;

/**
 * Clase principal del sistema que inicia la aplicación.
 *
 * Esta clase es el punto de entrada de la aplicación y lanza
 * la interfaz gráfica del sistema de cartas One Piece TCG.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class Principal {

    /**
     * Punto de entrada principal de la aplicación.
     *
     * - Modo gráfico (Swing)
     *
     * El modo gráfico lanza la ventana principal de la aplicación.
     */
    public static void main(String[] args) {
        try {
            // Instanciamos tu clase Conexion y obtenemos el objeto Connection
            Conexion conexionDB = new Conexion();
            Connection miConexion = conexionDB.mySQLConnect();

            if (miConexion != null) {
                // Le pasamos la conexión al constructor de tu VentanaPrincipal
                VentanaPrincipal ventana = new VentanaPrincipal(miConexion);
                ventana.setVisible(true); // Hace visible la ventana al usuario
            } else {
                System.err.println("Error: No se pudo establecer la conexión con la base de datos.");
            }

        } catch (Exception e) {
            System.err.println("No se pudo iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}