package th.in.route.routeinth.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
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
            buildNotification();
        }
    }

    private void buildNotification() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.navigating))
                .setContentText("Current Station: " + getNearestStation().getEn())
                .setSmallIcon(R.drawable.ic_directions_subway_black_24dp)
                .setOngoing(true);

        mNotificationManager.notify(
                notifyID,
                mBuilder.build());
    }

    private Station getNearestStation() {
        String nearestKey = DistanceUtils.getInstance().getNearestStation(this.mLocationResult.getLastLocation().getLatitude(), this.mLocationResult.getLastLocation().getLongitude());
        return StationUtils.getInstance().getStationFromKey(nearestKey);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void route(ArrayList<String> route) {
        this.route = route;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
