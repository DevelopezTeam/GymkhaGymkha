package com.example.android.gymkhagymkha;

import org.json.JSONException;
import org.json.JSONObject;

// Clase para crear objetos Ranking tanto general como de evento
public class Clase_Ranking {

    private int id;
    private int idCentro;
    private int idJugador;
    private int puntos;
    private String nombre;
    private String puntuacion;

    public Clase_Ranking(int id, String nombre, String puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public Clase_Ranking(JSONObject objetoJSON) throws JSONException {
        idCentro = objetoJSON.getInt("idCentro");
        idJugador = objetoJSON.getInt("idJugador");
        puntos = objetoJSON.getInt("puntos");
        nombre = objetoJSON.getString("nombre");
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


    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
