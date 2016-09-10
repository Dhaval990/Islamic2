package com.islamicbookslibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.islamicbookslibrary.BaseActivity;
import com.islamicbookslibrary.R;
import com.islamicbookslibrary.fragment.AddBookFragment;
import com.islamicbookslibrary.fragment.BooksListFragment;
import com.islamicbookslibrary.util.Util;

public class AdminMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_book_list) {
            Util.changeFragment(this, R.id.frm_admin, new BooksListFragment(), false, BooksListFragment.TAG);
        } else if (id == R.id.nav_add_book) {
            Util.changeFragment(this, R.id.frm_admin, new AddBookFragment(), false, AddBookFragment.TAG);

        } else if (id == R.id.nav_sign_out) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminMainActivity.this, UserLoginActivity.class));
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initVariable() {

    }

    @Override
    public void findViews() {
        drawer = (DrawerLayout) links(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public void postInitView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void loadData() {
        Util.changeFragment(this, R.id.frm_admin, new BooksListFragment(), false, BooksListFragment.TAG);

    }

    @Override
    public void addAdapter() {

    }
}
