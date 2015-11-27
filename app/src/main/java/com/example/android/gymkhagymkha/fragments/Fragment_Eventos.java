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
    int positionEvento;
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

		// Boolean para saber si ya tenemos un AsyncTask en ejecución
        inAsyncTask = false;

		// Inicializamos las variables
        progressBarEventos = (ProgressBar) view.findViewById(R.id.progressBarEventos);
        manager = new BDManager(getActivity());
        cursor = manager.cursorLogin();
        cursor.moveToFirst();
        fabEventos = (FloatingActionButton) view.findViewById(R.id.fabEvento);
        listaEventos = (ListView) view.findViewById(R.id.lvEventos);
        idAdministrador = cursor.getInt(cursor.getColumnIndex(manager.CN_IDADMINISTRADOR));

		// Lanzamos un primer AsyncTask
        newAsyncTask(idAdministrador);

		// Añadimos al Floating Action Button un escuchador para el evento click
        fabEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				// Si no estamos en un AsyncTask
                if (!inAsyncTask) {
					// Vaciamos el adaptador para vaciar la lista de eventos
                    listaEventos.setAdapter(null);
					// Lanzamos un nuevo AsyncTask
                    newAsyncTask(idAdministrador);
                }
            }
        });

    }

    public void newAsyncTask(int id) {
		// Cambiamos a true, para saber que estamos en un AsyncTask
		inAsyncTask = true;
		// Y ejecutamos el AsyncTask
        new AsyncEventos().execute("http://www.gymkhagymkha.esy.es/eventosAcceso.php?idAdministrador=" + id);
    }

    public class AsyncEventos extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
			// Mostramos el ProgressBar
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
					// Vaciamos el array de eventos
                    arrayEvent.clear();
					// Creamos un objeto JSON con el resultado del PHP
                    resultadoJSON = new JSONObject(resul);
					// Recorremos el objeto JSON para ir creando objetos Evento
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        evento = new Clase_Evento(resultadoJSON.getJSONObject(i+""));
						// Guardamos el evento en la BBDD de SQLite
                        manager.guardarEvento(evento.getIdEvento(), evento.getDescripcion(),evento.getNombre(), evento.getDiaEmpiece(), evento.getHora());
						// Y lo añadimos a un array
                        arrayEvent.add(evento);
                    }

					// Creamos un objetoo de AdapterEvento y le pasamos el array de eventos
                    AdapterEvento adapterEventos = new AdapterEvento(getActivity(), arrayEvent);
					// Añadimos al ListView el adaptador de eventos
                    listaEventos.setAdapter(adapterEventos);
					// Le añadimos un escuchador para el evento setOnItemClickListener
                    listaEventos.setOnItemClickListener(Fragment_Eventos.this);
					// Le añadimos un escuchador para el evento setOnItemLongClickListener
                    listaEventos.setOnItemLongClickListener(Fragment_Eventos.this);
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
			// Ocultamos el progressBar			
            progressBarEventos.setVisibility(View.INVISIBLE);
			// Hacemos visible el Floating Action Button
            fabEventos.setVisibility(View.VISIBLE);
			// Rellenamos el cursor con los eventos que tenemos en la BBDD SQLite
            cursorEventos = manager.cursorEventos();
			// Y ponemos a false inAsyncTask para saber que ya terminó el AsyncTask
            inAsyncTask = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        manager.borrarTesoros();
        try {
			// Creamos un objeto cursor que rellenamos con los eventos disponibles en la BBDD SQLite
            Cursor cursorEventos = manager.cursorEventos();
			// Movemos el cursor a la posición en la que hemos pulsado en el ListView
            cursorEventos.moveToPosition(position);
			// Guardamos la posición en una variable global para su uso en otro método
            this.positionEvento = position;
			// Recogemos la fecha y hora del evento del cursor
            String fechaEvento = cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_DATE));
            String horaEvento = cursorEventos.getString(cursorEventos.getColumnIndex(manager.CN_EVENT_HOUR));
			
			// Cambiamos el formato de las fechas y horas para poder compararlas
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
			// Línea para borrar
            intentGame();
            /*
			// Si estamos en el día de hoy
            if (event.getDate() == new Date().getDate()) {
				// Si la hora actual es mayor que la hora mínima de entrada
                if (auxTime >= horaMin ) {
					// Si la hora actual es mayor que la hora máxima de entrada
                    if( auxTime <= horaMax) {
						// Mostramos un dialog para avisar de que vamos a entrar a un evento
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.title_entrandoEvento);
                        builder.setIcon(R.drawable.ic_info_black_24dp);
                        builder.setMessage(R.string.message_entrandoEvento)
								// Si pulsamos aceptar, entraremos en el evento
                                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        intentGame();
                                    }
                                })
								// Si pulsamos cancelar, se cerrará el dialog
                                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.show();

                    } else {
                        lanzarDialogEventoCerrado();
                    }
                } else {
                    lanzarDialogEventoCerrado();
                }
           } else {
                lanzarDialogEventoCerrado();
            }
            */
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void intentGame() {
		// Intent para ir ActivityGame
        Intent intent = new Intent(getActivity(), Activity_Game.class);
		// Rellenamos un objeto cursor con los eventos que tenemos en la BBDD SQLite
        Cursor cursor = manager.cursorEventos();
		// Mover el cursor a la posición que hemos puesto como variable global anteriormente
        cursor.moveToPosition(this.positionEvento);
		// Recogemos el id
        int idEvento = cursor.getInt(cursor.getColumnIndex(manager.CN_IDEVENT));
		// Abrimos el SharedPreferences
        SharedPreferences prefs = this.getActivity().getSharedPreferences("preferenciasGymkha", Context.MODE_PRIVATE);
		// Le decimos que se puede editar
        SharedPreferences.Editor editor = prefs.edit();
		// Le añadimos un idEvento
        editor.putInt("idEvento", idEvento);
		// Y le commiteamos
        editor.commit();
		// Le ponemos al intent el valor del id también
        intent.putExtra("idEvento", idEvento);
		// Lanzamos el intent al ActivityGame
        startActivity(intent);
    }

    private void lanzarDialogEventoCerrado() {
		// Si no se cumplen los requisitos de la fecha y la hora se mostrará esta Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_EventoCerrado);
        builder.setIcon(R.drawable.ic_lock_black_24dp);
        builder.setMessage(R.string.message_EventoCerrado)
				// Si pulsamos aceptar se cerrará
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// En el caso de que hagamos una pulsación larga en el item de la lista eventos
		// Se abrirá un dialog con los detalles del evento pulsado
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




