package th.in.route.routeinth.app;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by phompang on 1/6/2017 AD.
 */

public class DatabaseUtils {
    private static FirebaseDatabase sDatabaseReference;

    public static FirebaseDatabase getDatabase() {
        if (sDatabaseReference == null) {
            sDatabaseReference = FirebaseDatabase.getInstance();
        }
        return sDatabaseReference;
    }
}
