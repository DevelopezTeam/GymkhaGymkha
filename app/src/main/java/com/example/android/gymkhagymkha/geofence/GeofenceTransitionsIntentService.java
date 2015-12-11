package com.example.android.gymkhagymkha.geofence;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Game;
import com.example.android.gymkhagymkha.activities.Activity_Login;
import com.example.android.gymkhagymkha.geofence.GeofenceErrorMessages;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Oyente para los cambios de los geofences.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    SharedPreferences prefs;
    Color color;

    protected static final String TAG = "GeofenceTransitionsIS";


    public GeofenceTransitionsIntentService() {

        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     * Intent que cuando añades los geofences le proporciona el servicio de la localización
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Recibe el tipo de mensaje que se va a mostrar en la notificación
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            //Envia una notificación
            sendNotification(geofenceTransitionDetails);
            //Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            //Star wars
            //v.vibrate(new long[]{0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500},-1);
            //Final fantasy sumado 50 a cada uno
            //v.vibrate(new long[]{100,150,100,150,100,150,450,150,350,150,400,100,250,150,150,100,650},-1);

        } else {
            // error.
        }
    }

    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition, triggeringGeofences);

        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        return geofenceTransitionString;
    }


    private void sendNotification(String notificationDetails) {

        Intent notificationIntent = new Intent(getApplicationContext(), Activity_Game.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(Activity_Game.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Vibracion

        long[] vibracion = new long[]{100};
        if(notificationDetails.compareTo(getString(R.string.geofence_transition_entered_small)) == 0){
            //entraste en el pequeño
            vibracion = new long[] {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
        }
        else if(notificationDetails.compareTo(getString(R.string.geofence_transition_exited_small)) == 0){
            //saliste del pequeño
        }
        else if(notificationDetails.compareTo(getString(R.string.geofence_transition_entered_big)) == 0){
            //entraste grande
            vibracion = new long[] { 100, 250, 250,  250, 250, 250, 250, 250, 250, 250, 250,  250, 250, 250, 250, 250, 250, 250};
        }
        else if(notificationDetails.compareTo(getString(R.string.geofence_transition_exited_big)) == 0){
            //saliste del grande
            vibracion = new long[] { 100, 750, 750,  750, 750, 750, 750, 750, 750, 750, 750, 750};
        }
        else{
            //R.string.unknown_geofence_transition no se sabe...
        }


        //star wars new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}
        builder.setSmallIcon(R.mipmap.ic_launcher2_small)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher2))
                .setVibrate(vibracion)
                .setContentTitle(notificationDetails)
                .setContentText("")
                .setSound(uri);

        prefs = this.getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);
        int idTema = prefs.getInt("idTema", 0);
        switch (idTema) {
            case 1: builder.setLights(getResources().getColor(R.color.md_purple_500),1000,1000*60*60*24);break;
            case 2: builder.setLights(getResources().getColor(R.color.md_red_500), 1000, 1000 * 60 * 60 * 24);break;
            case 3: builder.setLights(getResources().getColor(R.color.md_blue_500), 1000, 1000 * 60 * 60 * 24);break;
            case 4: builder.setLights(getResources().getColor(R.color.md_green_500), 1000, 1000 * 60 * 60 * 24);break;
            case 5: builder.setLights(getResources().getColor(R.color.md_deep_orange_500), 1000, 1000 * 60 * 60 * 24);break;
            case 6: builder.setLights(getResources().getColor(R.color.md_yellow_500), 1000, 1000 * 60 * 60 * 24);break;        }

        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build());
    }

    private String getTransitionString(int transitionType, List<Geofence> triggeringGeofences) {
        if(triggeringGeofences.get(0).getRequestId().compareTo("CIRCLE_SMALL") == 0){
            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    return getString(R.string.geofence_transition_entered_small);
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    return getString(R.string.geofence_transition_exited_small);
                default:
                    return getString(R.string.unknown_geofence_transition);
            }
        }
        else{
            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    return getString(R.string.geofence_transition_entered_big);
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    return getString(R.string.geofence_transition_exited_big);
                default:
                    return getString(R.string.unknown_geofence_transition);
            }
        }
    }
}
