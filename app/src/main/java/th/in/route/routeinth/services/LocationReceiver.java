package th.in.route.routeinth.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

/**
 * Created by phompang on 1/15/2017 AD.
 */

public class LocationReceiver extends IntentService {
    private String TAG = this.getClass().getSimpleName();
    private LocationResult mLocationResult;

    public LocationReceiver() {
        super("LocationReceiver");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if(LocationResult.hasResult(intent)) {
            this.mLocationResult = LocationResult.extractResult(intent);
            Log.i(TAG, "Location Received: " + this.mLocationResult.toString());
        }
    }
}
