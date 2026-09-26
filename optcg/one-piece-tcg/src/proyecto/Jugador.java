package proyecto;

/**
 * Clase que representa a un jugador en el sistema de cartas One Piece TCG.
 * 
 * Esta clase encapsula la información de un jugador, incluyendo su ID,
 * nombre, puntos acumulados y cantidad de berries. Proporciona constructores
 * para diferentes escenarios y métodos para acceder a los atributos.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class Jugador {
    /** Identificador único de la entidad. */
    protected int id;
    /** Nombre de la entidad. */
    protected String nombre;
    /** Puntos acumulados por el jugador en partidas. */
    private int puntos;
    /** Cantidad de berries que posee el jugador. */
    private int berries;

    /**
     * Constructor para crear un nuevo jugador sin ID (se asignará en BD).
     * 
     * @param nombre Nombre del jugador
     *
     */

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public Jugador(int idJugador, String nombre, int puntos, int berries) {
        this.id = idJugador;
        this.nombre = nombre;
        this.puntos = puntos;
        this.berries = berries;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public int getPuntos() { 
        return puntos; 
    }

    public void setPuntos(int puntos) { 
        this.puntos = puntos; 
    }

    public int getBerries() {
         return berries;
     }

    public void setBerries(int berries) {
         this.berries = berries; 
        }
}
