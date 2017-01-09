package th.in.route.routeinth.app;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import th.in.route.routeinth.model.User;

/**
 * Created by phompang on 1/9/2017 AD.
 */

public class UserUtils {
    private User user;
    private UIDUtils uidUtils;
    private static UserUtils sUserUtils;

    private UserUtils(Context context) {
        uidUtils = new UIDUtils(context);
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        reference.child("users").child(uidUtils.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static UserUtils getInstance(Context context) {
        if (sUserUtils == null) {
            sUserUtils = new UserUtils(context);
        }
        return sUserUtils;
    }

    public static UserUtils getInstance() {
        if (sUserUtils == null) {
            throw new NullPointerException();
        }
        return sUserUtils;
    }

    public User getUser() {
        return user;
    }
}
