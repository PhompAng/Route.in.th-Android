package th.in.route.routeinth.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import th.in.route.routeinth.MainActivity;
import th.in.route.routeinth.R;

/**
 * Created by phompang on 1/30/2017 AD.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getBody().contains("ขัดข้อง") || remoteMessage.getNotification().getBody().contains("ตามปกติ")) {
                updateSystemState(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }
    }

    private void updateSystemState(String title, String body) {
        SharedPreferences sharedPreferences = getSharedPreferences("status", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String system;
        if (title.contains("BTS")) {
            system = "BTS";
        } else if (title.contains("MRT")) {
            system = "MRT";
        } else {
            system = "ARL";
        }
        if (body.contains("ขัดข้อง")) {
            editor.putBoolean(system, true);
        } else if (body.contains("ตามปกติ")) {
            editor.putBoolean(system, false);
        }
        editor.apply();
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(99 /* ID of notification */, notificationBuilder.build());
    }
}
