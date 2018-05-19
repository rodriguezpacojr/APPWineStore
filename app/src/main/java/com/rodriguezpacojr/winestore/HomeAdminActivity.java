package com.rodriguezpacojr.winestore;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rodriguezpacojr.winestore.adapters.CustomerListAdapter;
import com.rodriguezpacojr.winestore.adapters.EmployeesListAdapter;
import com.rodriguezpacojr.winestore.adapters.ProductsListAdapter;
import com.rodriguezpacojr.winestore.adapters.RoutesListAdapter;
import com.rodriguezpacojr.winestore.admin.CRUDCustomer;
import com.rodriguezpacojr.winestore.admin.CRUDEmployee;
import com.rodriguezpacojr.winestore.admin.CRUDProduct;
import com.rodriguezpacojr.winestore.admin.CRUDRoute;
import com.rodriguezpacojr.winestore.admin.ListCustomersFragment;
import com.rodriguezpacojr.winestore.admin.ListEmployeesFragment;
import com.rodriguezpacojr.winestore.admin.ListProductsFragment;
import com.rodriguezpacojr.winestore.admin.ListRoutesFragment;
import com.rodriguezpacojr.winestore.helpers.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdminActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private int[] tabIcons = {R.drawable.ic_people_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_store_black_24dp, R.drawable.ic_routes_black_24dp};
    public static int mNumber, currentFragment=0;
    private android.app.ProgressDialog progressBar;

    @BindView(R.id.btnAddItem) FloatingActionButton fab_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        ButterKnife.bind(this);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);


        loadViewPager(mViewPager);
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

        fab_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                currentFragment = mViewPager.getCurrentItem();
                switch (currentFragment){
                    case 0:
                        CustomerListAdapter.flagUpdate = false;
                        Intent i0 = new Intent(getApplicationContext(), CRUDCustomer.class);
                        startActivity(i0);
                        break;
                    case 1:
                        EmployeesListAdapter.flagUpdate = false;
                        Intent i1 = new Intent(getApplicationContext(), CRUDEmployee.class);
                        startActivity(i1);
                        break;
                    case 2:
                        ProductsListAdapter.flatUpdate = false;
                        Intent i2 = new Intent(getApplicationContext(), CRUDProduct.class);
                        startActivity(i2);
                        break;
                    case 3:
                        RoutesListAdapter.flatUpdate = false;
                        Intent i3 = new Intent(getApplicationContext(), CRUDRoute.class);
                        startActivity(i3);
                        break;
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                progressBar = new android.app.ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("LogOut...");
                progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.cancel();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
                break;
            case R.id.menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeAdminActivity.this, R.style.AppTheme_AlertDialog);
                LayoutInflater inflater = HomeAdminActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.about_of, null))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //
                            }
                        }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}