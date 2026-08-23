package una.sistemareservas.vista;
import javax.swing.*;
import java.awt.*;


public class EjemploTabs extends JFrame {

    private JTabbedPane tabs;

    public EjemploTabs() {

        // Configuración de la ventana
        setTitle("Ejemplo de Tabs en Java");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el componente de pestañas
        tabs = new JTabbedPane();

        // Crear los paneles
        JPanel panelInicio = crearPanelInicio();
        JPanel panelEstudiante = crearPanelEstudiante();
        JPanel panelAcercaDe = crearPanelAcercaDe();

        // Agregar los paneles como pestañas
        tabs.addTab("Inicio", panelInicio);
        tabs.addTab("Estudiante", panelEstudiante);
        tabs.addTab("Acerca de", panelAcercaDe);

        // Agregar las pestañas a la ventana
        add(tabs);
    }

    // =====================================================
    // TAB 1: INICIO
    // =====================================================
    private JPanel crearPanelInicio() {

        JPanel panel = new JPanel();

        JLabel mensaje =
                new JLabel("Bienvenido al sistema");

        panel.add(mensaje);

        return panel;
    }

    // =====================================================
    // TAB 2: ESTUDIANTE
    // =====================================================
    private JPanel crearPanelEstudiante() {

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblEdad = new JLabel("Edad:");
        JTextField txtEdad = new JTextField();

        JLabel lblCarrera = new JLabel("Carrera:");
        JTextField txtCarrera = new JTextField();

        JButton btnGuardar = new JButton("Guardar");

        // Agregar componentes
        panel.add(lblNombre);
        panel.add(txtNombre);

        panel.add(lblEdad);
        panel.add(txtEdad);

        panel.add(lblCarrera);
        panel.add(txtCarrera);

        panel.add(new JLabel(""));
        panel.add(btnGuardar);

        // Evento del botón
        btnGuardar.addActionListener(e -> {

            String nombre = txtNombre.getText();
            String edad = txtEdad.getText();
            String carrera = txtCarrera.getText();

            JOptionPane.showMessageDialog(
                    this,
                    "Nombre: " + nombre +
                            "\nEdad: " + edad +
                            "\nCarrera: " + carrera
            );

        });

        return panel;
    }

    // =====================================================
    // TAB 3: ACERCA DE
    // =====================================================
    private JPanel crearPanelAcercaDe() {

        JPanel panel = new JPanel();

        JLabel informacion =
                new JLabel("Ejemplo sencillo de JTabbedPane");

        panel.add(informacion);

        return panel;
    }
}