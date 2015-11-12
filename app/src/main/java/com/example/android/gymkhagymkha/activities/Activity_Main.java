package com.example.android.gymkhagymkha.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.fragments.Fragment_AyudaContacto;
import com.example.android.gymkhagymkha.fragments.Fragment_Cuenta;
import com.example.android.gymkhagymkha.fragments.Fragment_Eventos;
import com.example.android.gymkhagymkha.fragments.Fragment_Ranking_General;
import com.example.android.gymkhagymkha.R;

public class Activity_Main extends AppCompatActivity {

    BDManager manager;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView tvUsuarioBurguer;
    String fullname;
    Cursor cursor;
    ActionBar actionBar;
    FragmentManager fManager;
    FragmentTransaction fTransaction;
    Fragment_Eventos fEventos;
    Fragment_Cuenta fCuenta;
    Fragment_AyudaContacto fAyudaContacto;
    Fragment_Ranking_General fRankingGeneral;
    public static Drawable circle_green, circle_red, header_blue, header_purple, header_red, header_green, header_yellow, header_orange;
    SharedPreferences prefs;
    NavigationView navigationView;
    ImageView ivHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences("preferenciasGymkha", Context.MODE_PRIVATE);
        int idTema = prefs.getInt("idTema", 0);
        switch (idTema) {
            case 1: this.setTheme(R.style.Purple_Theme);break;
            case 2: this.setTheme(R.style.Red_Theme);break;
            case 3: this.setTheme(R.style.Blue_Theme);break;
            case 4: this.setTheme(R.style.Green_Theme);break;
            case 5: this.setTheme(R.style.Orange_Theme);break;
            case 6: this.setTheme(R.style.Yellow_Theme);break;
        }
        setContentView(R.layout.activity_main);

        // Inicializamos el Fragment_Manager, el Fragment_Transaction
        fManager = getFragmentManager();
        fTransaction = fManager.beginTransaction();
        // Inicializamos los Fragments
        fEventos = (Fragment_Eventos) fManager.findFragmentById(R.id.fragment_eventos);
        fRankingGeneral = (Fragment_Ranking_General) fManager.findFragmentById(R.id.fragment_ranking_general);
        fCuenta = (Fragment_Cuenta) fManager.findFragmentById(R.id.fragment_cuenta);
        fAyudaContacto = (Fragment_AyudaContacto) fManager.findFragmentById(R.id.fragment_ayudaContacto);

        // Inicializamos la base de datos
        manager = new BDManager(this);

        // Rellenamos el campo con el nombre del usuario en el menú de la hamburguesa
        cursor = manager.cursorLogin();
        cursor.moveToFirst();
        fullname = cursor.getString(cursor.getColumnIndex(manager.CN_FIRSTNAME)) + " " + cursor.getString(cursor.getColumnIndex(manager.CN_LASTNAME));

        // Inicializamos dos Drawables

        circle_green = getResources().getDrawable(R.drawable.circle_green);
        circle_red = getResources().getDrawable(R.drawable.circle_red);

        // Inicializamos la toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cambiamos titulo y añadimos icono de la hamburguesa
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Eventos");

