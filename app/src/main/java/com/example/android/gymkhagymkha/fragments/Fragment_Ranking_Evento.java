package com.example.android.gymkhagymkha.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Login;
import com.example.android.gymkhagymkha.adapters.AdapterRanking;
import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.Clase_Ranking;
import com.example.android.gymkhagymkha.classes.Clase_RankingEvento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Fragment_Ranking_Evento extends Fragment {

    String resul;
    ArrayList<Clase_Ranking> arrayRank;
    ListView listaRankingEvento;
    BDManager manager;
    boolean inAsyncTask;
    ProgressBar pbRankingEvento;
    FloatingActionButton fabRankingEvento;
    int idEvento;

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_evento, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        inAsyncTask = false;

        /* Rellenamos el ListView manualmente, más adelante con un servicio lo rellenamos con
        información del servidor */
        listaRankingEvento = (ListView) view.findViewById(R.id.lvRankingEvento);
        arrayRank = new ArrayList<Clase_Ranking>();

        fabRankingEvento = (FloatingActionButton) view.findViewById(R.id.fabfabRankingEvento);
        pbRankingEvento = (ProgressBar) view.findViewById(R.id.progressBarRankingEvento);

        manager = new BDManager(getActivity());

        SharedPreferences prefs = this.getActivity().getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);

        idEvento = prefs.getInt("idEvento",0);

        newAsyncTask(idEvento);

        fabRankingEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inAsyncTask) {
                    listaRankingEvento.setAdapter(null);
                    inAsyncTask = true;
                    newAsyncTask(idEvento);
                }
            }
        });

        listaRankingEvento.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listaRankingEvento.getFirstVisiblePosition() == 0) {
                    fabRankingEvento.show();
                } else {
                    fabRankingEvento.hide();
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

    }

    public void newAsyncTask(int id) {
        new AsyncRankingEvento().execute("http://www.gymkhagymkha.esy.es/rankingEventoAcceso.php?idEvento=" + id);
    }

    public class AsyncRankingEvento extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pbRankingEvento.setVisibility(View.VISIBLE);
        }

        @Override
        protected StringBuilder doInBackground(String... _url) {
            HttpURLConnection urlConnection=null;
            StringBuilder sb = new StringBuilder();
            String linea;
            resul = "";
            try {
                URL url = new URL(_url[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((linea = br.readLine()) != null) {
                    resul = resul + linea;
                }
            }
            catch (MalformedURLException e) {
            }
            catch (IOException e) {
            }  finally {
                urlConnection.disconnect();
            }
            return sb;
        }
        protected void onPostExecute(StringBuilder sb) {

            if(resul.compareTo("-1") == 0 ){
            }
            else{
                JSONObject resultadoJSON;
                Clase_RankingEvento auxRanking;
                try {
                    arrayRank.clear();
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        auxRanking = new Clase_RankingEvento(resultadoJSON.getJSONObject(i+""));
                        Clase_Ranking auxAux = new Clase_Ranking((i+1),auxRanking.getNombre(),String.valueOf(auxRanking.getPuntos()));
                        arrayRank.add(auxAux);
                    }

                    // Creo el adapter personalizado
                    AdapterRanking adapter = new AdapterRanking(getActivity(), arrayRank);
                    // Lo aplico
                    listaRankingEvento.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pbRankingEvento.setVisibility(View.INVISIBLE);
            fabRankingEvento.setVisibility(View.VISIBLE);
            inAsyncTask = false;
        }
    }
}
