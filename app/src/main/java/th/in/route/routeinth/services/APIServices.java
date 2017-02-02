package th.in.route.routeinth.services;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import th.in.route.routeinth.model.result.Input;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.model.system.POJOSystem;

/**
 * Created by Pichai Sivawat on 6/5/2016.
 */
public interface APIServices {
    @GET("getsystem")
    Observable<List<POJOSystem>> getSystem();

    @POST("calculate")
    Observable<Result> calculate(@Body Input input);
}
