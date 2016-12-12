package com.awesome.smarthealthmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by yoonjae on 29/11/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RootFragment tab1 = new RootFragment();
                return tab1;
            case 1:
                PersonalSymptomFragment tab2 = new PersonalSymptomFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }
}
