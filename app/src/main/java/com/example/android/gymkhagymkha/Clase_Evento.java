package com.example.android.gymkhagymkha;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

// Clase para crear objetos evento
public class Clase_Evento {
    private int id;
    private String descripcion;
    private String hora;
    private boolean isOnline;

    //TODO intentar recogerlo como tipo Date
    private String diaEmpiece;
    private String horaEmpiece;

    public Clase_Evento(int id, String descripcion, String hora, boolean isOnline) {
        this.id = id;
        this.descripcion = descripcion;
        this.hora = hora;
        this.isOnline = isOnline;
    }

    public Clase_Evento(JSONObject objetoJSON) throws JSONException {
        id = objetoJSON.getInt("idEvento");
        descripcion = objetoJSON.getString("descripcion");
        diaEmpiece = objetoJSON.getString("diaEmpiece");
        horaEmpiece = objetoJSON.getString("horaEmpiece");
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

    public String getDiaEmpiece() {
        return diaEmpiece;
    }

    public void setDiaEmpiece(String diaEmpiece) {
        this.diaEmpiece = diaEmpiece;
    }

    public String getHoraEmpiece() {
        return horaEmpiece;
    }

    public void setHoraEmpiece(String horaEmpiece) {
        this.horaEmpiece = horaEmpiece;
    }
}
