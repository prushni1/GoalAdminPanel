package com.portalperfect.adminapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class AttendanceDates extends AppCompatActivity {

    //----
    ArrayList<HashMap<String, String>> pendingList;

    HashMap<String, String> jobs;

    SimpleAdapter mSchedule;

    JSONObject firstRoute;

    ListView lv_previous;
    TextView tv_group;
    String selected_group_name,url_previous_results;
    MyUtility mu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_dates);



        SharedPreferences sp_clientid = AttendanceDates.this.getSharedPreferences("AttendnceGROUPNAME", 0);
        selected_group_name = sp_clientid.getString("attendnce_group_name", " ");

        //
        //http://portalperfect.com/achievers/Models/ViewAttendanceByGroup.php?groupname=AA
        url_previous_results="http://portalperfect.com/achievers/Models/ViewAttendanceByGroup.php?groupname="+selected_group_name.replaceAll(" ","%20");



        Toast.makeText(AttendanceDates.this,"Please Wait...",Toast.LENGTH_LONG).show();
        Log.e("DATES=",""+url_previous_results);

        lv_previous=(ListView) findViewById(R.id.lv_previous);

        lv_previous.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String selected_subject = ((TextView) view.findViewById(R.id.tv_subject)).getText().toString();

                String selected_date= ((TextView) view.findViewById(R.id.tv_date)).getText().toString();

                // Log.e("selected_group_name", " " + selected_group_name);

                SharedPreferences sp_user_name = AttendanceDates.this.getSharedPreferences("DATENGROUP", 0);
                SharedPreferences.Editor spe_user_name = sp_user_name.edit();
                spe_user_name.putString("date_date", selected_subject);
                spe_user_name.putString("date_group", selected_date);

                spe_user_name.commit();


//
//                Intent it = new Intent(AttendanceDates.this, View_Previous_subject_marks.class);
//                startActivity(it);

            }
        });
        mu=new MyUtility();

        if(mu.isInternetAvailable(AttendanceDates.this)==true){

             ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_previous_results);
            complainasync.execute();

        }
        else{
            Toast.makeText(AttendanceDates.this, "Please connect to Internet.", Toast.LENGTH_LONG).show();

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
                // Log.i("log_tag", "Error in http conection  " + e.toString());
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
                Log.e("Response from url = ", res);

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

            JSONArray sucess = (JSONArray) json.get("attendancebygroup");
//            Log.e("jsonARRay  =", "" + sucess);
            Log.e("ARRAY LENGTH  =", "" + sucess.length());

            if (sucess.length() == 0) {
                Toast.makeText(AttendanceDates.this, "No Data Found!", Toast.LENGTH_LONG).show();
            }

            pendingList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i <= sucess.length(); i++) {

                jobs = new HashMap<String, String>();

                firstRoute = sucess.getJSONObject(i);


                jobs.put("subject", String.valueOf(firstRoute.getString("date")));

                jobs.put("examdate", (firstRoute.getString("aid")));


                pendingList.add(jobs);

                String[] from = {"subject", "examdate" };


                int[] to = {R.id.tv_subject, R.id.tv_date };

                mSchedule = new SimpleAdapter(AttendanceDates.this, pendingList, R.layout.custom_previous_result, from, to);


                lv_previous.setAdapter(mSchedule);
                Log.e("after setAdapter  =", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
