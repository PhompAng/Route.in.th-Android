package th.in.route.routeinth.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import th.in.route.routeinth.TweetPageFragment;

/**
 * Created by Acer on 12/1/2560.
 */

public class TweetPagerAdapter extends FragmentPagerAdapter {

    public TweetPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("position", position+"");
        return TweetPageFragment.newInstance("test", "test");
    }

    @Override
    public int getCount() {
        return 4;
    }
}
