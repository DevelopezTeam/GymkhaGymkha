package com.example.android.gymkhagymkha.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.gymkhagymkha.R;

public class Fragment_AyudaContacto extends Fragment {

    Button btnContacto;

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
    }
}
