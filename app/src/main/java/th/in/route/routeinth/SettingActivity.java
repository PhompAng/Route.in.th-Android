package th.in.route.routeinth;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import th.in.route.routeinth.app.LocaleHelper;

public class SettingActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (savedInstanceState == null) {
            // Create the fragment only when the activity is created for the first time.
            // ie. not after orientation changes
            SettingFragment fragment = new SettingFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContent, fragment, SettingFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onPreferenceStartScreen(android.support.v7.preference.PreferenceFragmentCompat caller, PreferenceScreen pref) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.getKey());
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.flContent, fragment, pref.getKey());
        ft.addToBackStack(pref.getKey());
        ft.commit();

        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recreate();
        super.onConfigurationChanged(newConfig);
    }
}
