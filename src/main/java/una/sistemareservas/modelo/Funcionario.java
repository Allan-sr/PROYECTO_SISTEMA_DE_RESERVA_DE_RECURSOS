package una.sistemareservas.modelo;

public class Funcionario extends Usuario  {
    private String nombre;
    private String telefono;

    public Funcionario(String id, String clave, Rol rol, String nombre, String telefono) {
        super(id, clave, rol);
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Funcionario(String id, Rol rol, String nombre, String telefono) {
        super(id, id, rol);
        this.nombre = nombre;
        this.telefono = telefono;
    }

    @Override
    public String getNombreCompleto() {
        return nombre;
    }
}
