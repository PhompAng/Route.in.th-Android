package th.in.route.routeinth.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import th.in.route.routeinth.model.result.Input;
import th.in.route.routeinth.model.system.POJOSystem;
import th.in.route.routeinth.model.result.Result;

/**
 * Created by Pichai Sivawat on 6/5/2016.
 */
public interface APIServices {
    @GET("getsystem")
    Call<List<POJOSystem>> getSystem();

    @POST("calculate")
    Call<Result> calculate(@Body Input input);
}
