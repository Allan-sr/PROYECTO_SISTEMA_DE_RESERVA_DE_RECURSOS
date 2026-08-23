package una.sistemareservas.modelo;

public class Recurso {
    private String id;
    private String descripcion;
    private Categoria categoria;

    public Recurso(String id, String descripcion, Categoria categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

}
