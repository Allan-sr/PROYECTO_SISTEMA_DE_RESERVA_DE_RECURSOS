package una.sistemareservas.vista;
import javax.swing.SwingUtilities;

public class Main
{
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->
    { EjemploTabs ventana = new EjemploTabs();
        ventana.setVisible(true);
    });
    }
}