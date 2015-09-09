package com.example.android.gymkhagymkha;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Fragment_Ranking_General extends Fragment {

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_general, container, false);
        return view; }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //RELLENAMOS LISTVIEW
        ListView listaRankingGeneral = (ListView) view.findViewById(R.id.lvRankingGeneral);
        ArrayList<Ranking> arrayRank = new ArrayList<Ranking>();
        Ranking rank;

        rank = new Ranking(55,"Hector","10");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(4,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(1,"Hector","10");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(4,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(1,"Hector","100");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(4,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(1,"Hector","100");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(4,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(1,"Hector","100");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(4,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(1,"Hector","10");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(10,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(200,"Hector","10");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(4,"Fernando","1");
        arrayRank.add(rank);
        rank = new Ranking(1,"Hector","100");
        arrayRank.add(rank);
        rank = new Ranking(2,"Victor","75");
        arrayRank.add(rank);
        rank = new Ranking(3,"Fran","25");
        arrayRank.add(rank);
        rank = new Ranking(50,"Fernando","1");
        arrayRank.add(rank);

        // Creo el adapter personalizado
        AdapterRanking adapter = new AdapterRanking(getActivity(), arrayRank);
        // Lo aplico
        listaRankingGeneral.setAdapter(adapter);

    }
}