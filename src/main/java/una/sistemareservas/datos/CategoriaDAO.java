package una.sistemareservas.datos;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import una.sistemareservas.modelo.Categoria;

import java.io.File;
import java.util.List;

public class CategoriaDAO {

    private static final String RUTA_ARCHIVO = "categorias.xml";

    public void guardar(List<Categoria> categorias) {
        try {
            CategoriaLista lista = new CategoriaLista();
            lista.setCategorias(categorias);

            JAXBContext contexto = JAXBContext.newInstance(CategoriaLista.class);
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(lista, new File(RUTA_ARCHIVO));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}