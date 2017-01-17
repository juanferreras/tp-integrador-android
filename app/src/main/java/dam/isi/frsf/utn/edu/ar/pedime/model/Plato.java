package dam.isi.frsf.utn.edu.ar.pedime.model;

import java.io.Serializable;

/**
 * Created by arielkohan on 1/17/17.
 */

public class Plato  implements Serializable{
    private String nombre;
    private String descripcion;
    private String foto; //URL del recurso
    private Double precio;

    public Plato() { }

    public Plato(String nombre, String descripcion, String foto, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
