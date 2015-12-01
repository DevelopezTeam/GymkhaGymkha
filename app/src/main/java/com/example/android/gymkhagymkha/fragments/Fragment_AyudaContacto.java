package com.example.android.gymkhagymkha.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Login;

import java.io.File;

public class Fragment_AyudaContacto extends Fragment {

    Button btnContacto;
    SharedPreferences prefs;

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_contacto, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Inicializamos el botón "Contacto"
        prefs = getActivity().getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);
        btnContacto = (Button) view.findViewById(R.id.btnContacto);
        btnContacto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Hacemos un intent para enviar un correo electrónico
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"gymkhagymkha@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");
                //Con este .setType debería salir solos los clientes de e-Mail
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Elije un cliente de correo electrónico"));

            }
        });

        setTheme();
    }

    private void setTheme() {
        int idTema = prefs.getInt("idTema", 0);
        switch (idTema) {
            case 1:
                btnContacto.setBackgroundColor(getResources().getColor(R.color.md_purple_800));
                break;
            case 2:
                btnContacto.setBackgroundColor(getResources().getColor(R.color.md_red_500));
                break;
            case 3:
                btnContacto.setBackgroundColor(getResources().getColor(R.color.md_indigo_500));
                break;
            case 4:
                btnContacto.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                break;
            case 5:
                btnContacto.setBackgroundColor(getResources().getColor(R.color.md_amber_700));
                break;
            case 6:
                btnContacto.setBackgroundColor(getResources().getColor(R.color.md_yellow_700));
                break;
        }
    }
}
