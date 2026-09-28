package proyecto;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase abstracta padre para manejar la conexión a la base de datos de los DAOs.
 *
 * Centraliza el objeto Connection para que las clases hijas (CartaDAO, 
 * TiendaDAO, JugadorDAO) puedan reutilizarlo sin duplicar código.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public abstract class DAOGenerico {

    protected Connection conexion;

    /**
     * Construye un nuevo DAO genérico.
     * * @param conexion La conexión activa a la base de datos MySQL.
     */
    public DAOGenerico(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Valida que la conexión actual esté abierta y lista para usarse.
     * * @throws SQLException Si la conexión es nula o está cerrada.
     */
    protected void validarConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            throw new SQLException("Conexión a la base de datos no disponible");
        }
    }
}