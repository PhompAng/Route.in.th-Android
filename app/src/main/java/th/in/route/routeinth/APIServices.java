package th.in.route.routeinth;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Pichai Sivawat on 6/5/2016.
 */
public interface APIServices {
    @GET("getsystem")
    Call<List<POJOSystem>> getSystem();
}
