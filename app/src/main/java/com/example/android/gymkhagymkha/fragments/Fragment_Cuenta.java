package com.example.android.gymkhagymkha.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

import java.net.URI;

public class Fragment_Cuenta extends Fragment {

    Button btnCerrarSesion;
    TextView tvUsuario;
    ImageView ivFotoPerfil, ivBackground;
    BDManager manager;
    String fullname;
    Cursor cursor;
    private static final int SELECT_PICTURE = 1;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new BDManager(getActivity());
        cursor = manager.cursorLogin();
        cursor.moveToFirst();
        fullname = cursor.getString(cursor.getColumnIndex(manager.CN_FIRSTNAME)) + " " + cursor.getString(cursor.getColumnIndex(manager.CN_LASTNAME));

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivFotoPerfil = (ImageView) view.findViewById(R.id.ivFotoPerfil);
        ivBackground = (ImageView) view.findViewById(R.id.ivBackground);
        prefs = getActivity().getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);
        editor = prefs.edit();
        String user_photo = prefs.getString("user_photo", null);
        if (user_photo != null) {
            try {
                Uri imageUri = Uri.parse(user_photo);
                ivFotoPerfil.setImageURI(imageUri);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            ivFotoPerfil.setImageDrawable(getResources().getDrawable(R.drawable.user_photo_small));
        }

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

                                editor.clear();
                                editor.commit();

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

        setTheme();
    }

    private void setTheme() {
        int idTema = prefs.getInt("idTema", 0);
        switch (idTema) {
            case 1:
                ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.header_purple));
                break;
            case 2:
                ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.header_red));
                break;
            case 3:
                ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.header_blue));
                break;
            case 4:
                ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.header_green));
                break;
            case 5:
                ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.header_orange));
                break;
            case 6:
                ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.header_yellow));
                break;
        }
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
        editor.putString("user_photo", imageUri.toString());
        editor.commit();
        ivFotoPerfil.setImageURI(imageUri);
    }
}


