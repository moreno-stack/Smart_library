
public class Bibliotecario extends Usuario {

    private final String codigoEmpleado;
    private final String turno;

    public Bibliotecario(String identificacion, String nombre, String correo,
            String codigoEmpleado, String turno) {
        super(identificacion, nombre, correo);
        this.codigoEmpleado = codigoEmpleado;
        this.turno = turno;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public String getTurno() {
        return turno;
    }
}
