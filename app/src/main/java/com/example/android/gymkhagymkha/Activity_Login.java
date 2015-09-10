package com.example.android.gymkhagymkha;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

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

        manager = new BDManager(this);

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        toolbarLogin = (Toolbar) findViewById(R.id.toolbarLogin);
        toolbarLogin.setTitle(R.string.app_name);
        toolbarLogin.setTitleTextColor(getResources().getColor(R.color.md_text_white));
        setSupportActionBar(toolbarLogin);

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_Login.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                revisarLogin();
            }
        });

        if (manager.cursorLogin().getCount() != 0) {
            intentMainActivity();
        }
    }

    private void intentMainActivity() {

        Intent intent = new Intent(Activity_Login.this, Main_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private void revisarLogin() {

        user = etUsuario.getText().toString();
        pass = etContrasena.getText().toString();

        if (user.compareTo("") != 0 && pass.compareTo("") != 0) {

            manager.login(user, pass);
            intentMainActivity();

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
