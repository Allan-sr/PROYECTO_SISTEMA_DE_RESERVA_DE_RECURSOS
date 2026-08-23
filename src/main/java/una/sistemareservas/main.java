package una.sistemareservas;

import una.sistemareservas.controlador.LoginController;
import una.sistemareservas.vista.LoginView;

import javax.swing.*;

public class main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            new LoginController(login);
            login.setVisible(true);
        });
    }
}
