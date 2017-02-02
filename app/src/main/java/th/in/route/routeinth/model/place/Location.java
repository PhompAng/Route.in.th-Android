package th.in.route.routeinth.model.place;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class Location {
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return getLat()+","+getLng();
    }
}
