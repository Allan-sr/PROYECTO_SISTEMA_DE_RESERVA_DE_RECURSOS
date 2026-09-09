package una.sistemareservas.vista;

import una.sistemareservas.modelo.Categoria;
import una.sistemareservas.modelo.Recurso;
import una.sistemareservas.modelo.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ReservasView extends JFrame {

    private JTextField txtFrase;
    private JButton btnExtraerIA;

    private JTextField txtActividad;
    private JSpinner spnFecha;
    private JSpinner spnHoraInicio;
    private JSpinner spnHoraFin;

    private JList<Categoria> listCategorias;
    private DefaultListModel<Categoria> modeloListaCategorias;

    private JButton btnReservar;
    private JButton btnCancelar;
    private JButton btnLimpiar;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnImprimir;

    public ReservasView() {

        setTitle("Reservas");
        setSize(780, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(construirPanelFrase(), BorderLayout.NORTH);
        panelSuperior.add(construirPanelFormulario(), BorderLayout.CENTER);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(construirPanelListado(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel construirPanelFrase() {

        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Llenar usando Inteligencia Artificial (opcional)"));

        panel.add(new JLabel("Frase:"), BorderLayout.WEST);

        txtFrase = new JTextField();
        panel.add(txtFrase, BorderLayout.CENTER);

        btnExtraerIA = new JButton("Extraer");
        panel.add(btnExtraerIA, BorderLayout.EAST);

        return panel;
    }

    private JPanel construirPanelFormulario() {

        JPanel panelExterno = new JPanel(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva reserva"));

        panel.add(new JLabel("Actividad:"));
        txtActividad = new JTextField();
        panel.add(txtActividad);

        panel.add(new JLabel("Fecha:"));
        spnFecha = new JSpinner(new SpinnerDateModel());
        spnFecha.setEditor(new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"));
        panel.add(spnFecha);

        panel.add(new JLabel("Hora inicio:"));
        spnHoraInicio = new JSpinner(new SpinnerDateModel());
        spnHoraInicio.setEditor(new JSpinner.DateEditor(spnHoraInicio, "HH:mm"));
        panel.add(spnHoraInicio);

        panel.add(new JLabel("Hora fin:"));
        spnHoraFin = new JSpinner(new SpinnerDateModel());
        spnHoraFin.setEditor(new JSpinner.DateEditor(spnHoraFin, "HH:mm"));
        panel.add(spnHoraFin);

        panelExterno.add(panel, BorderLayout.NORTH);

        JPanel panelCategorias = new JPanel(new BorderLayout());
        panelCategorias.setBorder(BorderFactory.createTitledBorder("Categorías requeridas (selección múltiple)"));

        modeloListaCategorias = new DefaultListModel<>();
        listCategorias = new JList<>(modeloListaCategorias);
        listCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listCategorias.setVisibleRowCount(4);

        JScrollPane scrollCategorias = new JScrollPane(listCategorias);
        panelCategorias.add(scrollCategorias, BorderLayout.CENTER);

        panelExterno.add(panelCategorias, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnReservar = new JButton("Reservar");
        btnCancelar = new JButton("Cancelar reserva seleccionada");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnReservar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnLimpiar);

        panelExterno.add(panelBotones, BorderLayout.SOUTH);

        return panelExterno;
    }

    private JPanel construirPanelListado() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Mis reservas"));

        modeloTabla = new DefaultTableModel(
                new Object[]{"Id", "Actividad", "Fecha", "Horario", "Recursos", "Estado"}, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(700, 180));

        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnImprimir = new JButton("Imprimir");
        panelBoton.add(btnImprimir);

        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Llena la lista de categorías disponibles para seleccionar en la reserva.
     */
    public void cargarCategorias(List<Categoria> categorias) {
        DefaultListModel<Categoria> model = new DefaultListModel<>();
        if (categorias != null) {
            for (Categoria cat : categorias) {
                model.addElement(cat);
            }
        }
        this.listCategorias.setModel(model); // Cambia 'listCategorias' por el nombre de tu JList
    }

    public void cargarTabla(List<Reserva> reservas) {

        modeloTabla.setRowCount(0);

        for (Reserva reserva : reservas) {

            String horario = reserva.getHoraInicio() + " - " + reserva.getHoraFin();

            StringBuilder recursos = new StringBuilder();
            for (Recurso recurso : reserva.getRecursos()) {
                if (recursos.length() > 0) {
                    recursos.append(", ");
                }
                recursos.append(recurso.getId());
            }

            modeloTabla.addRow(new Object[]{
                    reserva.getId(),
                    reserva.getActividad(),
                    reserva.getFecha(),
                    horario,
                    recursos.toString(),
                    reserva.getEstado()
            });
        }
    }

    public String getFrase() {
        return txtFrase.getText().trim();
    }

    public void setFrase(String frase) {
        txtFrase.setText(frase);
    }

    public String getActividad() {
        return txtActividad.getText().trim();
    }

    public void setActividad(String actividad) {
        txtActividad.setText(actividad);
    }

    public LocalDate getFecha() {
        Date fecha = (Date) spnFecha.getValue();
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setFecha(LocalDate fecha) {
        Date valor = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        spnFecha.setValue(valor);
    }

    public LocalTime getHoraInicio() {
        return convertirAHora((Date) spnHoraInicio.getValue());
    }

    public void setHoraInicio(LocalTime hora) {
        spnHoraInicio.setValue(convertirADate(hora));
    }

    public LocalTime getHoraFin() {
        return convertirAHora((Date) spnHoraFin.getValue());
    }

    public void setHoraFin(LocalTime hora) {
        spnHoraFin.setValue(convertirADate(hora));
    }

    private LocalTime convertirAHora(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
                .withSecond(0).withNano(0);
    }

    private Date convertirADate(LocalTime hora) {
        return Date.from(
                LocalDate.now().atTime(hora).atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    /**
     * Retorna los ids de las categorías seleccionadas en la lista de selección múltiple.
     */
    public List<String> getCategoriaIdsSeleccionadas() {
        return listCategorias.getSelectedValuesList()
                .stream()
                .map(Categoria::getId)
                .toList();
    }

    public int getFilaSeleccionada() {
        return tabla.getSelectedRow();
    }

    public String getValorCelda(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return (valor == null) ? "" : valor.toString();
    }

    public JButton getBtnReservar() {
        return btnReservar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnImprimir() {
        return btnImprimir;
    }

    public JTable getTabla() {
        return tabla;
    }

    /**
     * Limpia el formulario de nueva reserva (no toca el listado de "Mis reservas").
     */
    public void limpiarFormulario() {

        txtFrase.setText("");
        txtActividad.setText("");

        spnFecha.setValue(new Date());
        setHoraInicio(LocalTime.of(8, 0));
        setHoraFin(LocalTime.of(9, 0));

        listCategorias.clearSelection();
    }

    public void mostrarMensaje(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Reservas",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public boolean confirmar(String mensaje) {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        return opcion == JOptionPane.YES_OPTION;
    }

    public JTextField getTxtFrase() {
        return txtFrase;
    }

    public JButton getBtnExtraerIA() {
        return btnExtraerIA;
    }

    // Métodos para llenar los campos del formulario con el resultado de la IA
    public void setTxtActividad(String actividad) {
        txtActividad.setText(actividad); // Cambia txtActividad según el nombre exacto de tu campo
    }

    public void seleccionarCategoriasPorNombre(List<String> nombresCategorias) {
        if (nombresCategorias == null || listCategorias == null) return;

        ListModel<Categoria> model = listCategorias.getModel();
        int[] indicesToSelect = new int[model.getSize()];
        int count = 0;

        for (int i = 0; i < model.getSize(); i++) {
            Categoria cat = model.getElementAt(i);
            for (String nombreBuscado : nombresCategorias) {
                // Comparación que ignora mayúsculas y espacios
                if (cat.getDescripcion().trim().equalsIgnoreCase(nombreBuscado.trim())) {
                    indicesToSelect[count++] = i;
                    break;
                }
            }
        }
        listCategorias.setSelectedIndices(java.util.Arrays.copyOf(indicesToSelect, count));
    }

    public JTable getTablaReservas() {
        return tabla;
    }
}
