package com.example.android.gymkhagymkha.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
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
    public static Drawable circle_green;
    public static Drawable circle_red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        tvUsuarioBurguer = (TextView) findViewById(R.id.tvUsuarioBurguer);
        tvUsuarioBurguer.setText(fullname);

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
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
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

