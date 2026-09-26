package proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de gestionar los Jugadores y sus interacciones.
 *
 * Permite realizar el CRUD completo de jugadores, así como llamar a los 
 * procedimientos almacenados para comprar y vender cartas.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class JugadorDAO extends DAOGenerico {

    /**
     * Construye el DAO de Jugadores.
     * * @param conexion La conexión activa a la base de datos.
     */
    public JugadorDAO(Connection conexion) {
        super(conexion);
    }

    /**
     * Inserta un nuevo jugador en la base de datos (solo por nombre).
     * * @param jugador Objeto Jugador con el nombre a registrar.
     * @throws SQLException Si ocurre un error al insertar.
     */
    public void insertar(Jugador jugador) throws SQLException {
        validarConexion();
        String sql = "INSERT INTO jugador (nombre) VALUES (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, jugador.getNombre());
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza los datos de un jugador existente.
     * * @param jugador El objeto Jugador con los datos actualizados.
     * @throws SQLException Si ocurre un error al actualizar.
     */
    public void actualizar(Jugador jugador) throws SQLException {
        validarConexion();
        String sql = "UPDATE jugador SET nombre = ?, puntos = ?, berries = ? WHERE id_jugador = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, jugador.getNombre());
            ps.setInt(2, jugador.getPuntos());
            ps.setInt(3, jugador.getBerries());
            ps.setInt(4, jugador.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina permanentemente a un jugador del sistema.
     * * @param id El identificador del jugador a borrar.
     * @throws SQLException Si ocurre un error en la eliminación.
     */
    public void eliminar(int id) throws SQLException {
        validarConexion();
        String sql = "DELETE FROM jugador WHERE id_jugador = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un jugador por su nombre.
     * * @param nombre El nombre del jugador a buscar.
     * @return El objeto Jugador, o null si no se encuentra.
     * @throws SQLException Si ocurre un error de consulta.
     */
    public Jugador buscarPorNombre(String nombre) throws SQLException {
        validarConexion();
        String sql = "SELECT * FROM jugador WHERE UPPER(nombre) = UPPER(?) LIMIT 1";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Jugador(
                            rs.getInt("id_jugador"),
                            rs.getString("nombre"),
                            rs.getInt("puntos"),
                            rs.getInt("berries")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Obtiene el listado de los 10 mejores jugadores ordenados por puntos.
     * * @return Lista de hasta 10 objetos Jugador.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public List<Jugador> top10() throws SQLException {
        validarConexion();
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT * FROM jugador ORDER BY puntos DESC LIMIT 10";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Jugador(
                        rs.getInt("id_jugador"),
                        rs.getString("nombre"),
                        rs.getInt("puntos"),
                        rs.getInt("berries")
                ));
            }
        }
        return lista;
    }

    /**
     * Obtiene todas las cartas que posee actualmente un jugador en su inventario.
     * * @param nombreJugador El nombre del jugador propietario.
     * @return Lista de cartas pertenecientes al jugador.
     * @throws SQLException Si ocurre un error al cruzar las tablas.
     */
    public List<Carta> obtenerCartasDeJugador(String nombreJugador) throws SQLException {
        validarConexion();
        List<Carta> cartas = new ArrayList<>();
        String sql = "SELECT c.* FROM carta c " +
                     "JOIN inventario i ON c.id_carta = i.id_carta " +
                     "JOIN jugador j ON i.id_jugador = j.id_jugador " +
                     "WHERE UPPER(j.nombre) = UPPER(?)";
                     
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreJugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cartas.add(new Carta(
                            rs.getInt("id_carta"),
                            rs.getString("nombre"),
                            rs.getString("tipo"),
                            Rareza.valueOf(rs.getString("rareza").trim()),
                            rs.getInt("poder_base"),
                            rs.getString("descripcion"),
                            rs.getString("imagen"),
                            (Integer) rs.getObject("id_carta_base")
                    ));
                }
            }
        }
        return cartas;
    }

    /**
     * Ejecuta el procedimiento almacenado para comprar una carta.
     * * @param nombreJugador El nombre del jugador que realiza la compra.
     * @param nombreCarta El nombre de la carta a comprar.
     * @throws SQLException Si el procedimiento falla (ej. sin saldo, sin stock).
     */
    public void comprarCarta(String nombreJugador, String nombreCarta) throws SQLException {
        validarConexion();
        String sql = "{CALL sp_comprar_carta(?, ?)}";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setString(1, nombreJugador);
            cs.setString(2, nombreCarta);
            cs.execute();
        }
    }

    /**
     * Ejecuta el procedimiento almacenado para vender una carta.
     * * @param nombreJugador El nombre del jugador que realiza la venta.
     * @param nombreCarta El nombre de la carta a vender.
     * @throws SQLException Si el procedimiento falla (ej. no tiene la carta).
     */
    public void venderCarta(String nombreJugador, String nombreCarta) throws SQLException {
        validarConexion();
        String sql = "{CALL sp_vender_carta(?, ?)}";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setString(1, nombreJugador);
            cs.setString(2, nombreCarta);
            cs.execute();
        }
    }
}