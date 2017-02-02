package th.in.route.routeinth.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.in.route.routeinth.ServiceAlertsFragment;
import th.in.route.routeinth.TweetPageFragment;

/**
 * Created by Acer on 12/1/2560.
 */

public class TweetPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitle = new String[] {"News", "BTS", "MRT", "ARL"};

    public TweetPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ServiceAlertsFragment();
        } else {
            return TweetPageFragment.newInstance(position, "test");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
