package com.example.android.gymkhagymkha.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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
import android.view.WindowManager;
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
		// Inicializamos el SharedPreferences
        prefs = this.getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);
		// Recogemos una variable del SharedPreferences
        int idTema = prefs.getInt("idTema", 0);
		// Insertamos un tema dependiendo del valor de la variable anterior
        switch (idTema) {
            case 1: this.setTheme(R.style.Purple_Theme);break;
            case 2: this.setTheme(R.style.Red_Theme);break;
            case 3: this.setTheme(R.style.Blue_Theme);break;
            case 4: this.setTheme(R.style.Green_Theme);break;
            case 5: this.setTheme(R.style.Orange_Theme);break;
            case 6: this.setTheme(R.style.Yellow_Theme);break;
        }
        setContentView(R.layout.activity_game);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Insertamos un estilo a la barra de estado en android Lollipop
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_Game.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
            getWindow().setNavigationBarColor(colorPrimaryDark);
        }

		// Instanciamos la toolbar
        toolbarInGame = (Toolbar) findViewById(R.id.toolbarInGame);
        setSupportActionBar(toolbarInGame);

		// Instanciamos el viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addFragmentToViewPager(viewPager);

		// Instanciamos el tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        setTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void dialogSalirEvento(){
        // Mostrará un dialog donde nos avisará que vamos a salir
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_salirEvento);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setMessage(R.string.message_salirEvento)
                // Si pulsamos al positivo, saldremos
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager = new BDManager(Activity_Game.this);
                        Cursor cursorLogin;
                        cursorLogin = manager.cursorLogin();
                        cursorLogin.moveToFirst();
                        int idJugador = cursorLogin.getInt(cursorLogin.getColumnIndex(manager.CN_USER_ID));
                        new AsyncEventoActual().execute("http://www.gymkhagymkha.esy.es/eventoActualAcceso.php?idJugador=" + idJugador + "&idEvento=null");
                        salirEvento();}})
                        // Si pulsamos al negativo, nos quedaremos
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}});
        builder.show();
    }

    private void salirEvento() {
		// Intento al MainActivity
        Intent intent = new Intent(this, Activity_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
			// Si pulsamos el botón back, no hará nada
            case KeyEvent.KEYCODE_BACK:
                dialogSalirEvento();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    private void setTheme() {
		// Recogemos una variable del SharedPreferences
        int idTema = prefs.getInt("idTema", 0);
        switch (idTema) {
            case 1:
				// Añadimos el color correspondiente al toolbar
                toolbarInGame.setBackgroundColor(getResources().getColor(R.color.md_purple_800));
				// Añadimos el color correspondiente al tabLayout
                tabLayout.setBackgroundColor(getResources().getColor(R.color.md_purple_800));
                break;
            case 2:
                toolbarInGame.setBackgroundColor(getResources().getColor(R.color.md_red_500));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.md_red_500));
                break;
            case 3:
                toolbarInGame.setBackgroundColor(getResources().getColor(R.color.md_indigo_500));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.md_indigo_500));
                break;
            case 4:
                toolbarInGame.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                break;
            case 5:
                toolbarInGame.setBackgroundColor(getResources().getColor(R.color.md_amber_700));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.md_amber_700));
                break;
            case 6:
                toolbarInGame.setBackgroundColor(getResources().getColor(R.color.md_yellow_700));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.md_yellow_700));
                break;
        }
    }

	// Añadimos las vistas de los Fragments al ViewPager
    private void addFragmentToViewPager(ViewPager viewPager) {
		// Inicializamos el ViewPagerAdapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		// Añadimos al adapter las vistas de los Fragments
        adapter.addFragment(new Fragment_Pista(), "Pista");
        adapter.addFragment(new Fragment_Mapa(), "Mapa");
        adapter.addFragment(new Fragment_Ranking_Evento(), "Ranking");
		// Añadimos el adaptador al viewpager
        viewPager.setAdapter(adapter);
		// Posicionamos el viewpager en la posición 1 (en este caso en medio)
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

		// Añades el fragment al adapter y le pones su titulo
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class AsyncEventoActual extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected StringBuilder doInBackground(String... _url) {
            HttpURLConnection urlConnection=null;
            StringBuilder sb = new StringBuilder();
            String linea;
            resul = "";
            try {
                // Leemos la salida de la llamada
                URL url = new URL(_url[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((linea = br.readLine()) != null) {
                    resul = resul + linea;
                }
            }
            catch (MalformedURLException e) {
                Log.e("TESTNET", "URL MAL FORMADA");

            }
            catch (IOException e) {
                Log.e("TESTNET", "IO ERROR");
            }  finally {
                urlConnection.disconnect();
            }
            return sb;
        }
        protected void onPostExecute(StringBuilder sb) {

        }
    }
}
