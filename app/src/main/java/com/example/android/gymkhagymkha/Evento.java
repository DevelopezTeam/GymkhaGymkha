package com.example.android.gymkhagymkha;

/**
 * Created by Francisco on 26/08/2015.
 */
public class Evento {
    private int id;
    private String descripcion;
    private String hora;
    private boolean isOnline;

    public Evento(int id,String descripcion, String hora, boolean isOnline) {
        this.id = id;
        this.descripcion = descripcion;
        this.hora = hora;
        this.isOnline = isOnline;
    }

    public int getIdEvento() {
        return id;
    }

    public void setIdEvento(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }


}
