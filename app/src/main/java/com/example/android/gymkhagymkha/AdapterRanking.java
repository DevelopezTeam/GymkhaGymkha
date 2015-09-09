package com.example.android.gymkhagymkha;

/**
 * Created by Francisco on 26/08/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRanking extends BaseAdapter{

    protected Activity activity;
    protected ArrayList<Ranking> items;

    public AdapterRanking(Activity activity, ArrayList<Ranking> items) {
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
        return items.get(position).getIdRanking();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Generamos una convertView por motivos de eficiencia
        View v = convertView;

        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_ranking, null);
        }

        // Creamos un objeto directivo
        Ranking rank = items.get(position);
        TextView id = (TextView) v.findViewById(R.id.tvIdRanking);
        id.setText(rank.getIdRanking() + ".");
        //Rellenamos el nombre
        TextView nombre = (TextView) v.findViewById(R.id.tvNombreRanking);
        nombre.setText(rank.getNombre());
        //Rellenamos el puntuación
        TextView puntuacion = (TextView) v.findViewById(R.id.tvPuntuacionRanking);
        puntuacion.setText(rank.getPuntuacion());

        // Retornamos la vista
        return v;
    }
}
