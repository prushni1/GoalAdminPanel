package com.portalperfect.adminapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.List;

public class ViewAllStudents extends AppCompatActivity {
    ArrayList<HashMap<String, String>> pendingList;

    //--- THIS WILL SHOW ONE GROUPS ALL STUDENTS--> ON CLICK --> STUDENT DETAIL
    HashMap<String, String> jobs;

    String selected_menu;
    ProgressDialog progDialog;
    SimpleAdapter mSchedule;

    JSONObject firstRoute;
    String user_id, service_url;
    MyUtility mu;
    JsonArrayRequest jsonArrayRequest;

    Button btn_previous_results;
    RequestQueue requestQueue;
    ListView lv_students;
    Button btn_result, btn_home,btn_addresults;
    String url_grp_Students;
    String selected_group_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_students);

        mu = new MyUtility();

        btn_previous_results=(Button)findViewById(R.id.btn_previous_results);
        lv_students = (ListView) findViewById(R.id.lv_allgroups);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_result = (Button) findViewById(R.id.btn_result);
        btn_addresults=(Button)findViewById(R.id.btn_addresults);


        btn_previous_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewAllStudents.this, ViewPreviousResults.class);
                startActivity(it);
            }
        });
        btn_addresults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(ViewAllStudents.this, AddResultsPage.class);
                startActivity(it);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewAllStudents.this, HomeScreen.class);
                startActivity(it);
            }
        });
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ViewAllStudents.this, ResultManagement.class);
                startActivity(it);
            }
        });

        SharedPreferences sp_clientid = ViewAllStudents.this.getSharedPreferences("GROUPNAME", 0);
        selected_group_name = sp_clientid.getString("selected_group_name", " ");


        url_grp_Students="http://portalperfect.com/achievers/Models/ViewResultGroupByName.php?groupname="+selected_group_name.replaceAll(" ","%20");

        Log.e("Students in group",""+url_grp_Students);

        ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_grp_Students);
        complainasync.execute();

        progDialog = new ProgressDialog(ViewAllStudents.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progDialog.setMessage("Please Wait.. .");
        progDialog.setCancelable(false);

        progDialog.show();

        lv_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String selected_group_id = ((TextView) view.findViewById(R.id.group_selected_id)).getText().toString();

                String selected_group_name= ((TextView) view.findViewById(R.id.selected_grp_name)).getText().toString();
                String selected_student_id= ((TextView) view.findViewById(R.id.grp_stu_id)).getText().toString();

                Log.e("selected_group_name", " " + selected_group_name);

                SharedPreferences sp_user_name = ViewAllStudents.this.getSharedPreferences("SELECTED_STUDENT", 0);
                SharedPreferences.Editor spe_user_name = sp_user_name.edit();
                spe_user_name.putString("selected_group_id", selected_group_id);
                spe_user_name.putString("selected_group_name", selected_group_name);
                spe_user_name.putString("selected_student_id", selected_student_id);

                spe_user_name.commit();



//                Intent it=new Intent(ViewAllStudents.this,StudentDetails.class);
//                startActivity(it);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewallstudent, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deletestudetns:


                selected_menu="delte";
                SharedPreferences sp_user_name = ViewAllStudents.this.getSharedPreferences("ADDORDELETE", 0);
                SharedPreferences.Editor spe_user_name = sp_user_name.edit();
                spe_user_name.putString("selected_menu", selected_menu);
                spe_user_name.commit();

                Intent it=new Intent(ViewAllStudents.this,DeleteStudents.class);
                startActivity(it);

                Toast.makeText(this, "Delete students", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.addstudents:

                Toast.makeText(this, "Add new Students", Toast.LENGTH_SHORT).show();


                selected_menu="addstudents";
                SharedPreferences sp_user_menu = ViewAllStudents.this.getSharedPreferences("ADDORDELETE", 0);
                SharedPreferences.Editor spe_user_menu = sp_user_menu.edit();
                spe_user_menu.putString("selected_menu", selected_menu);
                spe_user_menu.commit();

                Intent it1=new Intent(ViewAllStudents.this,DeleteStudents.class);
                startActivity(it1);

                return true;


            case R.id.deletegroup:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        ViewAllStudents.this);

                alertDialog.setTitle("Delete Whole Group?");

                alertDialog.setMessage("This cannot be Undone");

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String delete_url="http://portalperfect.com/achievers/Models/DeleteGroup.php?groupname="+selected_group_name.replaceAll(" ","%20");

                                Log.e("delete_url"," "+delete_url);

                                DeleteAsyncTask deletsync = new  DeleteAsyncTask(delete_url);
                                deletsync.execute();

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
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
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

            JSONArray sucess = (JSONArray) json.get("resultgroupbynamelist");
            Log.e("jsonARRay  =", "" + sucess);
            Log.e("ARRAY LENGTH  =", "" + sucess.length());

            if (sucess.length() == 0) {
                Toast.makeText(ViewAllStudents.this, "Error!", Toast.LENGTH_LONG).show();
            }

            pendingList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i <= sucess.length(); i++) {

                jobs = new HashMap<String, String>();

                firstRoute = sucess.getJSONObject(i);
                Log.e("grp name  =", "" + (firstRoute.getString("groupname")));


                jobs.put("rgid", String.valueOf(firstRoute.getString("rgid")));

                jobs.put("groupname", (firstRoute.getString("groupname")));
                jobs.put("sid", (firstRoute.getString("sid")));
                jobs.put("fullname", (firstRoute.getString("fullname")));


                pendingList.add(jobs);

                String[] from = {"rgid", "groupname","sid","fullname" };


                int[] to = {R.id.group_selected_id, R.id.selected_grp_name,R.id.grp_stu_id ,R.id.tv_grp_studentname};

                mSchedule = new SimpleAdapter(ViewAllStudents.this, pendingList, R.layout.custom_studentlist, from, to);

                lv_students.setAdapter(mSchedule);
                Log.e("after setAdapter  =", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }













    //--DELETE ASYNC

    private class DeleteAsyncTask extends AsyncTask<Object, Object, Object> {
        private String url_registeration;
        private InputStream is;
        private String res;

        public DeleteAsyncTask(String url_registeration) {
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
            Log.e("code  =", "" + code);

            if (code.equalsIgnoreCase("true")){
                Toast.makeText(ViewAllStudents.this,"All Changes Made !",Toast.LENGTH_LONG).show();

                Intent it=new Intent(ViewAllStudents.this,Result_View_Groups.class);
                startActivity(it);

            }


        } catch(JSONException e){

            e.printStackTrace();
        }



    }
}
