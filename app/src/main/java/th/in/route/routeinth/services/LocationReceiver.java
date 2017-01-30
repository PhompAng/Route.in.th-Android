package th.in.route.routeinth.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import th.in.route.routeinth.R;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.StationUtils;
import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 1/15/2017 AD.
 */

public class LocationReceiver extends IntentService {
    private String TAG = this.getClass().getSimpleName();
    private LocationResult mLocationResult;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final int notifyID = 1234;

    private ArrayList<String> route;
    private int btsSameLine;

    public LocationReceiver() {
        super("LocationReceiver");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if(LocationResult.hasResult(intent)) {
            this.mLocationResult = LocationResult.extractResult(intent);
            Log.i(TAG, "Location Received: " + this.mLocationResult.toString());
            Station station = getNearestStation();
            if (station == null) {
                stopSelf();
                return;
            }
            String stationName = station.getEn();
            buildNotification(getString(R.string.navigating), "Current Station: " + stationName, true, notifyID, false);

            String changeText = needChangeSystem(station.getKey());
            if (changeText != null) {
                //TODO change title
                buildNotification("Route.in.th", changeText, false, 123, changeText.equals("End of route"));
            }
        }
    }

    private void buildNotification(String title, String text, boolean onGoing, int id, boolean isSound) {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_directions_subway_black_24dp)
                .setOngoing(onGoing);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (isSound && preferences.getBoolean("preference_sound", true)) {
            mBuilder.setSound(defaultSoundUri);
        }
        if (onGoing) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        }
        mNotificationManager.notify(
                id,
                mBuilder.build());
    }

    private String needChangeSystem(String currentKey) {
        if (route == null) {
            return null;
        }
        for (int i=0;i<route.size()-2;i++) {
            if (currentKey.equals(route.get(route.size()-1))) {
                return "End of route";
            } else if (currentKey.equals(route.get(i))) {
                if (currentKey.charAt(0) != route.get(i+1).charAt(0)) {
                    return "Change system to : " + StationUtils.getInstance().getStationFromKey(route.get(i + 1));
                } else if (route.get(i+1).charAt(1) == 'C' && btsSameLine == 0) {
                    return "Change system to : " + StationUtils.getInstance().getStationFromKey(route.get(i + 1));
                } else {
                    return null;
                }
            }
        }

        return null;
    }

    private Station getNearestStation() {
        String nearestKey = DistanceUtils.getInstance().getNearestStation(this.mLocationResult.getLastLocation().getLatitude(), this.mLocationResult.getLastLocation().getLongitude());
        return StationUtils.getInstance().getStationFromKey(nearestKey);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void route(ArrayList<String> route) {
        this.route = route;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void btsSameLine(Integer btsSameLine) {
        this.btsSameLine = btsSameLine;
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
