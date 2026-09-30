
public class Estudiante extends Usuario implements Notificable {

    private final String codigoEstudiantil;
    private final String programaAcademico;

    public Estudiante(String identificacion, String nombre, String correo,
            String codigoEstudiantil, String programaAcademico) {
        super(identificacion, nombre, correo);
        this.codigoEstudiantil = codigoEstudiantil;
        this.programaAcademico = programaAcademico;
    }

    public String getCodigoEstudiantil() {
        return codigoEstudiantil;
    }

    public String getProgramaAcademico() {
        return programaAcademico;
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("[NOTIFICACIÓN a " + getNombre() + " <" + getCorreo() + ">] " + mensaje);
    }
}
