package proyecto;

/**
 * Clase que representa una carta del juego One Piece TCG.
 * 
 * Esta clase almacena toda la información básica de una carta,
 * incluyendo sus atributos principales como:
 * - Identificador
 * - Nombre
 * - Tipo
 * - Rareza
 * - Poder base
 * - Descripción
 * - Imagen asociada
 * 
 * También permite indicar si una carta proviene de una evolución
 * mediante el identificador de la carta base.
 * 
 * Esta clase actúa como modelo principal para gestionar cartas
 * dentro del sistema del juego.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class Carta {

    protected int id;
    protected String nombre;

    private String tipo;
    private Rareza rareza;
    private int poderBase;
    private String descripcion;

    private String imagen;

    protected Integer idCartaBase;

    public Carta(int idCarta,
                 String nombre,
                 String tipo,
                 Rareza rareza,
                 int poderBase,
                 String descripcion,
                 String imagen,
                 Integer idCartaBase) {

        this.id = idCarta;
        this.nombre = nombre;
        this.tipo = tipo;
        this.rareza = rareza;
        this.poderBase = poderBase;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.idCartaBase = idCartaBase;
    }

    public int getIdCarta() {
        return id;
    }

    public void setIdCarta(int idCarta) {
        this.id = idCarta;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Rareza getRareza() {
        return rareza;
    }

    public void setRareza(Rareza rareza) {
        this.rareza = rareza;
    }

    public int getPoderBase() {
        return poderBase;
    }

    public void setPoderBase(int poderBase) {
        this.poderBase = poderBase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getIdCartaBase() {
        return idCartaBase;
    }

    public void setIdCartaBase(Integer idCartaBase) {
        this.idCartaBase = idCartaBase;
    }
}
