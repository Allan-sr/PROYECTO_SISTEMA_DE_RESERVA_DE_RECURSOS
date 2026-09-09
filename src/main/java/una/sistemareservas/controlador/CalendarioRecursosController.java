package una.sistemareservas.controlador;

import una.sistemareservas.modelo.Reserva;
import una.sistemareservas.negocio.ReservaService;
import una.sistemareservas.vista.CalendarioRecursosView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class CalendarioRecursosController {

    private final CalendarioRecursosView vista;
    private final ReservaService reservaService;
    private LocalDate lunesActual;

    public CalendarioRecursosController(CalendarioRecursosView vista) {
        this.vista = vista;
        this.reservaService = new ReservaService();
        iniciarEventos();
        cargarSemana(LocalDate.now());
    }

    private void iniciarEventos() {
        vista.getBtnAnterior().addActionListener(e -> cargarSemana(lunesActual.minusWeeks(1)));
        vista.getBtnSiguiente().addActionListener(e -> cargarSemana(lunesActual.plusWeeks(1)));
        vista.getBtnHoy().addActionListener(e -> cargarSemana(LocalDate.now()));

        vista.getSpinnerFechaRef().addChangeListener(e -> {
            LocalDate fecha = vista.getFechaSeleccionada();
            if (!fecha.equals(lunesActual)) {
                cargarSemana(fecha);
            }
        });
    }

    private void cargarSemana(LocalDate fechaReferencia) {
        // Calcular el Lunes y Domingo de la semana actual
        lunesActual = fechaReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingoActual = lunesActual.plusDays(6);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        vista.setRangoSemanaTexto("Semana del " + lunesActual.format(fmt) + " al " + domingoActual.format(fmt));

        // Actualizar los nombres de las columnas con la fecha exacta de cada día
        DefaultTableModel model = vista.getModelTabla();
        model.setRowCount(0);

        String[] headers = new String[8];
        headers[0] = "Hora";
        for (int i = 0; i < 7; i++) {
            LocalDate dia = lunesActual.plusDays(i);
            String nombreDia = traducirDia(dia.getDayOfWeek());
            headers[i + 1] = nombreDia + " " + dia.format(DateTimeFormatter.ofPattern("dd/MM"));
        }
        model.setColumnIdentifiers(headers);

        // Cargar todas las reservas del sistema
        List<Reserva> reservas = reservaService.listar();

        // Generar filas por cada hora (de 07:00 a 19:00)
        for (int hora = 7; hora <= 19; hora++) {
            Object[] fila = new Object[8];
            LocalTime horaBloque = LocalTime.of(hora, 0);
            fila[0] = String.format("%02d:00 - %02d:00", hora, hora + 1);

            for (int i = 0; i < 7; i++) {
                LocalDate diaColumna = lunesActual.plusDays(i);
                StringBuilder contenidoCelda = new StringBuilder();

                for (Reserva r : reservas) {
                    if (r.getFecha() != null && r.getFecha().equals(diaColumna)) {
                        // Verificar si la reserva coincide en este bloque de hora
                        if (r.getHoraInicio() != null && r.getHoraFin() != null) {
                            if (!r.getHoraInicio().isAfter(horaBloque) && r.getHoraFin().isAfter(horaBloque)) {
                                if (contenidoCelda.length() > 0) contenidoCelda.append(" | ");
                                contenidoCelda.append(r.getActividad());
                            }
                        }
                    }
                }
                fila[i + 1] = contenidoCelda.length() > 0 ? contenidoCelda.toString() : "";
            }
            model.addRow(fila);
        }

        // --- MEJORA VISUAL PARA CELDAS OCUPADAS ---
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(SwingConstants.CENTER);

                if (value != null && !value.toString().isEmpty() && column > 0) {
                    c.setBackground(new Color(217, 237, 247)); // Azul claro destacado
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    setToolTipText(value.toString());
                } else {
                    c.setBackground(Color.WHITE);
                    setToolTipText(null);
                }

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                }

                return c;
            }
        };

        // Aplicar el renderizador a las columnas de días (Lunes a Domingo)
        JTable tabla = vista.getTablaCalendario();
        for (int i = 1; i <= 7; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private String traducirDia(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "";
        }
    }
}


