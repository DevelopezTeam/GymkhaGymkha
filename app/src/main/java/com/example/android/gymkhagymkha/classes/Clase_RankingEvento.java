package com.example.android.gymkhagymkha.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Clase_RankingEvento {

    private int id;
    private int idEvento;
    private int idJugador;
    private int idTesoro;
    private int puntos;
    private String nombre;
    private String puntuacion;

    public Clase_RankingEvento(int id, String nombre, String puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public Clase_RankingEvento(JSONObject objetoJSON) throws JSONException {
        idEvento = objetoJSON.getInt("idEvento");
        idJugador = objetoJSON.getInt("idJugador");
        idTesoro = objetoJSON.getInt("idTesoro");
        puntos = objetoJSON.getInt("puntos");
        nombre = objetoJSON.getString("nombre");
    }

    public int getId() {
        return id;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public int getIdTesoro() {
        return idTesoro;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuntuacion() {
        return puntuacion;
    }
}
