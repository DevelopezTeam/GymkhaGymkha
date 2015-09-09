package com.example.android.gymkhagymkha;

/**
 * Created by Francisco on 26/08/2015.
 */
public class Ranking {

    private int id;
    private String nombre;
    private String puntuacion;

    public Ranking(int id, String nombre, String puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public int getIdRanking() {
        return id;
    }

    public void setIdRanking(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }


}
