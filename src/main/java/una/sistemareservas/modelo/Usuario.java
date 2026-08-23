package una.sistemareservas.modelo;

public abstract class Usuario {
    protected String id;
    protected String clave;
    protected Rol rol;

    public Usuario(String id, String clave, Rol rol) {
        this.id = id;
        this.clave = clave;
        this.rol = rol;
    }

    public String getClave() {
        return clave;
    }

    public String getId() {
        return id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    public abstract String getNombreCompleto();
}
