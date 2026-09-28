package proyecto;

/**
 * Clase que representa una oferta en la tienda de cartas.
 * 
 * Esta clase encapsula la información de una oferta de venta de cartas
 * en la tienda, incluyendo el ID de la oferta, ID de la carta, precio
 * en berries y stock disponible.
 * 
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class Tienda {
    /** Identificador único de la entidad. */
    protected int id;
    /** Nombre de la entidad. */
    protected String nombre;
    /** ID de la carta que se ofrece en venta. */
    private int idCarta;
    /** Precio de la carta en berries. */
    private int precioBerries;
    /** Cantidad de unidades disponibles en stock. */
    private int stock;

    public Tienda(int idOferta, int idCarta, int precioBerries, int stock) {
        this.id = idOferta;
        this.nombre = "Oferta #" + idOferta;
        this.idCarta = idCarta;
        this.precioBerries = precioBerries;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.nombre = "Oferta #" + id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCarta() { return idCarta; }

    public void setIdCarta(int idCarta) { this.idCarta = idCarta; }

    public int getPrecioBerries() { return precioBerries; }

    public void setPrecioBerries(int precioBerries) { this.precioBerries = precioBerries; }

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

}
