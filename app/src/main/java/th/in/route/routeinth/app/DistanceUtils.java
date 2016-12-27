package th.in.route.routeinth.app;

import android.location.Location;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import th.in.route.routeinth.model.place.PlaceResponse;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.services.GooglePlaceService;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class DistanceUtils {
    private static DistanceUtils sDistanceUtils;

    private Map<Station, Location> locationMap;
    private List<RailSystem> systems;

    private DistanceUtils() {
        systems = StationUtils.getInstance().getSystems();

        locationMap = new HashMap<>();
        for (RailSystem system: systems) {
            for (Station station: system.getChildList()) {
                setStationLocation(station);
            }
        }
    }

    private Location setLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }

    private void setStationLocation(final Station station) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GooglePlaceService service = retrofit.create(GooglePlaceService.class);
        service.getPlace(station.getEn())
                .subscribeOn(Schedulers.newThread())
                .doOnNext(new Action1<PlaceResponse>() {
                    @Override
                    public void call(PlaceResponse response) {
                        th.in.route.routeinth.model.place.Location location = response.getResults().get(0).getGeometry().getLocation();
                        locationMap.put(station, setLocation(location.getLat(), location.getLng()));
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("DistanceUtils", throwable.getMessage());
                    }
                })
                .subscribe();
    }

    public static DistanceUtils getInstance() {
        if (sDistanceUtils == null) {
            sDistanceUtils = new DistanceUtils();
        }
        return sDistanceUtils;
    }

    public Station getNearestStation(double lat, double lng) {
        Location location = setLocation(lat, lng);
        double distance = Double.MAX_VALUE;
        Station out = null;
        for (Station station: locationMap.keySet()) {
            double thisDis = location.distanceTo(locationMap.get(station));
            if (thisDis < distance) {
                distance = thisDis;
                out = station;
            }
        }

        return out;
    }
}
