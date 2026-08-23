package una.sistemareservas.modelo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

    private String id;
    private String actividad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Funcionario funcionario;
    private List<Recurso> recursos;
    private EstadoReserva estado;

    public Reserva(String id, String actividad, LocalDate fecha, LocalTime horaInicio,
                   LocalTime horaFin, Funcionario funcionario, List<Recurso> recursos,
                   EstadoReserva estado) {
        this.id = id;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.funcionario = funcionario;
        this.recursos = new ArrayList<>();
        this.estado = EstadoReserva.ACTIVA;
    }

    public String getId() {
        return id;
    }

    public String getActividad() {
        return actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }
    public void agregarRecurso(Recurso recurso) {
        recursos.add(recurso);
    }

}
