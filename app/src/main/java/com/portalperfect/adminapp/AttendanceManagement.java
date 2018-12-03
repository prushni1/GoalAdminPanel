package com.portalperfect.adminapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceManagement extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String url_allgroups = "http://portalperfect.com/achievers/Models/ViewResultGroup.php";
    //----
    ArrayList<HashMap<String, String>> pendingList;

    HashMap<String, String> jobs;

    SimpleAdapter mSchedule;

    JSONObject firstRoute;

    //----
    String user_id, service_url;
    MyUtility mu;
    JsonArrayRequest jsonArrayRequest;

    RequestQueue requestQueue;
    ListView lv_allgroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mu = new MyUtility();

        lv_allgroups = (ListView) findViewById(R.id.lv_allgroups);

        Log.e("",""+url_allgroups);

        if(mu.isInternetAvailable(AttendanceManagement.this)==true){

            Log.e("url_allgroups",""+url_allgroups);
             ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_allgroups);
            complainasync.execute();

        }
        else{
            Toast.makeText(AttendanceManagement.this, "Please connect to Internet.", Toast.LENGTH_LONG).show();

        }


        lv_allgroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String selected_group_id = ((TextView) view.findViewById(R.id.grp_id)).getText().toString();

                String selected_group_name= ((TextView) view.findViewById(R.id.grp_name)).getText().toString();

                // Log.e("selected_group_name", " " + selected_group_name);

                SharedPreferences sp_user_name = AttendanceManagement.this.getSharedPreferences("AttendnceGROUPNAME", 0);
                SharedPreferences.Editor spe_user_name = sp_user_name.edit();
                spe_user_name.putString("attendnce_group_name", selected_group_name);
                spe_user_name.commit();

                Intent it = new Intent(AttendanceManagement.this, AddAttendance.class);
                startActivity(it);

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
            Intent it=new Intent(AttendanceManagement.this,HomeScreen.class);
            startActivity(it);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id== R.id.nav_uploadvideo){
            Intent it=new Intent(AttendanceManagement.this,UploadVLink.class);
            startActivity(it);
        }
        else if (id == R.id.nav_export_stu) {

            String nav_export_stu="student";
            SharedPreferences sp_user_name = AttendanceManagement.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(AttendanceManagement.this,Webview.class);
            startActivity(it);

        } else if (id == R.id.nav_export_fees) {
            String nav_export_stu="fees";
            SharedPreferences sp_user_name = AttendanceManagement.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(AttendanceManagement.this,Webview.class);
            startActivity(it);
        } else if (id == R.id.nav_export_result) {
            String nav_export_stu="result";
            SharedPreferences sp_user_name = AttendanceManagement.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(AttendanceManagement.this,Webview.class);
            startActivity(it);
        } else if (id == R.id.nav_logout) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    AttendanceManagement.this);

            alertDialog.setTitle("Leave application?");

            alertDialog.setMessage("Are you sure you want to leave the application?");

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            boolean loggedin=false;
                            SharedPreferences sp_user_name = AttendanceManagement.this.getSharedPreferences("KEY_LOGGDIN", 0);
                            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

                            spe_user_name.putBoolean("loggedin", loggedin);

                            spe_user_name.commit();

                            Intent it=new Intent(AttendanceManagement.this,LoginScreen.class);
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
                    AttendanceManagement.this);

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
    // -- ASYNC
    private class ComplainDetailAsync extends AsyncTask<Object, Object, Object> {
        private String url_registeration;
        private InputStream is;
        private String res;

        public ComplainDetailAsync(String url_registeration) {
            this.url_registeration = url_registeration;
        }

        @Override
        protected Object doInBackground(Object... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(url_registeration);


                urlConnection = (HttpURLConnection) url.openConnection();
                is = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(is);


            } catch (Exception e) {
                Log.e("log_tag", "Error in http conection  " + e.toString());
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                res = sb.toString();
                Log.i("Response from url = ", res);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting resulturl " + e.toString());
            }
            return res;
        }

        @Override
        protected void onPostExecute(Object result11) {
            super.onPostExecute(result11);
            if(result11!=null){
                returnResultUrl(res);
            }


        }
    }

    private void returnResultUrl(String res) {
        parseJsonUrl(res);

    }

    private void parseJsonUrl(String res) {
        try {
            // prog_dialog.dismiss();


            JSONObject json = new JSONObject(res);

            JSONArray sucess = (JSONArray) json.get("resultgrouplist");
//            Log.e("jsonARRay  =", "" + sucess);
//            Log.e("ARRAY LENGTH  =", "" + sucess.length());

            if (sucess.length() == 0) {
                Toast.makeText(AttendanceManagement.this, "Error!", Toast.LENGTH_LONG).show();
            }

            pendingList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i <= sucess.length(); i++) {

                jobs = new HashMap<String, String>();

                firstRoute = sucess.getJSONObject(i);
                Log.e("grp name  =", "" + (firstRoute.getString("groupname")));


                jobs.put("rgid", String.valueOf(firstRoute.getString("rgid")));

                jobs.put("groupname", (firstRoute.getString("groupname")));


                pendingList.add(jobs);

                String[] from = {"rgid", "groupname" };


                int[] to = {R.id.grp_id, R.id.grp_name };

                mSchedule = new SimpleAdapter(AttendanceManagement.this, pendingList, R.layout.custom_result_viewgroup, from, to);


                lv_allgroups.setAdapter(mSchedule);
                Log.e("after setAdapter  =", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
