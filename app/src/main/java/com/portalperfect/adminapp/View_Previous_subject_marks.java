package com.portalperfect.adminapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class View_Previous_subject_marks extends AppCompatActivity {


    TextView tv_selected_sub;
    //----
    ArrayList<HashMap<String, String>> pendingList;

    HashMap<String, String> jobs;

    SimpleAdapter mSchedule;

    JSONObject firstRoute;

    MyUtility mu;
    String url_previoussubject,groupname,subject,date_selected;
    ListView lv_previous_subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityprevious_subject_marks);

        //http://portalperfect.com/achievers/Models/ViewResultBySubject.php?groupname=Girls&subject=xyz&examdate=2018-11-18

        mu=new MyUtility();
        lv_previous_subject=(ListView)findViewById(R.id.lv_previous_subject);

        tv_selected_sub=(TextView)findViewById(R.id.tv_selected_sub);

        SharedPreferences sp_clientid = View_Previous_subject_marks.this.getSharedPreferences("GROUPNAME", 0);
        groupname = sp_clientid.getString("selected_group_name", " ");


        SharedPreferences sp_subject = View_Previous_subject_marks.this.getSharedPreferences("PREVIOUS_SUBJECT_DATE", 0);
        subject = sp_subject.getString("selected_subject", " ");
        date_selected = sp_subject.getString("selected_date", " ");


        tv_selected_sub.setText("Showing Results of "+subject +" held on : "+date_selected +" of Group : "+groupname);
        url_previoussubject="http://portalperfect.com/achievers/Models/ViewResultBySubject.php?groupname="+groupname.replaceAll(" ","%20")
        +"&subject="+subject.replaceAll(" ","%20")+"&examdate="+date_selected;

        Log.e("url_previoussubject",""+url_previoussubject);

        if(mu.isInternetAvailable(View_Previous_subject_marks.this)==true){


            ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_previoussubject);
            complainasync.execute();

        }
        else{
            Toast.makeText(View_Previous_subject_marks.this, "Please connect to Internet.", Toast.LENGTH_LONG).show();

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
                Log.i("Response from url = ", res);

            } catch (Exception e) {
                // Log.i("log_tag", "Error converting resulturl " + e.toString());
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

            JSONArray sucess = (JSONArray) json.get("resulbysubject");
//            Log.e("jsonARRay  =", "" + sucess);
            Log.e("ARRAY LENGTH  =", "" + sucess.length());

            if (sucess.length() == 0) {
                Toast.makeText(View_Previous_subject_marks.this, "No Data Found!", Toast.LENGTH_LONG).show();
            }

            pendingList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i <= sucess.length(); i++) {

                jobs = new HashMap<String, String>();

                firstRoute = sucess.getJSONObject(i);
                Log.e("marks =", "" + (firstRoute.getString("marks")));


               // jobs.put("fullname","Name : "+(firstRoute.getString("fullname")) );
                jobs.put("marks","Name : "+(firstRoute.getString("fullname")) +"\n"+ "marks : "+(firstRoute.getString("marks")));

                jobs.put("outof", "Out Of : "+(firstRoute.getString("outof")));


                pendingList.add(jobs);

                String[] from = {"marks", "outof" };


                int[] to = {R.id.tv_subject, R.id.tv_date };

                mSchedule = new SimpleAdapter(View_Previous_subject_marks.this, pendingList, R.layout.custom_previous_result, from, to);


                lv_previous_subject.setAdapter(mSchedule);
                Log.e("after setAdapter  =", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
