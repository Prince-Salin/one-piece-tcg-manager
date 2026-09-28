package proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de la tienda de cartas.
 *
 * Se encarga exclusivamente de consultar las ofertas activas
 * en la tabla tienda_cartas.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class TiendaDAO extends DAOGenerico {

    /**
     * Construye el DAO de la tienda.
     * * @param conexion La conexión activa a la base de datos.
     */
    public TiendaDAO(Connection conexion) {
        super(conexion);
    }

    /**
     * Obtiene una lista con todas las ofertas actuales de la tienda.
     * * @return Una lista de objetos Tienda con el stock y precio.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    public List<Tienda> listar() throws SQLException {
        validarConexion();
        List<Tienda> resultados = new ArrayList<>();
        String sql = "SELECT * FROM tienda_cartas";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Tienda tienda = new Tienda(
                    rs.getInt("id_oferta"),
                    rs.getInt("id_carta"),
                    rs.getInt("precio_berries"),
                    rs.getInt("stock")
                );
                resultados.add(tienda);
            }
            return resultados;
        }
    }
}