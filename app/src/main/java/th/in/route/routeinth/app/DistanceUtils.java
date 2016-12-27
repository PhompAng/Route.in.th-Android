package th.in.route.routeinth.app;

import android.location.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class DistanceUtils {
    private static DistanceUtils sDistanceUtils;

    private Map<Station, Location> locationMap;
    private List<RailSystem> systems;

    private double[][] locations = new double[][]
            {
                    {13.6939843,100.7484582},
                    {13.7234914,100.7499419}
            };

    private DistanceUtils() {
        systems = StationUtils.getInstance().getSystems();

        locationMap = new HashMap<>();
        Location a1 = setLocation(locations[0][0], locations[0][1]);
        locationMap.put(systems.get(0).getChildList().get(0), a1);

        Location a2 = setLocation(locations[1][0], locations[1][1]);
        locationMap.put(systems.get(0).getChildList().get(1), a2);
    }

    private Location setLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }

    public static DistanceUtils getInstancec() {
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
