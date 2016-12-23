package th.in.route.routeinth.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import th.in.route.routeinth.model.system.Station;

/**
 * Created by phompang on 12/23/2016 AD.
 */
@Parcel
public class StationEvent {
    Station station;
    int type;

    @ParcelConstructor
    public StationEvent(Station station, int type) {
        this.station = station;
        this.type = type;
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
}
