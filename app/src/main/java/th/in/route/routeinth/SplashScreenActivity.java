package th.in.route.routeinth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.FacilitiesUtils;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.LocaleHelper;
import th.in.route.routeinth.services.CardService;

public class SplashScreenActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "olkRGdsGj8d2Wgz31g3OSyXVi";
    private static final String TWITTER_SECRET = "QAX5BCY8KLGaTu2cxSDXcsgIyZURb81ST4Ujq3xyvC0IkeB7bj";

    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 3000L;

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        FirebaseUtils.regisUser(getApplicationContext());
        DistanceUtils.getInstance();
        FacilitiesUtils.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("preference_noti", true)) {
            FirebaseMessaging.getInstance().subscribeToTopic("service_alerts");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("service_alerts");
        }

        startService(new Intent(this, CardService.class));

        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aller_Rg.ttf"));

        handler = new android.os.Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
