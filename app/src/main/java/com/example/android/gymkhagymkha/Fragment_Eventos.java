package com.example.android.gymkhagymkha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Fragment_Eventos extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ProgressBar progressBarEventos;
    Clase_Evento evento;
    ListView listaEventos;
    AdapterEvento adapterEventos;
    BDManager manager;
    Cursor cursorEventos, cursor;
    String resul = "";
    TextView tvDescripcionEventoDialog, tvNombreEventoDialog, tvHoraEventoDialog;

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
        cursor = manager.cursorLogin();
        cursor.moveToFirst();

        int idAdministrador = cursor.getInt(cursor.getColumnIndex(manager.CN_IDADMINISTRADOR));
        new AsyncEventos().execute("http://www.victordam2b.hol.es/eventosAcceso.php?idAdministrador=" + idAdministrador);

    }

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
                        manager.guardarEvento(evento.getIdEvento(), evento.getDescripcion(),evento.getNombre(), evento.getHora());
                        arrayEvent.add(evento);
                    }
                    listaEventos = (ListView) Fragment_Eventos.this.getActivity().findViewById(R.id.lvEventos);
                    adapterEventos = new AdapterEvento(getActivity(), arrayEvent);
                    listaEventos.setAdapter(adapterEventos);
                    listaEventos.setOnItemClickListener(Fragment_Eventos.this);
                    listaEventos.setOnItemLongClickListener(Fragment_Eventos.this);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressBarEventos.setVisibility(View.INVISIBLE);
            cursorEventos = manager.cursorEventos();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), Activity_InGame.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        cursorEventos = manager.cursorEventos();
        cursorEventos.moveToPosition(position);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event_description);
        tvDescripcionEventoDialog = (TextView) dialog.findViewById(R.id.tvDescripcionEventoDialog);
        tvHoraEventoDialog = (TextView) dialog.findViewById(R.id.tvHoraEventoDialog);
        tvNombreEventoDialog = (TextView) dialog.findViewById(R.id.tvNombreEventoDialog);
        tvDescripcionEventoDialog.setText(cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_DESCRIPTION)));
        tvNombreEventoDialog.setText(cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_NAME)));
        tvHoraEventoDialog.setText(cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_HOUR)));
        dialog.setTitle("Detalles del evento");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return true;
    }
}




