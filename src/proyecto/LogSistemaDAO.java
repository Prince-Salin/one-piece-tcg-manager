package proyecto;

import java.sql.*;
import java.util.*;

/**
 * Clase Data Access Object (DAO) para gestionar logs del sistema en la base de datos.
 * 
 * Esta clase proporciona métodos para insertar nuevos logs y obtener
 * los últimos logs registrados, ordenados por fecha descendente.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class LogSistemaDAO {

    /**
     * Inserta un nuevo log en la base de datos.
     * 
     * Este método inserta un registro en la tabla 'logs_sistema' con
     * el ID del jugador (puede ser null), la acción y los detalles.
     * La fecha se establece automáticamente en la base de datos.
     * 
     * @param conn La conexión a la base de datos
     * @param idJugador ID del jugador (puede ser null)
     * @param accion La acción realizada
     * @param detalles Detalles del evento
     * @throws SQLException Si ocurre un error en la inserción
     */
    public void insertar(Connection conn, Integer idJugador, String accion, String detalles) throws SQLException {
        String sql = "INSERT INTO logs_sistema (id_jugador, accion, detalles) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            if (idJugador == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, idJugador);
            }

            ps.setString(2, accion);
            ps.setString(3, detalles);

            ps.executeUpdate();
        }
    }

    /**
     * Obtiene los últimos 10 logs del sistema ordenados por fecha descendente.
     * 
     * @param conn La conexión a la base de datos
     * @return Lista de los últimos 10 logs
     * @throws SQLException Si ocurre un error en la consulta
     */
    public List<LogSistema> obtenerUltimosLogs(Connection conn) throws SQLException {
        List<LogSistema> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM logs_sistema ORDER BY fecha_hora DESC LIMIT 10";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                LogSistema log = new LogSistema();
                
                log.setIdJugador(rs.getInt("id_jugador"));
                log.setAccion(rs.getString("accion"));
                log.setDetalles(rs.getString("detalles"));
                
                lista.add(log);
            }
        }
        
        return lista;
    }
}