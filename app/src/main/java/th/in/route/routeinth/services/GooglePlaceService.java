package th.in.route.routeinth.services;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import th.in.route.routeinth.model.place.Location;
import th.in.route.routeinth.model.place.PlaceResponse;

/**
 * Created by phompang on 12/27/2016 AD.
 */

public interface GooglePlaceService {
    @GET("api/place/textsearch/json?key=AIzaSyCCItC0aFhqEKphc5NuOnhWEmK2BRLJnqM")
    Observable<PlaceResponse> getPlace(@Query("query") String query);

    @GET("api/place/nearbysearch/json?radius=5000&key=AIzaSyCCItC0aFhqEKphc5NuOnhWEmK2BRLJnqM")
    Observable<PlaceResponse> getNearbyPlace(@Query("location") Location location);
}