        // Inicializamos el Menu Lateral
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header = LayoutInflater.from(this).inflate(R.layout.navigation_drawer_header, null);
        navigationView.addHeaderView(header);
        ivHeader = (ImageView) header.findViewById(R.id.ivHeader);
        tvUsuarioBurguer = (TextView) header.findViewById(R.id.tvUsuarioBurguer);
        tvUsuarioBurguer.setText(fullname);

        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setTheme();
    }

    private void setTheme() {
        int idTema = prefs.getInt("idTema", 0);
        switch (idTema) {
            case 1:
                toolbar.setBackgroundColor(getResources().getColor(R.color.md_deep_purple_500));
                header_purple = getResources().getDrawable(R.drawable.header_purple);
                ivHeader.setImageDrawable(header_purple);
                break;
            case 2:
                toolbar.setBackgroundColor(getResources().getColor(R.color.md_red_500));
                header_red = getResources().getDrawable(R.drawable.header_red);
                ivHeader.setImageDrawable(header_red);
                break;
            case 3:
                toolbar.setBackgroundColor(getResources().getColor(R.color.md_indigo_500));
                header_blue = getResources().getDrawable(R.drawable.header_blue);
                ivHeader.setImageDrawable(header_blue);
                break;
            case 4:
                toolbar.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                header_green = getResources().getDrawable(R.drawable.header_green);
                ivHeader.setImageDrawable(header_green);
                break;
            case 5:
                toolbar.setBackgroundColor(getResources().getColor(R.color.md_amber_700));
                header_orange = getResources().getDrawable(R.drawable.header_orange);
                ivHeader.setImageDrawable(header_orange);
                break;
            case 6:
                toolbar.setBackgroundColor(getResources().getColor(R.color.md_yellow_700));
                header_yellow = getResources().getDrawable(R.drawable.header_yellow);
                ivHeader.setImageDrawable(header_yellow);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Evento para botones
    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            // Con el botón menú abriremos y cerraremos el Navigation-Drawer
            case KeyEvent.KEYCODE_MENU:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            // Con el botón back cerraremos el Navigation-Drawer antes de salir de la app
            case KeyEvent.KEYCODE_BACK:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.title_cerrarApp);
                    builder.setMessage(R.string.message_cerrarApp);
                    builder.setIcon(R.drawable.ic_warning_black_24dp)

                        /* Si pulsamos aceptar saldriamos de la aplicación */
                            .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            })
                                    // Si pulsamos cancelar no haría nada
                            .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.show();
                }
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_eventos:
                                menuItem.setChecked(true);
                                /*********MOSTRAR Y OCULTAR FRAGMENT*************/
                                fManager = getFragmentManager();
                                fTransaction = fManager.beginTransaction();
                                fTransaction.show(fEventos);
                                fTransaction.hide(fRankingGeneral);
                                fTransaction.hide(fCuenta);
                                fTransaction.hide(fAyudaContacto);
                                fTransaction.commit();
                                /*******************************************/
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_ranking:
                                menuItem.setChecked(true);
                                /*********MOSTRAR Y OCULTAR FRAGMENT*************/
                                fManager = getFragmentManager();
                                fTransaction = fManager.beginTransaction();
                                fTransaction.hide(fEventos);
                                fTransaction.show(fRankingGeneral);
                                fTransaction.hide(fCuenta);
                                fTransaction.hide(fAyudaContacto);
                                fTransaction.commit();
                                /*******************************************/
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_cuenta:
                                menuItem.setChecked(true);
                                /*********MOSTRAR Y OCULTAR FRAGMENT*************/
                                fManager = getFragmentManager();
                                fTransaction = fManager.beginTransaction();
                                fTransaction.hide(fEventos);
                                fTransaction.hide(fRankingGeneral);
                                fTransaction.show(fCuenta);
                                fTransaction.hide(fAyudaContacto);
                                fTransaction.commit();
                                /*******************************************/
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_ajustes:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                // Vamos al Activity_Ajustes
                                Intent intent = new Intent(Activity_Main.this, Activity_Ajustes.class);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_ayuda_y_contacto:
                                menuItem.setChecked(true);
                                /*********MOSTRAR Y OCULTAR FRAGMENT*************/
                                fManager = getFragmentManager();
                                fTransaction = fManager.beginTransaction();
                                fTransaction.hide(fEventos);
                                fTransaction.hide(fRankingGeneral);
                                fTransaction.hide(fCuenta);
                                fTransaction.show(fAyudaContacto);
                                fTransaction.commit();
                                /*******************************************/
                                toolbar.setTitle(menuItem.getTitle());
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }
}

