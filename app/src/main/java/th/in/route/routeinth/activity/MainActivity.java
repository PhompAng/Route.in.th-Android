package th.in.route.routeinth.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.layout.Result;
import th.in.route.routeinth.R;
import th.in.route.routeinth.layout.Main;

public class  MainActivity extends AppCompatActivity implements Main.OnFragmentInteractionListener, Result.OnTest {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Main mainFragment = Main.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, mainFragment).commit();
    }

    @Override
    public void onFragmentInteraction() {
        Log.i("aaaaaaaaaa", "aaaaaaaaaaa");
    }

    @Override
    public void onTest() {

    }
}
