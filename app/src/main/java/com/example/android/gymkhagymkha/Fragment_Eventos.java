package com.example.android.gymkhagymkha;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.Arrays;
import java.util.List;

public class Fragment_Eventos extends Fragment {

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

        /* Rellenamos el ListView manualmente, más adelante con un servicio lo rellenamos con
        información del servidor */

        /*TE DEJO ESTO PARA QUE VEAS COMO ES LA ESTRUCTURA DEL ITEM EVENTO*/
        ListView listaEventos = (ListView) view.findViewById(R.id.lvEventos);
        ArrayList<Clase_Evento> arrayEvent = new ArrayList<Clase_Evento>();
        Clase_Evento event;
        event = new Clase_Evento(1,"Evento22","13:00", true);
        arrayEvent.add(event);
        event = new Clase_Evento(1,"Evento1","13:00", false);
        arrayEvent.add(event);
        AdapterEvento adapter2 = new AdapterEvento(getActivity(), arrayEvent);
        listaEventos.setAdapter(adapter2);

        BDManager manager;
        Cursor cursor;

        manager = new BDManager(getActivity());
        cursor = manager.cursorLogin();
        cursor.moveToFirst();



        int idAdministrador = cursor.getInt(cursor.getColumnIndex(manager.CN_IDADMINISTRADOR));
        new AsyncEventos().execute("http://www.victordam2b.hol.es/eventosAcceso.php?idAdministrador=" + idAdministrador);

    }
    String resul = "";
    public class AsyncEventos extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            //TODO ProgressBar ??
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
                //Toast.makeText(Fragment_Eventos.this.getActivity(), "Usuario o contraseña incorrecto, intentelo de nuevo...", Toast.LENGTH_LONG).show();
                Log.i("Eventos","no admin");
            }
            else{
                JSONObject resultadoJSON;
                try {
                    resultadoJSON = new JSONObject(resul);

                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        //JSONObject aux = resultadoJSON.getJSONObject(i+"");
                        arrayEvent.add(new Clase_Evento(resultadoJSON.getJSONObject(i+"")));
                    }
                    ListView listaEventos = (ListView) Fragment_Eventos.this.getActivity().findViewById(R.id.lvEventos);
                    AdapterEvento adapter2 = new AdapterEvento(getActivity(), arrayEvent);
                    listaEventos.setAdapter(adapter2);

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


