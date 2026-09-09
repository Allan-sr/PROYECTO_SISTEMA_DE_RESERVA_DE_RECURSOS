package una.sistemareservas.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CalendarioRecursosView extends JFrame {

    private JSpinner spinnerFechaRef;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JButton btnHoy;
    private JLabel lblRangoSemana;
    private JTable tablaCalendario;
    private DefaultTableModel modelTabla;

    public CalendarioRecursosView() {
        setTitle("Programación Semanal de Actividades");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel Superior: Control de Navegación de Semanas
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelNavegacion.setBorder(BorderFactory.createTitledBorder("Navegación de Semana"));

        btnAnterior = new JButton("<< Semana Anterior");
        btnHoy = new JButton("Hoy");
        btnSiguiente = new JButton("Semana Siguiente >>");

        panelNavegacion.add(btnAnterior);
        panelNavegacion.add(btnHoy);
        panelNavegacion.add(btnSiguiente);

        panelNavegacion.add(new JLabel("Fecha Ref:"));
        spinnerFechaRef = crearSpinnerFecha(LocalDate.now());
        panelNavegacion.add(spinnerFechaRef);

        lblRangoSemana = new JLabel("", SwingConstants.CENTER);
        lblRangoSemana.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelNavegacion, BorderLayout.NORTH);
        panelNorte.add(lblRangoSemana, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        // Tabla de Calendario (Horas vs Días)
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        modelTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición directa de celdas
            }
        };

        tablaCalendario = new JTable(modelTabla);
        tablaCalendario.setRowHeight(40);
        tablaCalendario.getTableHeader().setReorderingAllowed(false);
        tablaCalendario.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(tablaCalendario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private JSpinner crearSpinnerFecha(LocalDate fecha) {
        Date date = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SpinnerDateModel model = new SpinnerDateModel(date, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        return spinner;
    }

    public LocalDate getFechaSeleccionada() {
        Date date = (Date) spinnerFechaRef.getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setFechaSeleccionada(LocalDate fecha) {
        Date date = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        spinnerFechaRef.setValue(date);
    }

    public void setRangoSemanaTexto(String texto) {
        lblRangoSemana.setText(texto);
    }

    public DefaultTableModel getModelTabla() {
        return modelTabla;
    }

    public JTable getTablaCalendario() {
        return tablaCalendario;
    }

    public JButton getBtnAnterior() { return btnAnterior; }
    public JButton getBtnSiguiente() { return btnSiguiente; }
    public JButton getBtnHoy() { return btnHoy; }
    public JSpinner getSpinnerFechaRef() { return spinnerFechaRef; }
}
