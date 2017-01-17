package dam.isi.frsf.utn.edu.ar.pedime.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by arielkohan on 1/17/17.
 */

public class Restaurante implements Serializable{
    private String _id;
    private String nombre;
    private String direccion;
    private List<Plato> platos;

    public Restaurante() { }

    public Restaurante(String _id, String nombre, String direccion, List<Plato> platos) {
        this._id = _id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.platos = platos;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Plato> getPlatos() {
        return platos;
    }

    public void setPlatos(List<Plato> platos) {
        this.platos = platos;
    }
}
