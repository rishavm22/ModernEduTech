package com.modernedutech.LoginSet.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    private  final List<Fragment> lstFragment = new ArrayList<>();
    private  final List<String> lstTitle = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lstFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitle.get(position);
    }
    public void  AddFragment(Fragment fragment, String title)
    {
        lstFragment.add(fragment);
        lstTitle.add(title);
    }
}
