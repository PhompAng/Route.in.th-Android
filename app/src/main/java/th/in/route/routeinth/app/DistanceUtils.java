package th.in.route.routeinth.app;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class DistanceUtils {
    private static DistanceUtils sDistanceUtils;

    private Map<String, Location> locationMap;

    private double[][] locations = new double[][]
            {
                    {13.6939843,100.7484582},
                    {13.7234914,100.7499419}
            };

    private DistanceUtils() {
        locationMap = new HashMap<>();
        Location a1 = new Location("A1");
        a1.setLatitude(locations[0][0]);
        a1.setLongitude(locations[0][1]);
        locationMap.put("A1", a1);


        Location a2 = new Location("A2");
        a2.setLatitude(locations[1][0]);
        a2.setLongitude(locations[1][1]);
        locationMap.put("A2", a2);
    }

    public static DistanceUtils getInstancec() {
        if (sDistanceUtils == null) {
            sDistanceUtils = new DistanceUtils();
        }
        return sDistanceUtils;
    }

    public String getNearestStation(double lat, double lon) {
        Location location = new Location("place");
        location.setLatitude(lat);
        location.setLongitude(lon);

        double distance = Double.MAX_VALUE;
        String out = "";
        for (String key: locationMap.keySet()) {
            double thisDis = location.distanceTo(locationMap.get(key));
            if (thisDis < distance) {
                distance = thisDis;
                out = key;
            }
        }

        return out;
    }
}
