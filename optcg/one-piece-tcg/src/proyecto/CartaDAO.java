package proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión del catálogo de Cartas.
 *
 * Proporciona métodos para listar todas las cartas y buscar
 * cartas específicas por su nombre o su identificador.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class CartaDAO extends DAOGenerico {

    /**
     * Construye el DAO de Cartas.
     * * @param conexion La conexión activa a la base de datos.
     */
    public CartaDAO(Connection conexion) {
        super(conexion);
    }

    /**
     * Obtiene todas las cartas registradas en el sistema.
     * * @return Lista de objetos Carta.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public List<Carta> listar() throws SQLException {
        validarConexion();
        List<Carta> lista = new ArrayList<>();
        String sql = "SELECT * FROM carta";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCarta(rs));
            }
        }
        return lista;
    }

    /**
     * Busca una carta específica utilizando su nombre (ignorando mayúsculas).
     * * @param nombre El nombre exacto de la carta a buscar.
     * @return El objeto Carta si se encuentra, o null si no existe.
     * @throws SQLException Si ocurre un error al ejecutar la consulta.
     */
    public Carta buscarCartaPorNombre(String nombre) throws SQLException {
        validarConexion();
        String sql = "SELECT * FROM carta WHERE UPPER(nombre) = UPPER(?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCarta(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca una carta específica utilizando su ID numérico.
     * * @param id El identificador único de la carta.
     * @return El objeto Carta correspondiente, o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    public Carta buscarCartaPorId(int id) throws SQLException {
        validarConexion();
        String sql = "SELECT * FROM carta WHERE id_carta = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCarta(rs);
                }
            }
        }
        return null;
    }

    /**
     * Método auxiliar para transformar un ResultSet en un objeto Carta.
     * * @param rs El ResultSet posicionado en la fila actual.
     * @return Un objeto Carta instanciado con los datos de la base de datos.
     * @throws SQLException Si hay un error leyendo las columnas.
     */
    private Carta mapearCarta(ResultSet rs) throws SQLException {
        return new Carta(
                rs.getInt("id_carta"),
                rs.getString("nombre"),
                rs.getString("tipo"),
                Rareza.valueOf(rs.getString("rareza").trim()),
                rs.getInt("poder_base"),
                rs.getString("descripcion"),
                rs.getString("imagen"),
                (Integer) rs.getObject("id_carta_base")
        );
    }
}