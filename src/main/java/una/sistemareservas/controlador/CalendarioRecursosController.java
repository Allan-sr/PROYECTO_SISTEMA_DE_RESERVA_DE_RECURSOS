package una.sistemareservas.controlador;

import una.sistemareservas.modelo.Recurso;
import una.sistemareservas.modelo.Reserva;
import una.sistemareservas.negocio.CalendarioRecursosService;
import una.sistemareservas.vista.CalendarioRecursosView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarioRecursosController {
    private final CalendarioRecursosView vista;
    private final CalendarioRecursosService servicio;
    public CalendarioRecursosController(CalendarioRecursosView vista) {
        this.vista = vista;
        this.servicio = new CalendarioRecursosService();
        vista.cargarCategorias(servicio.listarCategorias());
        iniciarEventos();
    }

    private void iniciarEventos() {
        vista.getBtnConsultar().addActionListener(e -> consultar());
    }
    private void consultar() {
        String categoriaId = vista.getCategoriaId();
        LocalDate fecha = vista.getFecha();
        if (categoriaId.isEmpty()) {
            vista.mostrarError("Debe seleccionar una categoría.");
            return;
        }

        List<Recurso> recursos = servicio.listarRecursosPorCategoria(categoriaId);
        List<Reserva> reservas = servicio.listarReservasPorFecha(fecha);
        List<LocalTime> horas = servicio.obtenerHorasDelDia();
        vista.cargarCalendario(recursos, horas, reservas);
        if (recursos.isEmpty()) {
            vista.mostrarMensaje("No existen recursos para la categoría seleccionada.");
        }
    }
}



