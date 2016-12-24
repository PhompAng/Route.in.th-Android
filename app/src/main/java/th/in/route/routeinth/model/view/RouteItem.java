package th.in.route.routeinth.model.view;

import th.in.route.routeinth.model.result.Route;

/**
 * Created by Acer on 23/12/2559.
 */

public class RouteItem {
    private String type;
    private String stationOf;
    private Route route;
    private int system;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStationOf() {
        return stationOf;
    }

    public void setStationOf(String stationOf) {
        this.stationOf = stationOf;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }


}
