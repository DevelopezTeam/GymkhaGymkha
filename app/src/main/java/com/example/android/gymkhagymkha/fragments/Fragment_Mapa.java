package com.example.android.gymkhagymkha.fragments;

import android.app.FragmentManager;
import android.database.Cursor;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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


public class Fragment_Mapa extends android.support.v4.app.Fragment implements OnMapReadyCallback {

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
        //View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        //setUpMapIfNeeded();
        GoogleMap auxMap = mMap;
        // Gets the MapView from the XML layout and creates it
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        MapsInitializer.initialize(getActivity());


        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
        {
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) view.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if(mapView!=null)
                {
                    mMap = mapView.getMap();
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    //mMap.setMyLocationEnabled(true);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 10);
                    mMap.animateCamera(cameraUpdate);
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        manager = new BDManager(getActivity());
        arrayTesoros = new ArrayList<Clase_Tesoro>();
        int idEvento = this.getActivity().getIntent().getExtras().getInt("idEvento");
        new AsyncTesoros().execute("http://www.victordam2b.hol.es/tesorosAcceso.php?idEvento=" + idEvento);
    }

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
        //map.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud )).title(pista));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //map.animateCamera(CameraUpdateFactory.zoomIn());
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

                    Cursor cursorTesoros = manager.cursorTesoros();
                    cursorTesoros.moveToFirst();
                    latitud = cursorTesoros.getDouble(cursorTesoros.getColumnIndex(manager.CN_TREASURE_LATITUDE));
                    longitud = cursorTesoros.getDouble(cursorTesoros.getColumnIndex(manager.CN_TREASURE_LONGITUDE));
                    pista = cursorTesoros.getString(cursorTesoros.getColumnIndex(manager.CN_TREASURE_CLUE));

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud )).title(pista));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 10));


                    //mMap = mapView.getMap();
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    //mMap.setMyLocationEnabled(true);
                    //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 10);
                    //mMap.animateCamera(cameraUpdate);


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
