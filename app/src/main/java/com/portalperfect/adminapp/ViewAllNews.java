package com.portalperfect.adminapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class ViewAllNews extends AppCompatActivity {

    //----
    ArrayList<HashMap<String, String>> pendingList;

    HashMap<String, String> jobs;

    SimpleAdapter mSchedule;

    JSONObject firstRoute;

    //----
    String url_news="http://portalperfect.com/achievers/Models/ViewNews.php";
    ListView lv_allnews;
    MyUtility mu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_news);

        mu=new MyUtility();
        lv_allnews=(ListView)findViewById(R.id.lv_allnews);
        if(mu.isInternetAvailable(ViewAllNews.this)==true){



            Toast.makeText(ViewAllNews.this, "Loading.. Please Wait..", Toast.LENGTH_LONG).show();
            ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_news);
            complainasync.execute();
            Log.e("url_news",""+url_news);


        }
        else{
            Toast.makeText(ViewAllNews.this, "Please connect to Internet.", Toast.LENGTH_LONG).show();

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

            Log.e("json",""+json);
            JSONArray sucess = (JSONArray) json.get("newsdetail");
            Log.e("jsonARRay  =", "" + sucess);

            if (sucess.length() == 0) {
                Toast.makeText(ViewAllNews.this, "No result found!", Toast.LENGTH_LONG).show();
            }

            pendingList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i <= sucess.length(); i++) {

                jobs = new HashMap<String, String>();

                firstRoute = sucess.getJSONObject(i);
                Log.e("title =", "" + (firstRoute.getString("title")));


                jobs.put("title",""+ String.valueOf(firstRoute.getString("title")));

                jobs.put("description", (""+firstRoute.getString("description")));


                pendingList.add(jobs);

                String[] from = {"title", "description"  };


                int[] to = {R.id.tv_subject, R.id.tv_description  };

                mSchedule = new SimpleAdapter(ViewAllNews.this, pendingList, R.layout.custom_news, from, to);


                lv_allnews.setAdapter(mSchedule);
                Log.e("after setAdapter  =", "");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        Intent it=new Intent(ViewAllNews.this,HomeScreen.class);
        startActivity(it);

    }

}
