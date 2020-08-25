package com.modernedutech.LoginSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.modernedutech.Adapter.ViewPagerAdapter;
import com.modernedutech.LoginSet.Fragments.ForgFragment;
import com.modernedutech.LoginSet.Fragments.LogFragment;
import com.modernedutech.R;
import com.modernedutech.LoginSet.Fragments.RegFragment;

import java.util.ArrayList;

public class LogsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        //tab view
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new RegFragment(), "Registration");
        viewPagerAdapter.addFragment(new LogFragment(), "Login");
        viewPagerAdapter.addFragment(new ForgFragment(),"Forget Password");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        //drawerlayout

    }






}