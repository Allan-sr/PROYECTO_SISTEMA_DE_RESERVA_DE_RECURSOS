package una.sistemareservas.controlador;

import una.sistemareservas.modelo.Categoria;
import una.sistemareservas.modelo.Recurso;
import una.sistemareservas.modelo.Reserva;
import una.sistemareservas.negocio.ReservaService;
import una.sistemareservas.negocio.ResultadoReserva;
import una.sistemareservas.vista.ReservasView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaController {

    private final ReservasView vista;
    private final ReservaService servicio;

    public ReservaController(ReservasView vista) {

        this.vista = vista;
        this.servicio = new ReservaService();

        vista.cargarCategorias(servicio.listarCategoriasDisponibles());
        vista.limpiarFormulario();

        iniciarEventos();
        cargarListado();
    }

    private void iniciarEventos() {

        vista.getBtnReservar().addActionListener(e -> reservar());
        vista.getBtnCancelar().addActionListener(e -> cancelar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarFormulario());
        vista.getBtnExtraerIA().addActionListener(e -> vista.mostrarMensaje("La extracción de datos con IA se agregará más adelante."));
        vista.getBtnImprimir().addActionListener(e -> vista.mostrarMensaje("La generación de reporte en PDF se agregará más adelante."));
    }

    private void reservar() {

        String actividad = vista.getActividad();
        LocalDate fecha = vista.getFecha();
        LocalTime horaInicio = vista.getHoraInicio();
        LocalTime horaFin = vista.getHoraFin();
        List<String> categoriaIds = vista.getCategoriaIdsSeleccionadas();

        ResultadoReserva resultado =
                servicio.crear(actividad, fecha, horaInicio, horaFin, categoriaIds);

        if (resultado.esErrorValidacion()) {

            vista.mostrarError(resultado.getMensajeError());

            return;
        }

        if (resultado.esSinDisponibilidad()) {

            vista.mostrarError(
                    "No hubo disponibilidad para: " + nombresCategorias(resultado.getCategoriasNoDisponibles())
                            + ". Puede modificar la reserva e intentar de nuevo."
            );

            return;
        }

        Reserva reserva = resultado.getReserva();

        vista.mostrarMensaje(
                "Reserva registrada con éxito. Recursos asignados: " + nombresRecursos(reserva)
        );

        vista.limpiarFormulario();

        cargarListado();
    }

    private void cancelar() {

        int fila = vista.getFilaSeleccionada();

        if (fila < 0) {

            vista.mostrarError("Seleccione una reserva de la lista para cancelar.");

            return;
        }

        String id = vista.getValorCelda(fila, 0);

        boolean confirmado =
                vista.confirmar("¿Desea cancelar la reserva " + id + "?");

        if (!confirmado) {
            return;
        }

        ResultadoReserva resultado = servicio.cancelar(id);

        if (resultado.esErrorValidacion()) {

            vista.mostrarError(resultado.getMensajeError());

            return;
        }

        vista.mostrarMensaje("Reserva cancelada correctamente. Los recursos fueron liberados.");

        cargarListado();
    }

    private void cargarListado() {
        vista.cargarTabla(servicio.listarMisReservas());
    }

    private String nombresCategorias(List<Categoria> categorias) {

        StringBuilder texto = new StringBuilder();

        for (Categoria categoria : categorias) {

            if (texto.length() > 0) {
                texto.append(", ");
            }

            texto.append(categoria.getDescripcion());
        }

        return texto.toString();
    }

    private String nombresRecursos(Reserva reserva) {

        StringBuilder texto = new StringBuilder();

        for (Recurso recurso : reserva.getRecursos()) {

            if (texto.length() > 0) {
                texto.append(", ");
            }

            texto.append(recurso.getId());
        }

        return texto.toString();
    }
}
