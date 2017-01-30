package th.in.route.routeinth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.messaging.FirebaseMessaging;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import th.in.route.routeinth.DirectionFragment.OnCalculate;
import th.in.route.routeinth.app.DistanceUtils;
import th.in.route.routeinth.app.FirebaseUtils;
import th.in.route.routeinth.app.LocaleHelper;
import th.in.route.routeinth.model.StationEvent;
import th.in.route.routeinth.model.result.Result;
import th.in.route.routeinth.services.CardService;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        ResultFragment.OnTest, OnCalculate{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "olkRGdsGj8d2Wgz31g3OSyXVi";
    private static final String TWITTER_SECRET = "QAX5BCY8KLGaTu2cxSDXcsgIyZURb81ST4Ujq3xyvC0IkeB7bj";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    private NestedScrollView main;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recreate();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        FirebaseUtils.regisUser(getApplicationContext());
        DistanceUtils.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("preference_noti", true)) {
            FirebaseMessaging.getInstance().subscribeToTopic("service_alerts");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("service_alerts");
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }

        fab.setVisibility(View.GONE);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        main = (NestedScrollView) findViewById(R.id.main);
        startService(new Intent(this, CardService.class));

        if (savedInstanceState == null) {
            DirectionFragment directionFragment = DirectionFragment.newInstance("test", "test");

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, directionFragment).commit();
        }
    }

    public void setFill(boolean isFill) {
        main.setFillViewport(isFill);
    }

    public void showFab() {
        fab.setVisibility(View.VISIBLE);
    }

    public void hideFab() {
        fab.setVisibility(View.GONE);
    }

    public void tabVisibility(int visibility) {
        tabLayout.setVisibility(visibility);
    }

    public void setViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.fab)
    public void addCard() {
        startActivity(new Intent(MainActivity.this, AddCardActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onTest() {

    }

    @Override
    public void OnCalculateBtnPressed(Result result, List<StationEvent> stationEvents) {
        ResultFragment resultFragment = ResultFragment.newInstance();
        resultFragment.setResult(result);
        resultFragment.setStations(stationEvents);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, resultFragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_direction:
                fragmentTransaction.replace(R.id.flContent, DirectionFragment.newInstance("test", "test")).commit();
                return true;
            case R.id.action_card:
                fragmentTransaction.replace(R.id.flContent, new CardFragment()).commit();
                return true;
            case R.id.action_info:
                fragmentTransaction.replace(R.id.flContent, new InformationFragment()).commit();
                return true;
            case R.id.action_announce:
                fragmentTransaction.replace(R.id.flContent, AnnounceFragment.newInstance("test", "test")).commit();
                return true;
        }
        return false;
    }

}
