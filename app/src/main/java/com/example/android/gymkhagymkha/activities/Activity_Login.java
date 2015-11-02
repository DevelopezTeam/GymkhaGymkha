package com.example.android.gymkhagymkha.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.Clase_Jugador;
import com.example.android.gymkhagymkha.R;

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

public class Activity_Login extends AppCompatActivity {

    Toolbar toolbarLogin;
    EditText etUsuario, etContrasena;
    Button btnLogin;
    String user, pass, resul;
    BDManager manager;

    JSONObject resultadoJSON;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciamos la base de datos
        manager = new BDManager(this);

        // Inicializamos las variables
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        // No mostrar sugerencias en el teclado con este EditText
        etUsuario.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setEnabled(false);
        toolbarLogin = (Toolbar) findViewById(R.id.toolbarLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Añadimos titulo y lo ponemos en blanco
        toolbarLogin.setTitle(R.string.app_name);
        toolbarLogin.setTitleTextColor(getResources().getColor(R.color.md_text_white));
        setSupportActionBar(toolbarLogin);

        // Estilo de la barra de estado
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_Login.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        // Evento del botón "Acceder"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                revisarLogin();
            }
        });

        /* Comprobar si hay un usuario en la base de datos, en el caso de que si lo haya pasamos
        directamente al MainActivity */
        if (manager.cursorLogin().getCount() != 0) {
            intentMainActivity();
        }

        // Evento que capta si el EditText sufre cambios
        etUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // En el caso de que no estén vacios
                if (etUsuario.getText().toString().compareTo("") != 0 && etContrasena.getText().toString().compareTo("") != 0)
                    // Activamos el botón acceder
                    btnLogin.setEnabled(true);
                else
                    btnLogin.setEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Evento que capta si el EditText sufre cambios
        etContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // En el caso de que no estén vacios
                if (etUsuario.getText().toString().compareTo("") != 0 && etContrasena.getText().toString().compareTo("") != 0)
                    // Activamos el botón acceder
                    btnLogin.setEnabled(true);
                else
                    btnLogin.setEnabled(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void intentMainActivity() {

        // Ir al MainActivity
        Intent intent = new Intent(Activity_Login.this, Activity_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private void revisarLogin() {

        // Recogemos los datos en variables string
        user = etUsuario.getText().toString();
        pass = etContrasena.getText().toString();

        /* De momento si no son vacios vale, pero habría que comprobar en el servidor
         si el usuario y contraseña existen. */

        if (user.compareTo("") != 0 && pass.compareTo("") != 0) {

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

            //Conexión (sin especificar)
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            boolean isConnected = false;
            try{
                isConnected = networkInfo.isConnected();
            }
            catch(NullPointerException ex){
                Toast.makeText(this, "No tiene conexión de red", Toast.LENGTH_LONG).show();
            }
            if(!isConnected)
                Toast.makeText(this, "No tiene conexión de red", Toast.LENGTH_LONG).show();
            else{

                new AsyncLogin().execute("http://www.victordam2b.hol.es/loginAcceso.php?usuario=" + user + "&password=" + pass);
                btnLogin.setEnabled(false);
            }

            /* En este caso si son vacios, pero más adelante si no coinciden usuario y contraseña
            borrariamos la contraseña y mostrariamos un toast. */
        } else {

            etContrasena.setText("");
            etUsuario.setText("");
            btnLogin.setEnabled(false);
            Toast.makeText(getApplicationContext(),
                    "Usuario o contraseña incorrectos, inténtelo de nuevo...",
                    Toast.LENGTH_SHORT).show();
        }

    }

    // Evento que se usará para revisar el login con el botón "ENTER" del teclado de android
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_ENTER:
                if(etContrasena.isFocused() && etContrasena.getText().toString().compareTo("") !=0) {
                    revisarLogin();
                }
                return true;
        }
        return super.onKeyUp(keycode, e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class AsyncLogin extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
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
            if(resul.compareTo("-1") == 0){
                Toast.makeText(Activity_Login.this, "Usuario o contraseña incorrecto, intentelo de nuevo...", Toast.LENGTH_LONG).show();
            }
            else{
                Clase_Jugador jugador;
                try {
                    resultadoJSON = new JSONObject(resul);

                    jugador = new Clase_Jugador(resultadoJSON);
                    Toast.makeText(Activity_Login.this, "Bienvenido "+jugador.getNombre(), Toast.LENGTH_LONG).show();
                    manager.login(jugador.getIdJugador(), jugador.getUsuario(), jugador.getNombre(), jugador.getApellido(), jugador.getEmail(),jugador.getIdAministrador(),jugador.getIdCentro());
                    intentMainActivity();
                } catch (JSONException e) {
                    Log.e("Mensaje","Error al crear Clase_Jugador JSON");
                    e.printStackTrace();
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            btnLogin.setEnabled(true);
        }
    }
}
