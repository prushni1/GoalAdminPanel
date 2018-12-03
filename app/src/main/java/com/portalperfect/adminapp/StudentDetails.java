package com.portalperfect.adminapp;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class StudentDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progDialog;
    String url_upload_marks;
    TextView tv_studentid,tv_group_name,tv_student_name,tv_studentmobile,tv_parentmobile,tv_email;
      HashMap<String, String> jobs;

    SimpleAdapter mSchedule;

    JSONObject firstRoute;
    String student_url,selected_group_id,selected_student_id,selected_group_name;
    LinearLayout contactlinearlyout;

    String studentmobile,parentmobile,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp_clientid = StudentDetails.this.getSharedPreferences("SELECTED_STUDENT", 0);
        selected_group_id = sp_clientid.getString("selected_group_id", " ");
        selected_student_id = sp_clientid.getString("selected_student_id", " ");
        selected_group_name = sp_clientid.getString("selected_group_name", " ");

        tv_studentid=(TextView)findViewById(R.id.tv_studentid);
        tv_group_name=(TextView)findViewById(R.id.tv_group_name);
        tv_student_name=(TextView)findViewById(R.id.tv_student_name);
        tv_studentmobile=(TextView)findViewById(R.id.tv_studentmobile);
        tv_parentmobile=(TextView)findViewById(R.id.tv_parentmobile);
        tv_email=(TextView)findViewById(R.id.tv_email);


        student_url="http://portalperfect.com/achievers/Models/ViewStudentDetail.php?sid="+selected_student_id;

        contactlinearlyout=(LinearLayout)findViewById(R.id.contactlinearlyout);



        ComplainDetailAsync complainasync = new ComplainDetailAsync(student_url);
        complainasync.execute();
        progDialog = new ProgressDialog(StudentDetails.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progDialog.setMessage("Please Wait.. .");
        progDialog.setCancelable(false);

        progDialog.show();




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
            Intent it=new Intent(StudentDetails.this,UploadVLink.class);
            startActivity(it);
        }
        else if (id == R.id.nav_export_stu) {

            String nav_export_stu="student";
            SharedPreferences sp_user_name = StudentDetails.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(StudentDetails.this,Webview.class);
            startActivity(it);

        } else if (id == R.id.nav_export_fees) {
            String nav_export_stu="fees";
            SharedPreferences sp_user_name = StudentDetails.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(StudentDetails.this,Webview.class);
            startActivity(it);
        } else if (id == R.id.nav_export_result) {
            String nav_export_stu="result";
            SharedPreferences sp_user_name = StudentDetails.this.getSharedPreferences("SELECTED_NAV", 0);
            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

            spe_user_name.putString("nav_Selection", nav_export_stu);

            spe_user_name.commit();
            Intent it=new Intent(StudentDetails.this,Webview.class);
            startActivity(it);
        } else if (id == R.id.nav_logout) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    StudentDetails.this);

            alertDialog.setTitle("Leave application?");

            alertDialog.setMessage("Are you sure you want to leave the application?");

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            boolean loggedin=false;
                            SharedPreferences sp_user_name = StudentDetails.this.getSharedPreferences("KEY_LOGGDIN", 0);
                            SharedPreferences.Editor spe_user_name = sp_user_name.edit();

                            spe_user_name.putBoolean("loggedin", loggedin);

                            spe_user_name.commit();

                            Intent it=new Intent(StudentDetails.this,LoginScreen.class);
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
                    StudentDetails.this);

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

    //--ASYNC






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
                Log.i("log_tag", "Error in http conection  " + e.toString());
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
                Log.i("log_tag", "Error converting resulturl " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result11) {
            super.onPostExecute(result11);
            returnResultUrl(res);

        }
    }

    private void returnResultUrl(String res) {
        Log.e("Result = ", res);
        parseJsonUrl(res);

    }

    private void parseJsonUrl(String res) {
        try {

            progDialog.dismiss();
            JSONObject json = new JSONObject(res);

//            String code = json.getString("result");
//            Log.e("code  =", "" + code);



            String student_id = String.valueOf(json.getString("sid"));
            String fullname = String.valueOf(json.getString("fullname"));
            String groupname = String.valueOf(json.getString("groupname"));
            parentmobile = String.valueOf(json.getString("parentmobile"));
            email = String.valueOf(json.getString("email"));
            String course = String.valueOf(json.getString("course"));
            studentmobile = String.valueOf(json.getString("studentmobile"));


            tv_studentid.setText("Student Id : "+student_id);
            tv_group_name.setText("Group Name : "+groupname);
            tv_student_name.setText("Name : "+fullname);

            tv_studentmobile.setText("Mobile: "+studentmobile);
            tv_parentmobile.setText("Parent's Mobile : "+parentmobile);
            tv_email.setText(email);
            Log.e("S details -- ", " " + student_id + " " + fullname + " " + course);






        } catch(JSONException e){

            e.printStackTrace();
        }



    }




    //-----------------------

    // -- ASYNC
    private class UploadAsyncTask extends AsyncTask<Object, Object, Object> {
        private String url_registeration;
        private InputStream is;
        private String res;

        public UploadAsyncTask(String url_registeration) {
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
                Log.i("log_tag", "Error in http conection  " + e.toString());
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
                Log.i("log_tag", "Error converting resulturl " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result11) {
            super.onPostExecute(result11);
            returnResultUrl1(res);

        }
    }

    private void returnResultUrl1(String res) {
        Log.e("Result = ", res);
        parseJsonUrl1(res);

    }

    private void parseJsonUrl1(String res) {
        try {
            JSONObject json = new JSONObject(res);

            String code = json.getString("result");
            Log.e("RESULT RESULT  =", "" + code);



            if(code.equalsIgnoreCase("true")){
                Toast.makeText(StudentDetails.this,"ADDED MARKS",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(StudentDetails.this,"Couldnot Add Marks",Toast.LENGTH_LONG).show();

            }


        } catch(JSONException e){

            e.printStackTrace();
        }




    }
}
