package th.in.route.routeinth.model.system;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by phompang on 12/23/2016 AD.
 */

public class RailSystemMapper implements Func1<List<POJOSystem>, List<RailSystem>> {
    @Override
    public List<RailSystem> call(List<POJOSystem> pojoSystems) {
        List<RailSystem> systems = new ArrayList<>();

        for (POJOSystem pojoSystem: pojoSystems) {
            List<Station> stations = new ArrayList<>();
            for (Detail detail: pojoSystem.option.values()) {
                stations.add(new Station(detail.th, detail.en, detail.code, detail.key));
            }
            systems.add(new RailSystem(pojoSystem.name, stations));
        }

        return systems;
    }
}
