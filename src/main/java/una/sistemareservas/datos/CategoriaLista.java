package una.sistemareservas.datos;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import una.sistemareservas.modelo.Categoria;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "categorias")
public class CategoriaLista {
    @XmlElement(name = "categoria")
    private List<Categoria> categorias = new ArrayList<>();

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
