package th.in.route.routeinth.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;
import th.in.route.routeinth.model.system.RailSystem;
import th.in.route.routeinth.model.system.RailSystemMapper;
import th.in.route.routeinth.model.system.Station;
import th.in.route.routeinth.services.APIServices;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public class StationUtils {
    private static StationUtils sStationUtils;
    private List<RailSystem> systems;
    private BlockingObservable<List<RailSystem>> observable;
    private StationUtils() {
        systems = new ArrayList<>();
        retrieveStations();
    }

    public static StationUtils getInstance() {
        if (sStationUtils == null) {
            sStationUtils = new StationUtils();
        }
        return sStationUtils;
    }

    private void retrieveStations() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://103.253.134.235:8888/").build();

        APIServices apiServices = retrofit.create(APIServices.class);
        observable = apiServices.getSystem()
                .subscribeOn(Schedulers.newThread())
                .map(new RailSystemMapper())
                .doOnNext(new Action1<List<RailSystem>>() {
                    @Override
                    public void call(List<RailSystem> railSystems) {
                        systems.addAll(railSystems);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("StationUtils", throwable.getMessage());
                    }
                })
                .toBlocking();
    }

    public List<RailSystem> getSystems() {
        return observable.first();
    }

    public Station getStationFromKey(String key) {
        for (RailSystem system: observable.first()) {
            for (Station station: system.getChildList()) {
                if (station.getKey().equals(key)) {
                    return station;
                }
            }
        }
        return null;
    }
}
