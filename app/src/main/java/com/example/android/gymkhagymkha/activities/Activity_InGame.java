package com.example.android.gymkhagymkha.activities;

import android.content.Intent;
import android.database.Cursor;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.gymkhagymkha.adapters.AdapterRanking;
import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.Clase_Ranking;
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

public class Activity_InGame extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String resul;
    ArrayList<Clase_Tesoro> arrayTesoros;
    BDManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Cargar el nombre del tesoro actual en el toolbar
        toolbar.setTitle("Tesoro");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addFragmentToViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Activity_InGame.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        manager = new BDManager(this);
        arrayTesoros = new ArrayList<Clase_Tesoro>();
        new AsyncTesoros().execute("http://www.victordam2b.hol.es/tesorosAcceso.php?idEvento=12");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_salirEvento) {
            Toast.makeText(getApplicationContext(),
                    "TE SALES???? PUES A MAMARLA!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Activity_Main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addFragmentToViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Pista(), "Pista");
        adapter.addFragment(new Fragment_Mapa(), "Mapa");
        adapter.addFragment(new Fragment_Ranking_Evento(), "Ranking");
        viewPager.setAdapter(adapter);
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
    }

    public class AsyncTesoros extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //progressBarEventos.setVisibility(View.VISIBLE);
        }

        @Override
        protected StringBuilder doInBackground(String... _url) {
            HttpURLConnection urlConnection=null;
            StringBuilder sb = new StringBuilder();
            String linea;
            resul = "";
            try {
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
            if(resul.compareTo("-1") == 0 && resul.compareTo("-2") == 0 && resul.compareTo("-3") == 0 && resul.compareTo("-4") == 0 ){
                Log.i("Tesoros","no encontrados");
            }
            else{
                JSONObject resultadoJSON;
                Clase_Tesoro auxTesoro;
                try {
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        auxTesoro = new Clase_Tesoro(resultadoJSON.getJSONObject(i +""));
                        //Clase_Tesoro auxAux = new Clase_Tesoro(auxTesoro.getIdTesoro(),auxTesoro.getNombre(),auxTesoro.getPista(),auxTesoro.getEstado(),auxTesoro.getLatitud(),auxTesoro.getLongitud());
                        //manager.guardarEvento(evento.getIdEvento(), evento.getDescripcion(),evento.getNombre());
                        arrayTesoros.add(auxTesoro);
                        manager.guardarTesoro(auxTesoro);
                    }

                    //listaEventos = (ListView) Fragment_Ranking_General.this.getActivity().findViewById(R.id.);
                    //adapterEventos = new AdapterEvento(getActivity(), arrayEvent);

                    // Creo el adapter personalizado
                    //AdapterRanking adapter = new AdapterRanking(getActivity(), arrayRank);
                    // Lo aplico
                    //listaRankingGeneral.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
