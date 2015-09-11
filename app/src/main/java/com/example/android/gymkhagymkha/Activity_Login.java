package com.example.android.gymkhagymkha;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Login extends AppCompatActivity {

    Toolbar toolbarLogin;
    EditText etUsuario, etContrasena;
    Button btnLogin;
    String user, pass;
    BDManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciamos la base de datos
        manager = new BDManager(this);

        // Inicializamos las variables
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        toolbarLogin = (Toolbar) findViewById(R.id.toolbarLogin);

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
                revisarLogin();
            }
        });

        /* Comprobar si hay un usuario en la base de datos, en el caso de que si lo haya pasamos
        directamente al MainActivity */
        if (manager.cursorLogin().getCount() != 0) {
            intentMainActivity();
        }
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

            manager.login(user, pass);
            intentMainActivity();

            /* En este caso si son vacios, pero más adelante si no coinciden usuario y contraseña
            borrariamos la contraseña y mostrariamos un toast. */
        } else {

            etContrasena.setText("");
            Toast.makeText(getApplicationContext(),
                    "Usuario o contraseña incorrectos, inténtelo de nuevo...",
                    Toast.LENGTH_SHORT).show();
        }
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
}
