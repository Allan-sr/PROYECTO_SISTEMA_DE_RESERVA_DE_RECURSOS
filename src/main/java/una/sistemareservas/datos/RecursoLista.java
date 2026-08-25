package una.sistemareservas.datos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import una.sistemareservas.modelo.Recurso;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "recursos")
public class RecursoLista {

    @XmlElement(name = "recurso")
    private List<Recurso> recursos = new ArrayList<>();

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }
}