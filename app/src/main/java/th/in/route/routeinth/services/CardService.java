package th.in.route.routeinth.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import th.in.route.routeinth.R;
import th.in.route.routeinth.app.UIDUtils;
import th.in.route.routeinth.model.view.Card;

/**
 * Created by phompang on 1/23/2017 AD.
 */

public class CardService extends Service {

    private static final String TAG = CardService.class.getSimpleName();

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private Query query;
    private ValueEventListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getCardFromFirebase();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getCardFromFirebase(){
        UIDUtils uidUtils = new UIDUtils(getApplicationContext());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        query = databaseReference.child("users").child(uidUtils.getUID()).child("cardMap");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot cardData: dataSnapshot.getChildren()){
                    Card card = cardData.getValue(Card.class);
                    if (card.getBalance() <= 0) {
                        buildNotification("Please add value to card", card.getName() + " Balance: " + card.getBalance() + " THB", false, card.getIntType());
                    } else {
                        mNotificationManager.cancel(card.getIntType());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        };
        query.addValueEventListener(listener);
    }

    private void buildNotification(String title, String text, boolean onGoing, int id) {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_payment_black_24dp)
                .setOngoing(onGoing);

        if (onGoing) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        }
        mNotificationManager.notify(
                id,
                mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        query.removeEventListener(listener);
    }
}
