package com.ryeeeeee.faceandflacdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ryeeeeee.faceandflacdemo.faceplusplus.FaceFragment;
import com.ryeeeeee.faceandflacdemo.flac.FlacFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryeeeeee on 1/12/16.
 */
public class QuestionPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public QuestionPagerAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
        initTitles();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    private void initFragments() {
        mFragments.add(FaceFragment.newInstance());
        mFragments.add(FlacFragment.newInstance());
    }

    private void initTitles() {
        mTitles.add("Face++");
        mTitles.add("Flac");
    }
}
