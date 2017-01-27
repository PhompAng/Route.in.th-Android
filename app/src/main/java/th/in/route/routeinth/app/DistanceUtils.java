package th.in.route.routeinth.app;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class DistanceUtils {
    private static DistanceUtils sDistanceUtils;

    private Map<String, Location> locationMap;
//    private List<RailSystem> systems;

    private DistanceUtils() {
//        systems = StationUtils.getInstance().getSystems();

        locationMap = new HashMap<>();
        DatabaseUtils.getDatabase().getReference().child("station_location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    locationMap.put(snapshot.getKey(), setLocation((double) snapshot.child("latitude").getValue(), (double) snapshot.child("longitude").getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        for (RailSystem system: systems) {
//            for (Station station: system.getChildList()) {
//                setStationLocation(station);
//            }
//        }
    }

    private Location setLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }

//    private void setStationLocation(final Station station) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/maps/")
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        GooglePlaceService service = retrofit.create(GooglePlaceService.class);
//        service.getPlace(station.getEn())
//                .subscribeOn(Schedulers.newThread())
//                .doOnNext(new Action1<PlaceResponse>() {
//                    @Override
//                    public void call(PlaceResponse response) {
//                        th.in.route.routeinth.model.place.Location location = response.getResults().get(0).getGeometry().getLocation();
//                        locationMap.put(station.getKey(), setLocation(location.getLat(), location.getLng()));
//                    }
//                })
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.e("DistanceUtils", throwable.getMessage());
//                    }
//                })
//                .subscribe();
//    }

    public static DistanceUtils getInstance() {
        if (sDistanceUtils == null) {
            sDistanceUtils = new DistanceUtils();
        }
        return sDistanceUtils;
    }

    public String getNearestStation(double lat, double lng) {
        Location location = setLocation(lat, lng);
        double distance = Double.MAX_VALUE;
        String out = null;
        for (String key: locationMap.keySet()) {
            double thisDis = location.distanceTo(locationMap.get(key));
            if (thisDis < distance) {
                distance = thisDis;
                out = key;
            }
        }

        return out;
    }

    public Location getLocationFromKey(String key) {
        return locationMap.get(key);
    }

    public Map<String, Location> getLocationMap(){
        return this.locationMap;
    }
}
