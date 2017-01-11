package th.in.route.routeinth.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import th.in.route.routeinth.model.view.Card;

/**
 * Created by phompang on 1/6/2017 AD.
 */

public class FirebaseUtils {
    public static void regisUser(Context context) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        UIDUtils uidUtils = new UIDUtils(context);
        Log.d("uid", uidUtils.getUID());
        if (TextUtils.isEmpty(uidUtils.getUID())) {
            DatabaseReference uid = reference.child("users").push();
            uidUtils.putUID(uid.getKey());
            uid.child("uid").setValue(uid.getKey());
        }
    }

    public static void addCard(String uid, Card c) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        reference.child("users").child(uid).child("cardMap").child(c.getSystem()).setValue(c);
    }

    public static void addValue(String uid, Card c, double value) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        reference.child("users").child(uid).child("cardMap").child(c.getSystem()).child("balance").setValue(c.getBalance() + value);
    }

    public static void pay(String uid, final int bts, final int mrt, final int arl) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        reference.child("users").child(uid).child("cardMap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    long current = (long) snapshot.child("balance").getValue();
                    switch (snapshot.getKey()) {
                        case "BTS":
                            snapshot.child("balance").getRef().setValue(current-bts);
                            break;
                        case "MRT":
                            snapshot.child("balance").getRef().setValue(current-mrt);
                            break;
                        case "ARL":
                            snapshot.child("balance").getRef().setValue(current-arl);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
