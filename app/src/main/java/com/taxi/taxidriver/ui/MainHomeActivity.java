package com.taxi.taxidriver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.taxi.taxidriver.R;
import com.taxi.taxidriver.constant.Constant;
import com.taxi.taxidriver.ui.activity.EditProfileActivity;
import com.taxi.taxidriver.ui.fragment.BookingFragment;
import com.taxi.taxidriver.ui.fragment.DashboardFragment;
import com.taxi.taxidriver.ui.fragment.HistoryFragment;
import com.taxi.taxidriver.ui.fragment.ProfileFragment;
import com.taxi.taxidriver.utils.BaseActivity;
import com.taxi.taxidriver.utils.FragmentUtils;

public class MainHomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static FragmentManager fragmentManager;
    public static FragmentUtils fragmentUtils;
    public static TextView tvEditProfile;
    public static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvEditProfile = findViewById(R.id.tvEditProfile);
        tvEditProfile.setVisibility(View.GONE);
        tvEditProfile.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentUtils = new FragmentUtils(fragmentManager);
        toolbar.setTitle("Home");
        fragmentUtils.replaceFragment(new DashboardFragment(), Constant.DashboardFragment, R.id.main_frame);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.icDashBoard) {
            toolbar.setTitle(Constant.DashboardFragment);
            fragmentUtils.replaceFragment(new DashboardFragment(), Constant.DashboardFragment, R.id.main_frame);
        } else if (id == R.id.icProfile) {
            toolbar.setTitle(Constant.ProfileFragment);
            fragmentUtils.replaceFragment(new ProfileFragment(), Constant.ProfileFragment, R.id.main_frame);
        } else if (id == R.id.icMyBookings) {
            toolbar.setTitle(Constant.BookingFragment);
            fragmentUtils.replaceFragment(new BookingFragment(), Constant.BookingFragment, R.id.main_frame);
        } else if (id == R.id.icHistory) {
            toolbar.setTitle(Constant.HistoryFragment);
            fragmentUtils.replaceFragment(new HistoryFragment(), Constant.HistoryFragment, R.id.main_frame);
        } else if (id == R.id.icShare) {
            toolbar.setTitle("Home");
            fragmentUtils.replaceFragment(new DashboardFragment(), Constant.DashboardFragment, R.id.main_frame);
        } else if (id == R.id.icLogout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvEditProfile:
                startActivity(new Intent(mContext, EditProfileActivity.class));
                break;

        }
    }
}
