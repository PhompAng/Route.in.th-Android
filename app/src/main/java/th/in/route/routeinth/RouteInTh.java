package th.in.route.routeinth;

import android.app.Application;

import th.in.route.routeinth.app.StationUtils;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class RouteInTh extends Application {
    private static RouteInTh instance;

    public RouteInTh() {
        instance = this;
        StationUtils.getInstance();
    }

    public static RouteInTh getInstance() {
        return instance;
    }
}
