package com.example.android.gymkhagymkha.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.gymkhagymkha.activities.Activity_Login;
import com.example.android.gymkhagymkha.classes.Clase_Evento;
import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Game;
import com.example.android.gymkhagymkha.adapters.AdapterEvento;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_Eventos extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ProgressBar progressBarEventos;
    Clase_Evento evento;
    ListView listaEventos;
    BDManager manager;
    Cursor cursorEventos, cursor;
    Button btnCerrarDialog;
    String resul = "";
    int idAdministrador;
    boolean inAsyncTask;
    FloatingActionButton fabEventos;
    TextView tvDescripcionEventoDialog, tvNombreEventoDialog, tvHoraEventoDialog, tvFechaEvento;

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

        inAsyncTask = false;

        progressBarEventos = (ProgressBar) view.findViewById(R.id.progressBarEventos);
        manager = new BDManager(getActivity());
        cursor = manager.cursorLogin();
        cursor.moveToFirst();

        fabEventos = (FloatingActionButton) view.findViewById(R.id.fabEvento);
        listaEventos = (ListView) view.findViewById(R.id.lvEventos);
        idAdministrador = cursor.getInt(cursor.getColumnIndex(manager.CN_IDADMINISTRADOR));

        newAsyncTask(idAdministrador);

        fabEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inAsyncTask) {
                    listaEventos.setAdapter(null);
                    inAsyncTask = true;
                    newAsyncTask(idAdministrador);
                }
            }
        });

    }

    public void newAsyncTask(int id) {
        new AsyncEventos().execute("http://www.victordam2b.hol.es/eventosAcceso.php?idAdministrador=" + id);
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
                    arrayEvent.clear();
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        evento = new Clase_Evento(resultadoJSON.getJSONObject(i+""));
                        manager.guardarEvento(evento.getIdEvento(), evento.getDescripcion(),evento.getNombre(), evento.getDiaEmpiece(), evento.getHora());
                        arrayEvent.add(evento);
                    }

                    AdapterEvento adapterEventos = new AdapterEvento(getActivity(), arrayEvent);
                    listaEventos.setAdapter(adapterEventos);
                    listaEventos.setOnItemClickListener(Fragment_Eventos.this);
                    listaEventos.setOnItemLongClickListener(Fragment_Eventos.this);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressBarEventos.setVisibility(View.INVISIBLE);
            fabEventos.setVisibility(View.VISIBLE);
            cursorEventos = manager.cursorEventos();
            inAsyncTask = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        manager.borrarTesoros();
        try {
            Cursor cursorEventos = manager.cursorEventos();
            cursorEventos.moveToPosition(position);
            String fechaEvento = cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_DATE));
            String horaEvento = cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_HOUR));
            DateFormat formatTime = new SimpleDateFormat ("hh:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatTime.parse(new Date().getHours() + ":" + new Date().getMinutes()));
            long auxTime = cal.getTimeInMillis();
            cal.setTime(formatTime.parse(horaEvento));
            long horaMax = cal.getTimeInMillis();
            cal.add(Calendar.MINUTE, -15);
            long horaMin = cal.getTimeInMillis();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date event = formatter.parse(fechaEvento);
            if (event.getDate() == new Date().getDate()) {
                if (auxTime >= horaMin ) {
                    if( auxTime <= horaMax) {
                        Intent intent = new Intent(getActivity(), Activity_Game.class);
                        int idEvento = cursorEventos.getInt(cursorEventos.getColumnIndex(manager.CN_IDEVENT));
                        SharedPreferences prefs = this.getActivity().getSharedPreferences("preferenciasGymkha", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("idEvento", idEvento);
                        editor.commit();
                        intent.putExtra("idEvento",idEvento );
                        startActivity(intent);
                    } else {
                        lanzarDialogEventoCerrado();
                    }
                } else {
                    lanzarDialogEventoCerrado();
                }
           } else {
                lanzarDialogEventoCerrado();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void lanzarDialogEventoCerrado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_EventoCerrado);
        builder.setIcon(R.drawable.ic_lock_black_24dp);
        builder.setMessage(R.string.message_EventoCerrado)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        cursorEventos = manager.cursorEventos();
        cursorEventos.moveToPosition(position);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(R.string.title_dialog_event);
        dialog.setContentView(R.layout.dialog_event_description);
        tvDescripcionEventoDialog = (TextView) dialog.findViewById(R.id.tvDescripcionEventoDialog);
        tvHoraEventoDialog = (TextView) dialog.findViewById(R.id.tvHoraEventoDialog);
        tvNombreEventoDialog = (TextView) dialog.findViewById(R.id.tvNombreEventoDialog);
        tvDescripcionEventoDialog.setText(cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_DESCRIPTION)));
        tvNombreEventoDialog.setText(cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_NAME)));
        tvHoraEventoDialog.setText(cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_HOUR)) + " del " + cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_DATE)));
        dialog.setTitle("Detalles del evento");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        btnCerrarDialog = (Button) dialog.findViewById(R.id.btnCerrarDialogEvento);
        btnCerrarDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.hide();
            }
        });
        dialog.show();
        return true;
    }
}




