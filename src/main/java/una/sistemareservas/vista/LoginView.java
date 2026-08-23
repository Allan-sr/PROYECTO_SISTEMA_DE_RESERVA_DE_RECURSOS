package una.sistemareservas.vista;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;

    private JButton btnIngresar;
    private JButton btnCerrar;

    public LoginView() {

        setTitle("Sistema de Reserva de Recursos");
        setSize(450, 300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );

        JLabel titulo =
                new JLabel(
                        "SISTEMA DE RESERVA DE RECURSOS",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(2, 2, 10, 10)
                );

        panelFormulario.add(
                new JLabel("Usuario:")
        );

        txtUsuario = new JTextField();

        panelFormulario.add(txtUsuario);

        panelFormulario.add(
                new JLabel("Contraseña:")
        );

        txtClave = new JPasswordField();

        panelFormulario.add(txtClave);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        btnIngresar =
                new JButton("Iniciar sesión");

        btnCerrar =
                new JButton("Cerrar");

        panelBotones.add(btnIngresar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getClave() {
        return new String(
                txtClave.getPassword()
        );
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    public void mostrarMensaje(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Sistema",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void limpiarCampos() {

        txtUsuario.setText("");
        txtClave.setText("");
        txtUsuario.requestFocus();
    }
}