package com.example.android.gymkhagymkha.geofence;

/**
 * Created by Victor on 11/24/2015.
 */
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
 * Listener for geofence transition changes.
 *
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    SharedPreferences prefs;
    Color color;

    protected static final String TAG = "GeofenceTransitionsIS";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public GeofenceTransitionsIntentService() {
        // Use the TAG to name the worker thread.
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

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            //Star wars
            //v.vibrate(new long[]{0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500},-1);
            //Final fantasy sumado 50 a cada uno
            //v.vibrate(new long[]{100,150,100,150,100,150,450,150,350,150,400,100,250,150,150,100,650},-1);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context               The app context.
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition, triggeringGeofences);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString;
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), Activity_Game.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(Activity_Game.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher2_small)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher2))
                .setVibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500})
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
        //.setContentTitle(notificationDetails)
        //.setContentText(getString(R.string.geofence_transition_notification_text));
        //.setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
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
