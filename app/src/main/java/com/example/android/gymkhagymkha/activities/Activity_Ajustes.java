package com.example.android.gymkhagymkha.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.gymkhagymkha.R;

public class Activity_Ajustes extends AppCompatActivity {

    Toolbar toolbar;
    Button btnCambiarTema;
    ImageButton ibPurple, ibRed, ibBlue, ibGreen, ibOrange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Instanciamos la toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Le damos el estilo a la barra de estado
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_Ajustes.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        btnCambiarTema = (Button) this.findViewById(R.id.btnCambiarTema);
        btnCambiarTema.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Activity_Ajustes.this);
                dialog.setTitle(R.string.title_cambiarTema);
                dialog.setContentView(R.layout.dialog_cambiar_tema);
                dialog.show();
            }
        });
/*
        ibPurple = (ImageButton) this.findViewById(R.id.ibPurple);
        ibRed = (ImageButton) this.findViewById(R.id.ibRed);
        ibBlue = (ImageButton) this.findViewById(R.id.ibBlue);
        ibGreen = (ImageButton) this.findViewById(R.id.ibGreen);
        ibOrange = (ImageButton) this.findViewById(R.id.ibOrange);
        ibPurple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Activity_Ajustes.this, "Morado", Toast.LENGTH_SHORT).show();
            }
        });
        ibRed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Activity_Ajustes.this, "Rojo", Toast.LENGTH_SHORT).show();
            }
        });
        ibBlue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Activity_Ajustes.this, "Azul", Toast.LENGTH_SHORT).show();
            }
        });
        ibGreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Activity_Ajustes.this, "Verde", Toast.LENGTH_SHORT).show();
            }
        });
        ibOrange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Activity_Ajustes.this, "Naranja", Toast.LENGTH_SHORT).show();
            }
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Mostramos un AlertDialog
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
