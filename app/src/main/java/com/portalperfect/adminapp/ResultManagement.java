package com.portalperfect.adminapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class ResultManagement extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ListView lv_result;
    String[] listItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // TO REMOVE DEFAULT TEXT IN TOOLBAR
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lv_result=(ListView)findViewById(R.id.lv_result);

        listItem = getResources().getStringArray(R.array.array_technology);
        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, listItem){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);

                return view;
            }
        };

        lv_result.setAdapter(arrayAdapter);

        lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub

                if (position==0){

                    Intent it = new Intent(ResultManagement.this,Result_add_Group.class);
                    startActivity(it);

                }

              else  if (position==1){

                    Intent it = new Intent(ResultManagement.this,Result_View_Groups.class);
                    startActivity(it);

                }



            }
        });
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
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id== R.id.nav_uploadvideo){
            Intent it=new Intent(ResultManagement.this,UploadVLink.class);
            startActivity(it);
        }
        else if (id == R.id.nav_export_stu) {

            String nav_export_stu="student";
            SharedPreferences sp_user_name = ResultManagement.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(ResultManagement.this,Webview.class);
            startActivity(it);

        } else if (id == R.id.nav_export_fees) {
            String nav_export_stu="fees";
            SharedPreferences sp_user_name = ResultManagement.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(ResultManagement.this,Webview.class);
            startActivity(it);
        } else if (id == R.id.nav_export_result) {
            String nav_export_stu="result";
            SharedPreferences sp_user_name = ResultManagement.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(ResultManagement.this,Webview.class);
            startActivity(it);
        } else if (id == R.id.nav_logout) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    ResultManagement.this);

            alertDialog.setTitle("Leave application?");

            alertDialog.setMessage("Are you sure you want to leave the application?");

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            boolean loggedin=false;
                            SharedPreferences sp_user_name = ResultManagement.this.getSharedPreferences("KEY_LOGGDIN", 0);
                            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

                            spe_user_name.putBoolean("loggedin", loggedin);

                            spe_user_name.commit();

                            Intent it=new Intent(ResultManagement.this,LoginScreen.class);
                            startActivity(it);
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


        } else if (id == R.id.nav_share) {
            final String appPackageName =  getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out This App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_exit) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    ResultManagement.this);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
