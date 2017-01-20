package th.in.route.routeinth.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.in.route.routeinth.TweetPageFragment;

/**
 * Created by Acer on 12/1/2560.
 */

public class TweetPagerAdapter extends FragmentStatePagerAdapter {

    public TweetPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TweetPageFragment.newInstance(position, "test");
    }

    @Override
    public int getCount() {
        return 4;
    }
}
