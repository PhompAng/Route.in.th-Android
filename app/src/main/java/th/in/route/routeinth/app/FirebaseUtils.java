package th.in.route.routeinth.app;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

import th.in.route.routeinth.model.view.Card;

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

    public static void addCard(Context context, Card c) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        UIDUtils uidUtils = new UIDUtils(context);
        reference.child("users").child(uidUtils.getUID()).child("cardMap").child(c.getSystem()).setValue(c);
    }

    public static void addValue(Context context, Card c, double value) {
        DatabaseReference reference = DatabaseUtils.getDatabase().getReference();
        UIDUtils uidUtils = new UIDUtils(context);
        reference.child("users").child(uidUtils.getUID()).child("cardMap").child(c.getSystem()).child("balance").setValue(c.getBalance() + value);
    }
}
