package th.in.route.routeinth.model.system;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by phompang on 12/22/2016 AD.
 */

public class RailSystem extends ExpandableGroup<Station> {
    public RailSystem(String title, List<Station> items) {
        super(title, items);
    }
}
