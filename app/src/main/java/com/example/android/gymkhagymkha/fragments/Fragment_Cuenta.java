package com.example.android.gymkhagymkha.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Login;
import com.example.android.gymkhagymkha.bbdd.BDManager;

import java.io.IOException;

import javax.xml.transform.Result;


public class Fragment_Cuenta extends Fragment {

    Button btnCerrarSesion;
    TextView tvUsuario;
    ImageView ivFotoPerfil;
    BDManager manager;
    String fullname;
    Cursor cursor;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_SINGLE_PICTURE = 101;

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        return view; }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new BDManager(getActivity());
        cursor = manager.cursorLogin();
        cursor.moveToFirst();
        fullname = cursor.getString(cursor.getColumnIndex(manager.CN_FIRSTNAME)) + " " + cursor.getString(cursor.getColumnIndex(manager.CN_LASTNAME));

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ivFotoPerfil = (ImageView) view.findViewById(R.id.ivFotoPerfil);
		// Inicializamos el TextView y le añadimos el nombre completo del usuario
        tvUsuario = (TextView) view.findViewById(R.id.tvUsuario);
        tvUsuario.setText(fullname);

        // Evento para cerrar sesión
        btnCerrarSesion = (Button) view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Mostramos un AlertDialog para avisar de que va a cerrar sesión
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.title_cerrarSesion);
                builder.setIcon(R.drawable.ic_warning_black_24dp);
                builder.setMessage(R.string.message_cerrarSesion)

                        /* Si pulsamos aceptar borraremos el login de la base de datos del
                         móvil y nos volvemos al activity del login */
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                id = cursor.getInt(cursor.getColumnIndex(manager.CN_ID));
                                manager.borrarLogin(id);
                                manager.borrarEventos();
                                manager.borrarTesoros();

                                // Intent al ActivityLogin
                                Intent intent = new Intent(getActivity(), Activity_Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                                // Si pulsamos cancelar no haría nada
                        .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.show();
            }
        });

        ivFotoPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            Uri selectedImageUri = data.getData();
            cargarImagen(selectedImageUri);
        } else {
            Toast.makeText(getActivity(), "Si no quieres una imagen, pa que tocas...", Toast.LENGTH_LONG).show();
        }
    }

    private void cargarImagen(Uri imageUri) {

        Log.i("RUTA", imageUri.getPath());
        ivFotoPerfil.setImageURI(imageUri);

    }
}
