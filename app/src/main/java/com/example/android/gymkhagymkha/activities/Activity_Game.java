package com.example.android.gymkhagymkha.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.Clase_Tesoro;
import com.example.android.gymkhagymkha.fragments.Fragment_Mapa;
import com.example.android.gymkhagymkha.fragments.Fragment_Pista;
import com.example.android.gymkhagymkha.fragments.Fragment_Ranking_Evento;
import com.example.android.gymkhagymkha.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Activity_Game extends AppCompatActivity {

    private Toolbar toolbarInGame;
    private TextView tvPista;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String resul;
    ArrayList<Clase_Tesoro> arrayTesoros;
    BDManager manager;
    SharedPreferences prefs;

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
        setContentView(R.layout.activity_game);

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_Game.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        toolbarInGame = (Toolbar) findViewById(R.id.toolbarInGame);
        setSupportActionBar(toolbarInGame);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addFragmentToViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        manager = new BDManager(this);
//        arrayTesoros = new ArrayList<Clase_Tesoro>();
//        int idEvento = getIntent().getExtras().getInt("idEvento");
//        new AsyncTesoros().execute("http://www.victordam2b.hol.es/tesorosAcceso.php?idEvento="+idEvento);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_salirEvento) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_salirEvento);
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage(R.string.message_salirEvento)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            salirEvento();}})
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {}});
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salirEvento() {
        Intent intent = new Intent(this, Activity_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_BACK: return true;
        }
        return super.onKeyDown(keycode, e);
    }

    private void addFragmentToViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Pista(), "Pista");
        adapter.addFragment(new Fragment_Mapa(), "Mapa");
        adapter.addFragment(new Fragment_Ranking_Evento(), "Ranking");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
