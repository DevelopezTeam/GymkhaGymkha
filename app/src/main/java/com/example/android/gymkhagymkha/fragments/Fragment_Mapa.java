package com.example.android.gymkhagymkha.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.Clase_Tesoro;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class Fragment_Mapa extends android.support.v4.app.Fragment implements OnMapReadyCallback,LocationListener{

    private GoogleMap mMap;
    private BDManager manager;
    private Cursor cursorTesoros;
    private double latitud,longitud;
    private String pista,resul;
    private ArrayList<Clase_Tesoro> arrayTesoros;
    private Toolbar toolbarInGame;
    private TextView tvPista;
    MapView mapView;

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        setUpMapIfNeeded();

        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        manager = new BDManager(getActivity());
        arrayTesoros = new ArrayList<Clase_Tesoro>();
        int idEvento = this.getActivity().getIntent().getExtras().getInt("idEvento");
        new AsyncTesoros().execute("http://www.victordam2b.hol.es/tesorosAcceso.php?idEvento=" + idEvento);
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    */

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.3836, -3.7319)).title("Wiiiii!!!"));
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setAllGesturesEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(), "Cambiando location Latitud:"+location.getLatitude()+" Longitud:"+location.getLongitude(), Toast.LENGTH_LONG).show();
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
            HttpURLConnection urlConnection = null;
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
            } catch (MalformedURLException e) {
                Log.e("TESTNET", "URL MAL FORMADA");

            } catch (IOException e) {
                Log.e("TESTNET", "IO ERROR");
            } finally {
                urlConnection.disconnect();
            }
            return sb;
        }

        protected void onPostExecute(StringBuilder sb) {
            if (resul.compareTo("-1") == 0 && resul.compareTo("-2") == 0 && resul.compareTo("-3") == 0 && resul.compareTo("-4") == 0) {
                Log.i("Tesoros", "no encontrados");
            } else {
                JSONObject resultadoJSON;
                Clase_Tesoro auxTesoro;
                try {
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        auxTesoro = new Clase_Tesoro(resultadoJSON.getJSONObject(i + ""));
                        //Clase_Tesoro auxAux = new Clase_Tesoro(auxTesoro.getIdTesoro(),auxTesoro.getNombre(),auxTesoro.getPista(),auxTesoro.getEstado(),auxTesoro.getLatitud(),auxTesoro.getLongitud());
                        //manager.guardarEvento(evento.getIdEvento(), evento.getDescripcion(),evento.getNombre());
                        arrayTesoros.add(auxTesoro);
                        manager.guardarTesoro(auxTesoro);
                    }

                    toolbarInGame = (Toolbar) getActivity().findViewById(R.id.toolbarInGame);
                    //setSupportActionBar(toolbarInGame);
                    toolbarInGame.setTitle(arrayTesoros.get(0).getNombre());
                    String auxPista = arrayTesoros.get(0).getPista();
                    tvPista = (TextView) getActivity().findViewById(R.id.tvPista);
                    tvPista.setText(auxPista);

                    SharedPreferences prefs = getActivity().getSharedPreferences("preferenciasGymkha", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("pista", auxPista);
                    editor.commit();

                    cursorTesoros = manager.cursorTesoros();
                    cursorTesoros.moveToFirst();
                    latitud = cursorTesoros.getDouble(cursorTesoros.getColumnIndex(manager.CN_TREASURE_LATITUDE));
                    longitud = cursorTesoros.getDouble(cursorTesoros.getColumnIndex(manager.CN_TREASURE_LONGITUDE));
                    pista = cursorTesoros.getString(cursorTesoros.getColumnIndex(manager.CN_TREASURE_CLUE));

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(pista));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 16));
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(latitud, longitud))
                            .radius(250)
                            .strokeColor(Color.RED)
                            .fillColor(Color.TRANSPARENT));
                    mMap.setMyLocationEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
