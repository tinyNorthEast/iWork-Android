package com.iwork.ui.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iwork.model.IndustryListModel;
import com.iwork.ui.fragment.SampleFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 8;
    private List<IndustryListModel.Industry> titles= Collections.emptyList();
    private final List<SampleFragment> mFragments = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm, List<IndustryListModel.Industry> titles2) {
        super(fm);
        titles = titles2;

    }

    @Override
    public Fragment getItem(int position) {
        return SampleFragment.newInstance(titles.get(position).getObjId());
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public CharSequence getPageTitle(int position) {
        return titles.get(position).getName();
    }

    @Override
    public int getCount() {
        return titles.size();
    }

}