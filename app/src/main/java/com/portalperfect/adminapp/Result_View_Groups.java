package com.portalperfect.adminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Result_View_Groups extends AppCompatActivity {

    ProgressDialog prog_dialog;
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
    Button btn_result, btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result__view__groups);

        mu = new MyUtility();

        lv_allgroups = (ListView) findViewById(R.id.lv_allgroups);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_result = (Button) findViewById(R.id.btn_result);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Result_View_Groups.this, HomeScreen.class);
                startActivity(it);
            }
        });
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Result_View_Groups.this, ResultManagement.class);
                startActivity(it);
            }
        });

        lv_allgroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String selected_group_id = ((TextView) view.findViewById(R.id.grp_id)).getText().toString();

                String selected_group_name= ((TextView) view.findViewById(R.id.grp_name)).getText().toString();

               // Log.e("selected_group_name", " " + selected_group_name);

                SharedPreferences sp_user_name = Result_View_Groups.this.getSharedPreferences("GROUPNAME", 0);
                SharedPreferences.Editor spe_user_name = sp_user_name.edit();
                spe_user_name.putString("selected_group_name", selected_group_name);
                spe_user_name.commit();

                Intent it = new Intent(Result_View_Groups.this, ViewAllStudents.class);
                startActivity(it);

            }
        });

//        prog_dialog = new ProgressDialog(Result_View_Groups.this);
//
//        prog_dialog.setMessage("Please Wait....");
//        prog_dialog.setTitle("Achiever's Academy");
//        prog_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        prog_dialog.show();


        Log.e("",""+url_allgroups);

        if(mu.isInternetAvailable(Result_View_Groups.this)==true){

            Log.e("url_allgroups",""+url_allgroups);
            ComplainDetailAsync complainasync = new ComplainDetailAsync(url_allgroups);
            complainasync.execute();

        }
        else{
            Toast.makeText(Result_View_Groups.this, "Please connect to Internet.", Toast.LENGTH_LONG).show();

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
                Toast.makeText(Result_View_Groups.this, "Error!", Toast.LENGTH_LONG).show();
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

                mSchedule = new SimpleAdapter(Result_View_Groups.this, pendingList, R.layout.custom_result_viewgroup, from, to);


                lv_allgroups.setAdapter(mSchedule);
                Log.e("after setAdapter  =", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

            Intent it=new Intent(Result_View_Groups.this,HomeScreen.class);
            startActivity(it);

    }

}
