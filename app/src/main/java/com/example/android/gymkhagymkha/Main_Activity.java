package com.example.android.gymkhagymkha;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Main_Activity extends AppCompatActivity {

    BDManager manager;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
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

        fManager = getFragmentManager();
        fTransaction = fManager.beginTransaction();
        fEventos = (Fragment_Eventos) fManager.findFragmentById(R.id.fragment_eventos);
        fRankingGeneral = (Fragment_Ranking_General) fManager.findFragmentById(R.id.fragment_ranking_general);
        fCuenta = (Fragment_Cuenta) fManager.findFragmentById(R.id.fragment_cuenta);
        fAyudaContacto = (Fragment_AyudaContacto) fManager.findFragmentById(R.id.fragment_ayudaContacto);

        manager = new BDManager(this);

        circle_green = getResources().getDrawable(R.drawable.circle_green);
        circle_red = getResources().getDrawable(R.drawable.circle_red);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Eventos");

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

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
                                Intent intent = new Intent(Main_Activity.this, Ajustes_Activity.class);
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

