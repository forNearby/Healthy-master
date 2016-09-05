package android.microanswer.healthy.adapter;

import android.content.Context;
import android.microanswer.healthy.fragment.HealthyFragment;
import android.microanswer.healthy.fragment.LifeFragment;
import android.microanswer.healthy.fragment.MoreFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Micro on 2016/6/21.
 */

@Deprecated
public class MainViewPagerAdapter_NoUse extends FragmentPagerAdapter {
    private Context context;
    private Fragment[] fragments;

    public MainViewPagerAdapter_NoUse(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        fragments = new Fragment[3];
        fragments[0] = new HealthyFragment();
        fragments[1] = new LifeFragment();
        fragments[2] = new MoreFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
