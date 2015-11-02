package com.example.android.gymkhagymkha.classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor on 02/11/2015.
 */
public class Clase_Tesoro {
    private int idTesoro;
    private String nombre;
    private String pista;
    private String estado; // String?
    private double latitud;
    private double longitud;
    private int idGanador ;



    public Clase_Tesoro(JSONObject objetoJSON) throws JSONException {
        idTesoro = objetoJSON.getInt("idTesoro");
        nombre = objetoJSON.getString("nombre");
        pista = objetoJSON.getString("pista");
        estado = objetoJSON.getString("estado");
        latitud = objetoJSON.getDouble("latitud");
        longitud = objetoJSON.getDouble("longitud");
        idGanador = objetoJSON.getInt("idGanador");
    }

    public Clase_Tesoro(int idTes,String nom,String pis,String est,double lat,double longi/*,int idGan*/){
        idTesoro = idTes;
        nombre = nom;
        pista = pis;
        estado = est;
        latitud = lat;
        longitud = longi;
        idGanador = 0;
    }

    public Clase_Tesoro(int idTes,String nom,String pis,String est,double lat,double longi,int idGan){
        idTesoro = idTes;
        nombre = nom;
        pista = pis;
        estado = est;
        latitud = lat;
        longitud = longi;
        idGanador = idGan;
    }

    public int getIdTesoro() {
        return idTesoro;
    }

    public void setIdTesoro(int idTesoro) {
        this.idTesoro = idTesoro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdGanador() {
        return idGanador;
    }

    public void setIdGanador(int idGanador) {
        this.idGanador = idGanador;
    }
}
