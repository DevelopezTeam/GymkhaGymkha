package com.example.android.gymkhagymkha.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.gymkhagymkha.classes.Clase_Evento;
import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        TextView idEvento = (TextView) v.findViewById(R.id.tvIdEvento);
        idEvento.setText(event.getIdEvento()+"");
        TextView nombre = (TextView) v.findViewById(R.id.tvNombreEvento);
        nombre.setText(event.getNombre());
        TextView fecha = (TextView)v.findViewById(R.id.tvFechaEvento);
        fecha.setText(event.getDiaEmpiece());

        /*
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date fechaEvento = formatter.parse(event.getDiaEmpiece());
            fecha.setText(new String(formatter.format(fechaEvento)));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        /*
        Date d = Calendar.getInstance().getTime(); // Current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Set your date format
        String currentData = sdf.format(d);
        */
        TextView hora = (TextView) v.findViewById(R.id.tvHoraEvento);
        hora.setText(event.getHora());
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
