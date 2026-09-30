import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Estudiante estudiante = new Estudiante("1085", "Ana Torres", "ana@univ.edu.co", "E2024001", "Ingeniería de Sistemas");
        Libro libro = new Libro("978-0134494166", "Clean Architecture");
        Ejemplar ejemplar = new Ejemplar("EJ-001", libro);
        libro.agregarEjemplar(ejemplar);
        Prestamo prestamo = new Prestamo(estudiante, ejemplar, LocalDate.of(2026, 10, 8));

        System.out.println("=== PRUEBA 1: renovación válida ===");
        prestamo.renovar(LocalDate.of(2026, 10, 15));
        estudiante.notificar("Su préstamo fue renovado.");
        System.out.println("Nueva fecha prevista: " + prestamo.getFechaPrevistaDevolucion());
        System.out.println("Cantidad de renovaciones: " + prestamo.getRenovaciones().size());
        Renovacion r = prestamo.getRenovaciones().get(0);
        System.out.println("Fecha anterior conservada: " + r.getFechaAnterior());

        System.out.println("\n=== PRUEBA 2: renovación inválida (fecha anterior a la vigente) ===");
        try {
            prestamo.renovar(LocalDate.of(2026, 10, 10));
            System.out.println("ERROR: debió rechazarse");
        } catch (IllegalArgumentException e) {
            System.out.println("Rechazada correctamente: " + e.getMessage());
        }
        System.out.println("Fecha prevista sigue en: " + prestamo.getFechaPrevistaDevolucion());
        System.out.println("Cantidad de renovaciones sigue en: " + prestamo.getRenovaciones().size());

        System.out.println("\n=== PRUEBA 3: renovación inválida (fecha igual) ===");
        try {
            prestamo.renovar(LocalDate.of(2026, 10, 15));
        } catch (IllegalArgumentException e) {
            System.out.println("Rechazada correctamente: " + e.getMessage());
        }
    }
}
