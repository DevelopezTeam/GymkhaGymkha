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
    private double latitudTesoro;
    private double longitudTesoro;
    private int idGanador ;



    public Clase_Tesoro(JSONObject objetoJSON) throws JSONException {
        idTesoro = objetoJSON.getInt("idTesoro");
        nombre = objetoJSON.getString("nombre");
        pista = objetoJSON.getString("pista");
        estado = objetoJSON.getString("estado");
        latitudTesoro = objetoJSON.getDouble("latitudTesoro");
        longitudTesoro = objetoJSON.getDouble("longitudTesoro");
        idGanador = objetoJSON.getInt("idGanador");
    }

    public Clase_Tesoro(int idTes,String nom,String pis,String est,double lat,double longi/*,int idGan*/){
        idTesoro = idTes;
        nombre = nom;
        pista = pis;
        estado = est;
        latitudTesoro = lat;
        longitudTesoro = longi;
        idGanador = 0;
    }

    public Clase_Tesoro(int idTes,String nom,String pis,String est,double lat,double longi,int idGan){
        idTesoro = idTes;
        nombre = nom;
        pista = pis;
        estado = est;
        latitudTesoro = lat;
        longitudTesoro = longi;
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

    public double getlatitudTesoro() {
        return latitudTesoro;
    }

    public void setlatitudTesoro(double latitudTesoro) {
        this.latitudTesoro = latitudTesoro;
    }

    public double getlongitudTesoro() {
        return longitudTesoro;
    }

    public void setlongitudTesoro(double longitudTesoro) {
        this.longitudTesoro = longitudTesoro;
    }

    public int getIdGanador() {
        return idGanador;
    }

    public void setIdGanador(int idGanador) {
        this.idGanador = idGanador;
    }
}
