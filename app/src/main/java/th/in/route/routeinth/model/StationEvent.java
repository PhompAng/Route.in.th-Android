package th.in.route.routeinth.model;

import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 12/23/2016 AD.
 */
public class StationEvent {
    Station station;
    int type;
    boolean isStation;
    Place place;

    public StationEvent(Station station, int type, boolean isStation) {
        this.station = station;
        this.type = type;
        this.isStation = isStation;
    }

    public StationEvent(Place place, int type, boolean isStation) {
        this.place = place;
        this.type = type;
        this.isStation = isStation;
    }

    public String toString(String lang) {
        switch (lang) {
            case "en":
                return isStation() ? getStation().getEn().replaceFirst(" ", "\n"):getPlace().getName().toString();
            case "th":
                return isStation() ? getStation().getTh().replaceFirst(" ", "\n"):getPlace().getName().toString();
        }
        return isStation() ? getStation().getEn().replaceFirst(" ", "\n"):getPlace().getName().toString();
    }

    public LatLng toLatLng() {
        if (isStation()) {
            Location location = DistanceUtils.getInstance().getLocationFromKey(getStation().getKey());
            return new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            return getPlace().getLatLng();
        }
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isStation() {
        return isStation;
    }

    public void setStation(boolean station) {
        isStation = station;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StationEvent && getStation().equals(((StationEvent) obj).getStation());
    }
}
