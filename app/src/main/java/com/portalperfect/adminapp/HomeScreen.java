package com.portalperfect.adminapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.portalperfect.adminapp.adapters.Person;
import com.portalperfect.adminapp.adapters.RVadapter;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MyUtility mu;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RVadapter adapter;


    List<Person> persons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mu=new MyUtility();

        // TO REMOVE DEFAULT TEXT IN TOOLBAR
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);


        initdata();

        initadap();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    HomeScreen.this);

            alertDialog.setTitle("Leave application?");

            alertDialog.setMessage("Are you sure you want to leave the application?");

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }
    }



    private void initdata() {


        persons=new ArrayList<>();

        persons.add(new Person(R.mipmap.img_employe));
        persons.add(new Person(R.mipmap.img_reslt));
        persons.add(new Person(R.mipmap.img_attendnc));
        persons.add(new Person(R.mipmap.img_fees));

        persons.add(new Person(R.mipmap.img_set_goal));
        persons.add(new Person(R.mipmap.img_news
        ));



    }
    private void initadap() {
        adapter=new RVadapter(persons);
        recyclerView.setAdapter(adapter);

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_export_stu) {
            
        } else if (id == R.id.nav_export_fees) {

        } else if (id == R.id.nav_export_result) {

        } else if (id == R.id.nav_logout) {

            boolean loggedin=false;
            SharedPreferences sp_user_name = HomeScreen.this.getSharedPreferences("KEY_LOGGDIN", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putBoolean("loggedin", loggedin);

            spe_user_name.commit();

            Intent it=new Intent(HomeScreen.this,LoginScreen.class);
            startActivity(it);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
