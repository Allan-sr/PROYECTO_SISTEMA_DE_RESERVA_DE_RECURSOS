package una.sistemareservas.vista;

import una.sistemareservas.modelo.Categoria;
import una.sistemareservas.modelo.Recurso;
import una.sistemareservas.modelo.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarioRecursosView extends JFrame {
    private JComboBox<Categoria> cmbCategoria;
    private JSpinner spnFecha;
    private JButton btnConsultar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public CalendarioRecursosView() {
        setTitle("Calendario de Recursos");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.add(construirPanelFiltro(), BorderLayout.NORTH);
        panelPrincipal.add(construirPanelCalendario(), BorderLayout.CENTER);
        add(panelPrincipal);
    }

    private JPanel construirPanelFiltro() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Consulta"));
        panel.add(new JLabel("Categoría:"));

        cmbCategoria = new JComboBox<>();
        cmbCategoria.setPreferredSize(new Dimension(200, 25));
        panel.add(cmbCategoria);

        panel.add(new JLabel("Fecha:"));
        SpinnerDateModel modeloFecha = new SpinnerDateModel();
        spnFecha = new JSpinner(modeloFecha);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
        spnFecha.setEditor(editor);
        spnFecha.setPreferredSize(new Dimension(110, 25));
        panel.add(spnFecha);

        btnConsultar = new JButton("Consultar");
        panel.add(btnConsultar);
        return panel;
    }

    private JPanel construirPanelCalendario() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Disponibilidad de Recursos"));
        String[] columnas = new String[25];
        columnas[0] = "Recurso";
        for (int i = 0; i < 24; i++) {
            columnas[i + 1] = String.format("%02d:00", i);
        }
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(35);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(180);

        for (int i = 1; i < 25; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(65);
        }
        tabla.setDefaultRenderer(Object.class, new CalendarioCellRenderer()
        );

        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void cargarCategorias(List<Categoria> categorias) {
        cmbCategoria.removeAllItems();
        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }

    public String getCategoriaId() {
        Categoria seleccionada = (Categoria) cmbCategoria.getSelectedItem();
        if (seleccionada == null) {
            return "";
        }
        return seleccionada.getId();
    }

    public LocalDate getFecha() {
        java.util.Date fecha = (java.util.Date) spnFecha.getValue();
        return fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    public JButton getBtnConsultar() {
        return btnConsultar;
    }

    public JTable getTabla() {
        return tabla;
    }

    public void limpiarTabla() {

        modeloTabla.setRowCount(0);
    }

    public void cargarCalendario(List<Recurso> recursos, List<LocalTime> horas, List<Reserva> reservas) {
        modeloTabla.setRowCount(0);
        for (Recurso recurso : recursos) {
            Object[] fila = new Object[horas.size() + 1]; fila[0] = recurso.getDescripcion();
            for (int i = 0; i < horas.size(); i++) {
                LocalTime hora = horas.get(i);
                boolean ocupado = false; for (Reserva reserva : reservas) {
                    if (reserva.getRecursoIds() .contains(recurso.getId())) {
                        if (!hora.isBefore(reserva.getHoraInicio()) && hora.isBefore(reserva.getHoraFin())) {
                            ocupado = true; break;
                        }
                    }
                }
                fila[i + 1] = ocupado ? "Ocupado" : "Disponible";
            } modeloTabla.addRow(fila);
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Calendario de Recursos", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class CalendarioCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            Component componente = super.getTableCellRendererComponent(tabla, valor, seleccionado, tieneFoco, fila, columna);
            setHorizontalAlignment(SwingConstants.CENTER);
            if (columna == 0) {
                setHorizontalAlignment(SwingConstants.LEFT);
            } else if ("Ocupado".equals(valor)) {
                setToolTipText("Recurso reservado");
            } else if ("Disponible".equals(valor)) {
                setToolTipText("Recurso disponible");
            }
            return componente;
        }
    }
}

