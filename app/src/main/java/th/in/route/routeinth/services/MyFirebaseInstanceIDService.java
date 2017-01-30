package th.in.route.routeinth.services;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import th.in.route.routeinth.app.UIDUtils;

/**
 * Created by phompang on 1/30/2017 AD.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("UpdateToken", token);
        UIDUtils u = new UIDUtils(getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("users").child(u.getUID()).child("token").setValue(token);
    }
}
