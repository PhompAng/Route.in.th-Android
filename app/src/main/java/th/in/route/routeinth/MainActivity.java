package th.in.route.routeinth;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.in.route.routeinth.model.result.Result;

public class MainActivity extends AppCompatActivity implements MainFragment.OnCalculateBtnPressedListener, ResultFragment.OnTest {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MainFragment mainFragmentFragment = MainFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, mainFragmentFragment).commit();
        }
    }

    @Override
    public void OnCalculateBtnPressed(Result result) {
        ResultFragment resultFragment = ResultFragment.newInstance();
        resultFragment.setResult(result);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, resultFragment).addToBackStack(null).commit();
    }

    @Override
    public void onTest() {

    }
}
