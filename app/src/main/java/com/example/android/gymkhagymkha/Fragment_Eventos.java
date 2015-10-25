package com.example.android.gymkhagymkha;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class Fragment_Eventos extends Fragment implements AdapterView.OnItemClickListener {

    ProgressBar progressBarEventos;
    Clase_Evento evento;
    ListView listaEventos;
    AdapterEvento adapterEventos;
    BDManager manager;
    Cursor cursorEventos;

        @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_eventos, container, false);
            return view; }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
    }

    ArrayList<Clase_Evento> arrayEvent = new ArrayList<Clase_Evento>();

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        progressBarEventos = (ProgressBar) view.findViewById(R.id.progressBarEventos);

        manager = new BDManager(getActivity());
        Cursor cursor = manager.cursorLogin();
        cursor.moveToFirst();

        int idAdministrador = cursor.getInt(cursor.getColumnIndex(manager.CN_IDADMINISTRADOR));
        new AsyncEventos().execute("http://www.victordam2b.hol.es/eventosAcceso.php?idAdministrador=" + idAdministrador);

    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position,
                            long id) {
        //TODO este evento nos llevaria a dentro del evento
    }

    String resul = "";
    public class AsyncEventos extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressBarEventos.setVisibility(View.VISIBLE);
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
                try {
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        evento = new Clase_Evento(resultadoJSON.getJSONObject(i+""));
                        manager.guardarEvento(evento.getIdEvento(), evento.getDescripcion());
                        arrayEvent.add(evento);
                    }
                    listaEventos = (ListView) Fragment_Eventos.this.getActivity().findViewById(R.id.lvEventos);
                    adapterEventos = new AdapterEvento(getActivity(), arrayEvent);
                    listaEventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int position, long id) {
                            cursorEventos = manager.cursorEventos();
                            cursorEventos.moveToPosition(position);
                            cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_DESCRIPTION));
                            //TODO Hacer un dialog personalizado para mostrar los detalles del evento al hacer pulsación larga
                            // http://developer.android.com/intl/es/guide/topics/ui/dialogs.html
                            return true;
                        }
                    });
                    listaEventos.setAdapter(adapterEventos);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressBarEventos.setVisibility(View.INVISIBLE);
            cursorEventos = manager.cursorEventos();
        }
    }
}




