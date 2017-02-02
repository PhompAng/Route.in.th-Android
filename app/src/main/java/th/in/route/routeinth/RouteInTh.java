package th.in.route.routeinth;

import android.app.Application;
import android.content.Context;

import th.in.route.routeinth.app.LocaleHelper;
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
