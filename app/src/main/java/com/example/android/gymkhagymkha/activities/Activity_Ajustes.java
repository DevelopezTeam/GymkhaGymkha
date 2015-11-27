package com.example.android.gymkhagymkha.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.android.gymkhagymkha.R;

public class Activity_Ajustes extends AppCompatActivity {

    Toolbar toolbar;
    Button btnCambiarTema;
    ImageButton ibPurple, ibRed, ibBlue, ibGreen, ibOrange, ibYellow;
    Button btnCancelarTema;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// Inicializamos el SharedPreferences
        prefs = this.getSharedPreferences("preferenciasGymkha", Context.MODE_PRIVATE);
		// El SharedPreferences será editable
        editor = prefs.edit();
		// Recogemos el valor de una variable
        int idTema = prefs.getInt("idTema", 0);
		// Añadimos un tema dependiendo del valor de la variable
        switch (idTema) {
            case 1: this.setTheme(R.style.Purple_Theme);break;
            case 2: this.setTheme(R.style.Red_Theme);break;
            case 3: this.setTheme(R.style.Blue_Theme);break;
            case 4: this.setTheme(R.style.Green_Theme);break;
            case 5: this.setTheme(R.style.Orange_Theme);break;
            case 6: this.setTheme(R.style.Yellow_Theme);break;
        }
        setContentView(R.layout.activity_settings);

        // Instanciamos la toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarAjustes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Le damos el estilo a la barra de estado en android Lollipop
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_Ajustes.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21)  {
            getWindow().setStatusBarColor(colorPrimaryDark);
            getWindow().setNavigationBarColor(colorPrimaryDark);
        }

		// Instanciamos el botón
        btnCambiarTema = (Button) this.findViewById(R.id.btnCambiarTema);
		// Añadimos un escuchador al botón
        btnCambiarTema.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				// Mostrará un dialog al pulsar el botón
                final Dialog dialog = new Dialog(Activity_Ajustes.this);
				// Añadimos una vista personalizada a este dialog
                dialog.setContentView(R.layout.dialog_cambiar_tema);
				// Añadimos un título al dialog
                dialog.setTitle(R.string.title_cambiarTema);
				// Instanciamos los botones que contiene
                ibPurple = (ImageButton) dialog.findViewById(R.id.ibPurple);
                ibRed = (ImageButton) dialog.findViewById(R.id.ibRed);
                ibBlue = (ImageButton) dialog.findViewById(R.id.ibBlue);
                ibGreen = (ImageButton) dialog.findViewById(R.id.ibGreen);
                ibOrange = (ImageButton) dialog.findViewById(R.id.ibOrange);
                ibYellow = (ImageButton) dialog.findViewById(R.id.ibYellow);
                btnCancelarTema = (Button) dialog.findViewById(R.id.btnCancelarTema);
				// Añadimos escuchadores a todos
                ibPurple.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
						// Insertamos un valor a una variable del SharedPreferences
                        editor.putInt("idTema", 1);
						// La commiteamos
                        editor.commit();
						// Y hacemos un intent para ver los resultamos
                        intentMainActivity();
                    }
                });
                ibRed.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        editor.putInt("idTema", 2);
                        editor.commit();
                        intentMainActivity();
                    }
                });
                ibBlue.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        editor.putInt("idTema", 3);
                        editor.commit();
                        intentMainActivity();
                    }
                });
                ibGreen.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        editor.putInt("idTema", 4);
                        editor.commit();
                        intentMainActivity();
                    }
                });
                ibOrange.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        editor.putInt("idTema", 5);
                        editor.commit();
                        intentMainActivity();
                    }
                });
                ibYellow.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        editor.putInt("idTema", 6);
                        editor.commit();
                        intentMainActivity();
                    }
                });
                btnCancelarTema.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });
        setToolbarTheme();
    }
	
    private void setToolbarTheme() {
		// Recogemos una variable del SharedPreferences
        int idTema = prefs.getInt("idTema", 0);
		// Añade un color al toolbar dependiendo del valor de la variable anterior
        switch (idTema) {
            case 1: toolbar.setBackgroundColor(getResources().getColor(R.color.md_purple_800));break;
            case 2: toolbar.setBackgroundColor(getResources().getColor(R.color.md_red_500));break;
            case 3: toolbar.setBackgroundColor(getResources().getColor(R.color.md_indigo_500));break;
            case 4: toolbar.setBackgroundColor(getResources().getColor(R.color.md_green_500));break;
            case 5: toolbar.setBackgroundColor(getResources().getColor(R.color.md_amber_700));break;
            case 6: toolbar.setBackgroundColor(getResources().getColor(R.color.md_yellow_700));break;
        }
    }

    private void intentMainActivity() {
		// Intent al MainActivity
        Intent intent = new Intent(Activity_Ajustes.this, Activity_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Mostramos un AlertDialog al pulsar el menú
        if (id == R.id.action_acercade) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_acercade);
            builder.setMessage(R.string.creditos);
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
