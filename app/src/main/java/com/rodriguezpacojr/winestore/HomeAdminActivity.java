package com.rodriguezpacojr.winestore;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.rodriguezpacojr.winestore.admin.ListCustomersFragment;
import com.rodriguezpacojr.winestore.admin.ListEmployeesFragment;
import com.rodriguezpacojr.winestore.admin.ListProductsFragment;
import com.rodriguezpacojr.winestore.admin.ListRoutesFragment;

public class HomeAdminActivity extends AppCompatActivity {

    private static final String TAG = "HomeAdminActivity";

    private TabLayout mTabLayout;
    private int[] tabIcons = {R.drawable.ic_people_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_store_black_24dp, R.drawable.ic_routes_black_24dp};
    private ViewPager mViewPager;
    public static int mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        loadViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        tabIcons();
        mViewPager.setCurrentItem(mNumber);

        iconColor(mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()), "#1B02F8");
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                iconColor(tab, "#1B02F8");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                iconColor(tab, "#C6C6C6");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void iconColor(TabLayout.Tab tab, String color) {
        tab.getIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
    }

    private void loadViewPager(ViewPager mViewPager) {
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(getListCustomersFragment());
        mViewPagerAdapter.addFragment(getListEmployeesFragment());
        mViewPagerAdapter.addFragment(getListProductsFragment());
        mViewPagerAdapter.addFragment(getListRoutesFragment());
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void tabIcons() {
        for (int i=0; i<4; i++)
            mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
    }

    private ListCustomersFragment getListCustomersFragment() {
        ListCustomersFragment listCustomersFragment = new ListCustomersFragment();
        return listCustomersFragment;
    }

    private ListEmployeesFragment getListEmployeesFragment() {
        ListEmployeesFragment listEmployeesFragment = new ListEmployeesFragment();
        return listEmployeesFragment;
    }

    private ListProductsFragment getListProductsFragment() {
        ListProductsFragment listProductsFragment = new ListProductsFragment();
        return listProductsFragment;
    }

    private ListRoutesFragment getListRoutesFragment() {
        ListRoutesFragment listRoutesFragment = new ListRoutesFragment();
        return listRoutesFragment;
    }
}