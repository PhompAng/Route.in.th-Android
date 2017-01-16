package th.in.route.routeinth.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import th.in.route.routeinth.R;

/**
 * Created by phompang on 1/15/2017 AD.
 */

public class LocationReceiver extends IntentService {
    private String TAG = this.getClass().getSimpleName();
    private LocationResult mLocationResult;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private final int notifyID = 1;

    public LocationReceiver() {
        super("LocationReceiver");
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
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(getString(R.string.navigating))
                    .setContentText(this.mLocationResult.getLastLocation().getLatitude() + " " + this.mLocationResult.getLastLocation().getLongitude())
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_directions_subway_black_24dp);

            mNotificationManager.notify(
                    notifyID,
                    mBuilder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(notifyID);
    }
}
