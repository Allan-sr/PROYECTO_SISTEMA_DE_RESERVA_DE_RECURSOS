package una.sistemareservas.controlador;

import una.sistemareservas.modelo.Usuario;
import una.sistemareservas.negocio.UsuarioService;
import una.sistemareservas.util.Sesion;
import una.sistemareservas.vista.CambiarClaveView;

public class CambiarClaveController {

    private final CambiarClaveView vista;
    private final UsuarioService servicio;

    public CambiarClaveController(
            CambiarClaveView vista) {

        this.vista = vista;
        this.servicio = new UsuarioService();

        iniciarEventos();
    }

    private void iniciarEventos() {

        vista.getBtnCambiar()
                .addActionListener(
                        e -> cambiarClave()
                );

        vista.getBtnCancelar()
                .addActionListener(
                        e -> vista.dispose()
                );
    }

    private void cambiarClave() {

        Usuario usuario =
                Sesion.getUsuarioActual();

        if (usuario == null) {

            vista.mostrarMensaje(
                    "No existe una sesión activa."
            );

            vista.dispose();

            return;
        }

        String claveActual = vista.getClaveActual();

        String nuevaClave = vista.getNuevaClave();

        String confirmar = vista.getConfirmarClave();

        if (claveActual.isEmpty() || nuevaClave.isEmpty() || confirmar.isEmpty()) {

            vista.mostrarMensaje("Todos los campos son obligatorios."
            );

            return;
        }

        if (!nuevaClave.equals(confirmar)) {

            vista.mostrarMensaje(
                    "Las nuevas contraseñas no coinciden."
            );

            return;
        }

        if (nuevaClave.equals(claveActual)) {

            vista.mostrarMensaje(
                    "La nueva contraseña debe ser diferente."
            );

            return;
        }

        boolean resultado =
                servicio.cambiarClave(
                        usuario.getId(),
                        claveActual,
                        nuevaClave
                );

        if (resultado) {

            /*
             * Actualizamos también el usuario
             * que está en la sesión.
             */
            usuario.setClave(nuevaClave);

            vista.mostrarMensaje(
                    "Contraseña cambiada correctamente."
            );

            vista.dispose();

        } else {

            vista.mostrarMensaje(
                    "La contraseña actual es incorrecta."
            );
        }
    }
}