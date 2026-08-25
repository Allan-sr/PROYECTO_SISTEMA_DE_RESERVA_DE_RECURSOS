package una.sistemareservas.datos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import una.sistemareservas.modelo.Reserva;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "reservas")
public class ReservaLista {

    @XmlElement(name = "reserva")
    private List<Reserva> reservas = new ArrayList<>();

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}