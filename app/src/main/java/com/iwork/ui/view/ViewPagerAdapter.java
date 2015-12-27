package com.iwork.ui.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iwork.model.IndustryListModel;
import com.iwork.ui.fragment.SampleFragment;

import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 8;
    private List<IndustryListModel.Industry> titles;

    public ViewPagerAdapter(FragmentManager fm, List<IndustryListModel.Industry> titles2) {
        super(fm);
        titles = titles2;

    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            // Open FragmentTab1.java
//            case 0:
//                return SampleFragment.newInstance(position);
//            case 1:
//                return SampleFragment.newInstance(position);
//            case 2:
//                return SampleFragment.newInstance(position);
//            case 3:
//                return SampleFragment.newInstance(position);
//            case 4:
//                return SampleFragment.newInstance(position);
//            case 5:
//                return SampleFragment.newInstance(position);
//            case 6:
//                return SampleFragment.newInstance(position);
//            case 7:
        return SampleFragment.newInstance(position);

//        }
//        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles.get(position).getName();
    }

    @Override
    public int getCount() {
        return titles.size();
    }

}