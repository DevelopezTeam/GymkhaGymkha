package com.example.android.gymkhagymkha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// Clase para Adapter personalizado de eventos
public class AdapterEvento extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Clase_Evento> items;

    public AdapterEvento(Activity activity, ArrayList<Clase_Evento> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getIdEvento();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Generamos una convertView por motivos de eficiencia
        View v = convertView;

        //Asociamos el layout de la lista que hemos creado
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_evento, null);
        }

        // Creamos un objeto directivo
        Clase_Evento event = items.get(position);
        TextView id = (TextView) v.findViewById(R.id.tvNombreEvento);
        id.setText(event.getNombre());
        //Rellenamos el nombre
        TextView nombre = (TextView) v.findViewById(R.id.tvHoraEvento);
        nombre.setText(event.getHora());
        //Rellenamos el puntuación
        ImageView online = (ImageView) v.findViewById(R.id.ivOnline);
        if (event.isIsOnline()){
            online.setImageDrawable(Activity_Main.circle_green);
        }
        else {
            online.setImageDrawable(Activity_Main.circle_red);
        }
            // Retornamos la vista
            return v;
    }
}
