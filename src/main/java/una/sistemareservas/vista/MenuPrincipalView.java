package una.sistemareservas.vista;

import una.sistemareservas.modelo.Rol;
import una.sistemareservas.modelo.Usuario;
import una.sistemareservas.util.Sesion;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private JLabel lblUsuario;

    private JButton btnFuncionarios;
    private JButton btnCategorias;
    private JButton btnRecursos;
    private JButton btnReservas;
    private JButton btnCalendario;
    private JButton btnActividades;
    private JButton btnEstadisticas;
    private JButton btnCambiarClave;
    private JButton btnCerrarSesion;

    public MenuPrincipalView() {

        setTitle("Sistema de Reserva de Recursos");

        setSize(600, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        inicializarComponentes();

        configurarEventos();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        Usuario usuario =
                Sesion.getUsuarioActual();

        String nombre =
                usuario.getNombreCompleto();

        String rol =
                usuario.getRol().toString();

        lblUsuario =
                new JLabel(
                        "Usuario: " +
                                nombre +
                                " | Rol: " +
                                rol
                );

        lblUsuario.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        panelPrincipal.add(
                lblUsuario,
                BorderLayout.NORTH
        );

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                10,
                                10
                        )
                );

        btnFuncionarios =
                new JButton("Funcionarios");

        btnCategorias =
                new JButton("Categorías de recursos");

        btnRecursos =
                new JButton("Recursos");

        btnReservas =
                new JButton("Mis reservas");

        btnCalendario =
                new JButton("Calendario de recursos");

        btnActividades =
                new JButton("Programación de actividades");

        btnEstadisticas =
                new JButton("Estadísticas");

        btnCambiarClave =
                new JButton("Cambiar contraseña");

        btnCerrarSesion =
                new JButton("Cerrar sesión");

        /*
         * Los botones de administrador
         */
        panelBotones.add(btnFuncionarios);
        panelBotones.add(btnCategorias);
        panelBotones.add(btnRecursos);

        /*
         * Funciones disponibles para ambos
         */
        panelBotones.add(btnCalendario);
        panelBotones.add(btnActividades);
        panelBotones.add(btnEstadisticas);

        /*
         * Reserva solamente para funcionarios
         */
        if (usuario.getRol() == Rol.FUNCIONARIO) {

            panelBotones.add(btnReservas);

        } else {

            btnReservas.setVisible(false);
        }

        panelBotones.add(btnCambiarClave);
        panelBotones.add(btnCerrarSesion);

        /*
         * Si es funcionario, ocultamos funciones
         * exclusivas del administrador.
         */
        if (usuario.getRol() == Rol.FUNCIONARIO) {

            btnFuncionarios.setVisible(false);
            btnCategorias.setVisible(false);
            btnRecursos.setVisible(false);
        }

        panelPrincipal.add(
                panelBotones,
                BorderLayout.CENTER
        );

        add(panelPrincipal);
    }

    private void configurarEventos() {

        btnCerrarSesion.addActionListener(e -> {

            Sesion.cerrar();

            dispose();

            LoginView login =
                    new LoginView();

            new una.sistemareservas.controlador.LoginController(
                    login
            );

            login.setVisible(true);
        });

        btnCambiarClave.addActionListener(e -> {

            CambiarClaveView vista =
                    new CambiarClaveView(this);

            vista.setVisible(true);
        });

        /*
         * Por ahora estos botones solamente muestran
         * que las funcionalidades todavía están
         * pendientes de implementar.
         */

        btnFuncionarios.addActionListener(e -> {

            FuncionariosView vista = new FuncionariosView();

            new una.sistemareservas.controlador.FuncionariosController(vista);

            vista.setVisible(true);
        });

        btnCategorias.addActionListener(e -> {

            CategoriasView vista = new CategoriasView();

            new una.sistemareservas.controlador.CategoriasController(vista);

            vista.setVisible(true);
        });

        btnRecursos.addActionListener(e -> {

            RecursosView vista = new RecursosView();

            new una.sistemareservas.controlador.RecursosController(vista);

            vista.setVisible(true);
        });

        btnReservas.addActionListener(e -> {ReservasView vista = new ReservasView();
            new una.sistemareservas.controlador.ReservaController(vista);
            vista.setVisible(true);
        });

        btnCalendario.addActionListener(e ->
                mostrarPendiente("Calendario")
        );

        btnActividades.addActionListener(e ->
                mostrarPendiente("Programación de actividades")
        );

        btnEstadisticas.addActionListener(e ->
                mostrarPendiente("Estadísticas")
        );
    }

    private void mostrarPendiente(String funcionalidad) {

        JOptionPane.showMessageDialog(
                this,
                funcionalidad +
                        " será implementado en el siguiente módulo.",
                "Funcionalidad pendiente",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}