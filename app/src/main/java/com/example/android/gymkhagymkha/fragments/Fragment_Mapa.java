package com.example.android.gymkhagymkha.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.example.android.gymkhagymkha.activities.Activity_Login;
import com.example.android.gymkhagymkha.bbdd.BDManager;
import com.example.android.gymkhagymkha.classes.Clase_Jugador;
import com.example.android.gymkhagymkha.classes.Clase_Tesoro;
import com.example.android.gymkhagymkha.geofence.GeofenceErrorMessages;
import com.example.android.gymkhagymkha.geofence.GeofenceTransitionsIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Fragment_Mapa extends android.support.v4.app.Fragment implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    protected static final String TAG = "location-updates";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     * Intervalo normal de la actualización de la localización
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     * Intervalo rápido de la actualización de la localización
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     * Proveedor del servicio de google
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     * ¿?
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     * Localización actual
     */
    protected Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Estado de la solicitud de actualizaciones de la ubicación
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated.
     * Hora en que se actualiza la ubicación.
     */
    protected String mLastUpdateTime;

    //GEOFENCE
    /**
     * The list of geofences.
     * Lista de geofences (circulo pequeño y grande).
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used to keep track of whether geofences were added.
     * Verificación de si los geofences están añadidos (circulo pequeño y grande)
     */
    private boolean mGeofence1Added;
    private boolean mGeofence2Added;

    /**
     * Used when requesting to add or remove geofences.
     * "Servicio" que se utiliza para añadir o borrar los geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * Used to persist application state about whether geofences were added.
     * Nombre de los geofences (se usan para verificar si están añadidos o no).
     */

    private String GEOFENCE1_ADDED_KEY = "GEOFENCE1_ADDED_KEY";
    private String GEOFENCE2_ADDED_KEY = "GEOFENCE2_ADDED_KEY";
    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     * Tiempo que está activo el/los geofences.
     */
    private long GEOFENCE_EXPIRATION_IN_HOURS = 1;

    private long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    /*
    Radio del circulo grande
     */
    private float GEOFENCE_RADIUS_BIG_IN_METERS = 100;
    /*
    Radio del circulo pequeño
     */
    private float GEOFENCE_RADIUS_SMALL_IN_METERS = 20;

    /**
     * Map for storing information about treasure.
     * Lista de nombres y latitud/longitud de los tesoros.
     */
    private static HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();

    private SharedPreferences prefs;

    private GoogleMap mMap;
    private BDManager manager;
    private Cursor cursorTesoros;
    private double latitudTesoro,longitudTesoro;
    private String pista,resul;
    private ArrayList<Clase_Tesoro> arrayTesoros;
    private Toolbar toolbarInGame;
    private TextView tvPista;
    Location myLocation;
    boolean firstTimeZoom = false;
    boolean asynEnemigos = false;
    ArrayList<Marker> markers= new ArrayList<Marker>();

    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        setUpMapIfNeeded();

        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        mGeofenceList = new ArrayList<Geofence>();

        mGeofencePendingIntent = null;

        prefs = getActivity().getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);

        mGeofence1Added = prefs.getBoolean(GEOFENCE1_ADDED_KEY, false);
        mGeofence2Added = prefs.getBoolean(GEOFENCE2_ADDED_KEY, false);

        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();

        manager = new BDManager(getActivity());
        arrayTesoros = new ArrayList<Clase_Tesoro>();
        int idEvento = this.getActivity().getIntent().getExtras().getInt("idEvento");
        new AsyncTesoros().execute("http://www.gymkhagymkha.esy.es/tesorosAcceso.php?idEvento=" + idEvento);
    }

    /*
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     *
     * Datos guardados en el bundle
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     * Metodo para solicitar el servicio de ubicación
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     *
     * Creación de la solicitud de la localización
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     * Iniciar la actualización de la localización
     */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        /*
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();


            BAY_AREA_LANDMARKS.put("CIRCLE_BIG", new LatLng(latitudTesoro, longitudTesoro));
            BAY_AREA_LANDMARKS.put("CIRCLE_SMALL", new LatLng(latitudTesoro, longitudTesoro));

            populateGeofenceList();
            insertarGeofences();
        }
        */
    }

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        Cursor cursorLogin;
        cursorLogin = manager.cursorLogin();
        cursorLogin.moveToFirst();
        int idJugador = cursorLogin.getInt(cursorLogin.getColumnIndex(manager.CN_USER_ID));
        int idEvento = this.getActivity().getIntent().getExtras().getInt("idEvento");

        double latitud = location.getLatitude();
        String auxLatString = String.valueOf(latitud);
        String[] arrayLat = auxLatString.split(Pattern.quote("."));
        int latitudEntero = Integer.parseInt(arrayLat[0]);
        int latitudDecimal = Integer.parseInt(arrayLat[1]);

        double longitud = location.getLongitude();
        String auxLonString = String.valueOf(longitud);
        String[] arrayLon = auxLonString.split(Pattern.quote("."));
        int longitudEntero = Integer.parseInt(arrayLon[0]);
        int longitudDecimal = Integer.parseInt(arrayLon[1]);
        String url = "http://www.gymkhagymkha.esy.es/jugadoresAcceso.php?idJugador=" + idJugador + "&latitudEntero="+latitudEntero+"&latitudDecimal="+latitudDecimal+"&longitudEntero="+longitudEntero+"&longitudDecimal="+longitudDecimal+"&idEvento="+idEvento;
        if(!asynEnemigos){
            new AsyncEnemigos().execute(url);
        }
        if(!firstTimeZoom){
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(16).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            firstTimeZoom = true;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        builder.addGeofences(mGeofenceList);

        return builder.build();
    }


    @Override
    public void onDestroy() {
        removeGeofences();
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    /*
    * Carga el mapa
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    /*
    *Añade los geofences a la lista
     */
    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : BAY_AREA_LANDMARKS.entrySet()) {
            float auxRadiusMeter;
            if( entry.getKey().compareTo("CIRCLE_SMALL") == 0 ){
                auxRadiusMeter = GEOFENCE_RADIUS_SMALL_IN_METERS;
            }
            else{
                auxRadiusMeter = GEOFENCE_RADIUS_BIG_IN_METERS;
            }
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(entry.getKey())
                        .setCircularRegion(
                                entry.getValue().latitude,
                                entry.getValue().longitude,
                                auxRadiusMeter
                        )
                        .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());


        }
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            mGeofence1Added = !mGeofence1Added;
            mGeofence2Added = !mGeofence2Added;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(GEOFENCE1_ADDED_KEY, mGeofence1Added);
            editor.putBoolean(GEOFENCE2_ADDED_KEY, mGeofence2Added);
            editor.apply();

        } else {

            String errorMessage = GeofenceErrorMessages.getErrorString(getActivity(),
                    status.getStatusCode());
        }
    }

    /*
    * Creación del servicio (intent)
     */
    private PendingIntent getGeofencePendingIntent() {

        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);

        PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void logSecurityException(SecurityException securityException) {

    }

    /*
    * Inserta los geofences y lanza el servicio
     */
    private void insertarGeofences(){
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result se procesa en onResult().
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    /*
    * Borra los geofences y "mata" el servicio
     */
    public void removeGeofences(){
        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result se procesa en onResult().
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    /**
     * Se descarga los tesoros del evento en el que estas
     */
    public class AsyncTesoros extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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


            } catch (IOException e) {

            } finally {
                urlConnection.disconnect();
            }
            return sb;
        }

        protected void onPostExecute(StringBuilder sb) {
            if (resul.compareTo("-1") == 0 ) {

            } else {
                JSONObject resultadoJSON;
                Clase_Tesoro auxTesoro;
                try {
                    resultadoJSON = new JSONObject(resul);
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        auxTesoro = new Clase_Tesoro(resultadoJSON.getJSONObject(i + ""));
                        arrayTesoros.add(auxTesoro);
                        manager.guardarTesoro(auxTesoro);
                    }

                    toolbarInGame = (Toolbar) getActivity().findViewById(R.id.toolbarInGame);


                    String nombreEvento = Fragment_Mapa.this.getActivity().getIntent().getExtras().getString("nombreEvento");
                    toolbarInGame.setTitle(nombreEvento+": "+arrayTesoros.get(0).getNombre());
                    String auxPista = arrayTesoros.get(0).getPista();
                    tvPista = (TextView) getActivity().findViewById(R.id.tvPista);
                    tvPista.setText(auxPista);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("pista", auxPista);
                    editor.commit();

                    cursorTesoros = manager.cursorTesoros();
                    cursorTesoros.moveToFirst();
                    latitudTesoro = cursorTesoros.getDouble(cursorTesoros.getColumnIndex(manager.CN_TREASURE_LATITUDE));
                    longitudTesoro = cursorTesoros.getDouble(cursorTesoros.getColumnIndex(manager.CN_TREASURE_LONGITUDE));
                    pista = cursorTesoros.getString(cursorTesoros.getColumnIndex(manager.CN_TREASURE_CLUE));

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitudTesoro, longitudTesoro)).title(pista));

                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(latitudTesoro, longitudTesoro))
                            .radius(GEOFENCE_RADIUS_BIG_IN_METERS)
                            .strokeColor(Color.RED)
                            .fillColor(Color.TRANSPARENT));
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(latitudTesoro, longitudTesoro))
                            .radius(GEOFENCE_RADIUS_SMALL_IN_METERS)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.TRANSPARENT));
                    setMyLocation(mMap.getMyLocation());
                    if (!mRequestingLocationUpdates) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();


                        BAY_AREA_LANDMARKS.put("CIRCLE_BIG", new LatLng(latitudTesoro, longitudTesoro));
                        BAY_AREA_LANDMARKS.put("CIRCLE_SMALL", new LatLng(latitudTesoro, longitudTesoro));

                        populateGeofenceList();
                        insertarGeofences();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Se descarga la localización
     */
    public class AsyncEnemigos extends AsyncTask<String, Void, StringBuilder> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            asynEnemigos = true;
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

            } catch (IOException e) {
            } finally {
                urlConnection.disconnect();
            }
            return sb;
        }

        protected void onPostExecute(StringBuilder sb) {
            if (resul.compareTo("-1") == 0 ) {
            } else {
                JSONObject resultadoJSON;
                Clase_Jugador auxJugador;
                try {
                    resultadoJSON = new JSONObject(resul);

                    for (Marker mark:markers) {
                        mark.remove();
                    }
                    markers.clear();
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        auxJugador = new Clase_Jugador(resultadoJSON.getJSONObject(i + ""));
                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(auxJugador.getLatitud(), auxJugador.getLongitud())).title("Enemigo").icon(BitmapDescriptorFactory.fromResource(R.drawable.enemy));
                        Marker marker = mMap.addMarker(markerOptions);
                        markers.add(marker);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            asynEnemigos = false;
        }
    }
}
