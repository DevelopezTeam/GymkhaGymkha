package com.example.android.gymkhagymkha.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.android.gymkhagymkha.classes.Clase_Ranking;
import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.adapters.AdapterRanking;
import com.example.android.gymkhagymkha.bbdd.BDManager;

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

public class Fragment_Ranking_General extends Fragment {

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking_general, container, false);
        return view; }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
    }

    String resul;
    ArrayList<Clase_Ranking> arrayRank;
    ListView listaRankingGeneral;
    BDManager manager;
    int idCentro;
    boolean inAsyncTask;
    ProgressBar pbRankingGeneral;
    FloatingActionButton fabRankingGeneral;

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        inAsyncTask = false;

        /* Rellenamos el ListView manualmente, más adelante con un servicio lo rellenamos con
        información del servidor */
        listaRankingGeneral = (ListView) view.findViewById(R.id.lvRankingGeneral);
        arrayRank = new ArrayList<Clase_Ranking>();
        Clase_Ranking rank;

        fabRankingGeneral = (FloatingActionButton) view.findViewById(R.id.fabRankingGeneral);
        pbRankingGeneral = (ProgressBar) view.findViewById(R.id.progressBarRankingGeneral);

        manager = new BDManager(getActivity());
        Cursor cursor = manager.cursorLogin();
        cursor.moveToFirst();

        idCentro = cursor.getInt(cursor.getColumnIndex(manager.CN_IDCENTRO));
        newAsyncTask(idCentro);

        fabRankingGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inAsyncTask) {
                    listaRankingGeneral.setAdapter(null);
                    inAsyncTask = true;
                    newAsyncTask(idCentro);
                }
            }
        });

        listaRankingGeneral.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listaRankingGeneral.getFirstVisiblePosition() == 0) {
                    fabRankingGeneral.show();
                } else {
                    fabRankingGeneral.hide();
                }
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
    }

    public void newAsyncTask(int id) {
        new AsyncRanking().execute("http://www.gymkhagymkha.esy.es/rankingAcceso.php?idCentro=" + id);
    }

    public class AsyncRanking extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pbRankingGeneral.setVisibility(View.VISIBLE);
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
                Log.e("TESTNET", "URL MAL FORMADA");

            }
            catch (IOException e) {
                Log.e("TESTNET", "IO ERROR");
            }  finally {
                urlConnection.disconnect();
            }
            return sb;
        }
        protected void onPostExecute(StringBuilder sb) {
            if(resul.compareTo("-1") == 0 && resul.compareTo("-2") == 0 && resul.compareTo("-3") == 0 && resul.compareTo("-4") == 0 ){
                Log.i("Eventos","no admin");
            }
            else{
                JSONObject resultadoJSON;
                Clase_Ranking auxRanking;
                try {
                    arrayRank.clear();
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        auxRanking = new Clase_Ranking(resultadoJSON.getJSONObject(i+""));
                        Clase_Ranking auxAux = new Clase_Ranking((i+1),auxRanking.getNombre(),String.valueOf(auxRanking.getPuntos()));
                        arrayRank.add(auxAux);
                    }

                    // Creo el adapter personalizado
                    AdapterRanking adapter = new AdapterRanking(getActivity(), arrayRank);
                    // Lo aplico
                    listaRankingGeneral.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pbRankingGeneral.setVisibility(View.INVISIBLE);
            fabRankingGeneral.setVisibility(View.VISIBLE);
            inAsyncTask = false;
        }
    }
}