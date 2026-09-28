package proyecto;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase utilitaria para registrar eventos en el sistema de logs.
 * 
 * Esta clase proporciona un método estático para registrar acciones
 * en la base de datos a través del LogSistemaDAO. Maneja errores
 * de SQL e imprime confirmaciones en consola.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class Logger {
    
    /**
     * Registra un evento en el sistema de logs.
     * 
     * Este método intenta insertar un nuevo log en la base de datos
     * usando el DAO proporcionado. Si falla, imprime el error.
     * Siempre imprime un mensaje de confirmación en consola.
     * 
     * @param conn La conexión a la base de datos
     * @param dao El DAO para manejar logs
     * @param idJugador ID del jugador (puede ser null para eventos del sistema)
     * @param accion El tipo de acción realizada
     * @param detalles Detalles adicionales del evento
     */
    public static void log(Connection conn, LogSistemaDAO dao, Integer idJugador, String accion, String detalles) {
        try {
            dao.insertar(conn, idJugador, accion, detalles);
            
            System.out.println("[LOG REGISTRADO]: " + accion + " - " + detalles);
        } catch (SQLException e) {
            System.err.println("Error al guardar log: " + e.getMessage());
        }
    }
}