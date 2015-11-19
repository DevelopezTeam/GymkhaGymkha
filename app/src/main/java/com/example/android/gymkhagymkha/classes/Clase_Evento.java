package com.example.android.gymkhagymkha.classes;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Clase para crear objetos evento
public class Clase_Evento {
    private int id;
    private String nombre;
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

    public Clase_Evento(int id, String descripcion, String hora) {
        this.id = id;
        this.descripcion = descripcion;
        this.hora = hora;
        this.isOnline = true;
    }

    public Clase_Evento(JSONObject objetoJSON) throws JSONException {
        id = objetoJSON.getInt("idEvento");
        nombre = objetoJSON.getString("nombre");
        descripcion = objetoJSON.getString("descripcion");
        diaEmpiece = objetoJSON.getString("diaEmpiece");
        hora = objetoJSON.getString("horaEmpiece");
        /*hora = "23:45";
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat formatTime = new SimpleDateFormat ("hh:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatTime.parse(hora));
            cal.add(Calendar.MINUTE, -15);
            Date horaMin = cal.getTime();
            Date horaMax = formatTime.parse(hora);
            Date fechaEvento = formatDate.parse(diaEmpiece);
            this.isOnline = false;
            if (fechaEvento.getDate() == new Date().getDate()) {
                if (new Date().getTime() >= horaMin.getTime() && new Date().getTime() <= horaMax.getTime()) {
                    this.isOnline = true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public int getIdEvento() {return id;}

    public void setIdEvento(int id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
