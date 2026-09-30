
public class Ejemplar {

    private final String codigoInventario;
    private final Libro libro;

    public Ejemplar(String codigoInventario, Libro libro) {
        this.codigoInventario = codigoInventario;
        this.libro = libro;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public Libro getLibro() {
        return libro;
    }
}
