package th.in.route.routeinth.model.system;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class RailSystem implements Parent<Station> {
    private String title;
    private List<Station> stations;

    public RailSystem(String title, List<Station> stations) {
        this.setTitle(title);
        this.setStations(stations);
    }

    @Override
    public List<Station> getChildList() {
        return stations;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
}
