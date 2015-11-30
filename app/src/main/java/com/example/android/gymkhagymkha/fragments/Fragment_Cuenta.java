package com.example.android.gymkhagymkha.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Login;
import com.example.android.gymkhagymkha.activities.Activity_Main;
import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.ImageConverter;

import java.io.File;

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
        if (user_photo != null && new File(user_photo).exists()) {
            insertarImagen(user_photo);
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
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PICTURE);
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
        if (requestCode == SELECT_PICTURE && resultCode != 0 && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            editor.putString("user_photo", picturePath);
            editor.commit();
            insertarImagen(picturePath);
        }
    }

    private void insertarImagen(String picturePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
        Bitmap bitmap2 = bitmap;
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, 64, 64, true);
        Bitmap bm = ImageConverter.getRoundedCornerBitmap(bitmap, 1000);
        Bitmap bm2 = ImageConverter.getRoundedCornerBitmap(bitmap2, 1000);
        ivFotoPerfil.setImageBitmap(bm);
    }
}