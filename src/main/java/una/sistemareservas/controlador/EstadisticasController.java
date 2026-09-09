package una.sistemareservas.controlador;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import una.sistemareservas.modelo.Recurso;
import una.sistemareservas.modelo.Reserva;

import una.sistemareservas.negocio.RecursoService;
import una.sistemareservas.negocio.ReservaService;
import una.sistemareservas.vista.EstadisticasView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticasController {

    private final EstadisticasView vista;
    private final ReservaService reservaService;

    public EstadisticasController(EstadisticasView vista) {
        this.vista = vista;
        this.reservaService = new ReservaService();
        iniciarEventos();
        generarReporte(); // Carga el gráfico por defecto al abrir
    }

    private void iniciarEventos() {
        vista.getBtnGenerar().addActionListener(e -> generarReporte());
    }

    private void generarReporte() {
        LocalDate inicio = vista.getFechaInicio();
        LocalDate fin = vista.getFechaFin();

        if (inicio.isAfter(fin)) {
            vista.mostrarMensaje("La fecha de inicio no puede ser posterior a la fecha fin.");
            return;
        }

        List<Reserva> todas = reservaService.listar(); // Obtiene todas las reservas guardadas

        // Filtrar reservas por el rango de fechas
        List<Reserva> filtradas = todas.stream()
                .filter(r -> r.getFecha() != null &&
                        !r.getFecha().isBefore(inicio) &&
                        !r.getFecha().isAfter(fin))
                .collect(Collectors.toList());

        if (filtradas.isEmpty()) {
            vista.mostrarMensaje("No se encontraron reservas registrados en el rango de fechas seleccionado.");
        }

        if (vista.getTipoGraficoSeleccionado() == 0) {
            generarGraficoCategorias(filtradas);
        } else {
            generarGraficoEstados(filtradas);
        }
    }

    private void generarGraficoCategorias(List<Reserva> reservas) {
        Map<String, Integer> conteoCategorias = new HashMap<>();
        RecursoService recursoService = new RecursoService();

        for (Reserva r : reservas) {
            if (r.getRecursos() != null && !r.getRecursos().isEmpty()) {
                for (Recurso rec : r.getRecursos()) {
                    if (rec.getCategoria() != null) {
                        String catNombre = rec.getCategoria().getDescripcion();
                        conteoCategorias.put(catNombre, conteoCategorias.getOrDefault(catNombre, 0) + 1);
                    }
                }
            } else if (r.getRecursoIds() != null) {
                for (String idRecurso : r.getRecursoIds()) {
                    Recurso rec = recursoService.buscarPorId(idRecurso);
                    if (rec != null && rec.getCategoria() != null) {
                        String catNombre = rec.getCategoria().getDescripcion();
                        conteoCategorias.put(catNombre, conteoCategorias.getOrDefault(catNombre, 0) + 1);
                    }
                }
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        conteoCategorias.forEach((categoria, cantidad) -> {
            dataset.addValue(cantidad, "Solicitudes", categoria);
        });

        JFreeChart chart = ChartFactory.createBarChart(
                "Categorías de Recursos Más Solicitadas",
                "Categoría",
                "Cantidad de Solicitudes",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // Personalización Estética del Gráfico
        chart.setBackgroundPaint(java.awt.Color.WHITE);
        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new java.awt.Color(245, 247, 250));
        plot.setDomainGridlinePaint(java.awt.Color.WHITE);
        plot.setRangeGridlinePaint(new java.awt.Color(220, 224, 230));

        // Forzar el eje Y a mostrar solo números enteros (1, 2, 3...)
        org.jfree.chart.axis.NumberAxis rangeAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits());
        rangeAxis.setLowerBound(0); // Evita valores negativos

        // Ajustar el color y ancho de las barras
        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new java.awt.Color(41, 128, 185)); // Azul corporativo
        renderer.setMaximumBarWidth(0.15); // Evita que una sola barra ocupe toda la pantalla

        vista.mostrarGrafico(chart);
    }

    private void generarGraficoEstados(List<Reserva> reservas) {
        Map<String, Integer> conteoEstados = new HashMap<>();

        for (Reserva r : reservas) {
            String estado = (r.getEstado() != null) ? r.getEstado().toString() : "DESCONOCIDO";
            conteoEstados.put(estado, conteoEstados.getOrDefault(estado, 0) + 1);
        }

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        conteoEstados.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Reservas por Estado",
                dataset,
                true, true, false
        );

        // Personalización Estética del Pastel
        chart.setBackgroundPaint(java.awt.Color.WHITE);
        org.jfree.chart.plot.PiePlot<?> plot = (org.jfree.chart.plot.PiePlot<?>) chart.getPlot();
        plot.setBackgroundPaint(new java.awt.Color(245, 247, 250));
        plot.setSectionPaint("ACTIVA", new java.awt.Color(46, 204, 113));    // Verde
        plot.setSectionPaint("CANCELADA", new java.awt.Color(231, 76, 60)); // Rojo

        vista.mostrarGrafico(chart);
    }
}