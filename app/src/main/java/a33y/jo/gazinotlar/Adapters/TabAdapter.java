package a33y.jo.gazinotlar.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ahmed on 17/8/2017.
 */

public class TabAdapter extends FragmentPagerAdapter {
    Fragment[] fragments ;
    public TabAdapter(FragmentManager fm, Fragment[] fragments , Bundle bundle) {
        super(fm);
        this.fragments=fragments;
        for(Fragment f : fragments)
            f.setArguments(bundle);
    }
    public TabAdapter(FragmentManager fm, Fragment[] fragments ) {
        super(fm);
        this.fragments=fragments;

    }
    @Override
    public Fragment getItem(int position) {
        return  fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
