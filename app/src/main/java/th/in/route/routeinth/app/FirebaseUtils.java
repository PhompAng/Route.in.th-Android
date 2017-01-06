package th.in.route.routeinth.app;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by phompang on 1/6/2017 AD.
 */

public class FirebaseUtils {
    public static void regisUser(Context context) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        UIDUtils uidUtils = new UIDUtils(context);
        if (TextUtils.isEmpty(uidUtils.getUID())) {
            DatabaseReference uid = reference.child("users").push();
            uidUtils.putUID(uid.getKey());
        }
    }
}
