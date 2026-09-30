
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Libro {

    private final String isbn;
    private final String titulo;
    private final List<Ejemplar> ejemplares = new ArrayList<>();

    public Libro(String isbn, String titulo) {
        this.isbn = isbn;
        this.titulo = titulo;
    }

    public void agregarEjemplar(Ejemplar ejemplar) {
        if (ejemplar.getLibro() != this) {
            throw new IllegalArgumentException("El ejemplar pertenece a otro libro.");
        }
        ejemplares.add(ejemplar);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Ejemplar> getEjemplares() {
        return Collections.unmodifiableList(ejemplares);
    }
}
