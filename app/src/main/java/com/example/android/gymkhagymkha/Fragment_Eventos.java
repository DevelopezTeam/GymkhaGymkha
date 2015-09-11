package com.example.android.gymkhagymkha;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class Fragment_Eventos extends Fragment {

        @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_eventos, container, false);
            return view; }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        /* Rellenamos el ListView manualmente, más adelante con un servicio lo rellenamos con
        información del servidor */
        ListView listaEventos = (ListView) view.findViewById(R.id.lvEventos);
        ArrayList<Clase_Evento> arrayEvent = new ArrayList<Clase_Evento>();
        Clase_Evento event;

        event = new Clase_Evento(1,"Evento22","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento33","13:00", false);
        arrayEvent.add(event);

        AdapterEvento adapter2 = new AdapterEvento(getActivity(), arrayEvent);
        listaEventos.setAdapter(adapter2);
    }
}

