package com.example.gabriel.puntodeapoyo;

/**
 * Created by gabii on 23/12/2017.
 */

public class Lugar {
    private String id;
    private String nombre;

    public Lugar(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
