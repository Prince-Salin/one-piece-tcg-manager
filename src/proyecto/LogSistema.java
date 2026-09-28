package proyecto;

/**
 * Clase que representa un registro de log del sistema.
 * 
 * Esta clase encapsula la información de un evento registrado en el sistema,
 * incluyendo el ID del log, ID del jugador (opcional), acción realizada,
 * detalles del evento y fecha/hora.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class LogSistema {
    
    /** Identificador único del log en la base de datos. */
    private int idLog;
    /** ID del jugador relacionado con el log (puede ser null). */
    private Integer idJugador; // Usamos Integer por si es null (ON DELETE SET NULL)
    /** Tipo de acción registrada. */
    private String accion;
    /** Detalles específicos del evento. */
    private String detalles;
    /** Fecha y hora en que ocurrió el evento. */
    private String fechaHora;

    public LogSistema() {}

    public LogSistema(Integer idJugador, String accion, String detalles) {
        this.idJugador = idJugador;
        this.accion = accion;
        this.detalles = detalles;
    }

    public int getIdLog() { return idLog; }

    public void setIdLog(int idLog) { this.idLog = idLog; }
    
    public Integer getIdJugador() { return idJugador; }

    public void setIdJugador(Integer idJugador) { this.idJugador = idJugador; }

    public String getAccion() { return accion; }

    public void setAccion(String accion) { this.accion = accion; }

    public String getDetalles() { return detalles; }
    
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public String getFechaHora() { return fechaHora; }

    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}