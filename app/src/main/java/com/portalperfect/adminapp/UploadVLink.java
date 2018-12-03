package com.portalperfect.adminapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadVLink extends AppCompatActivity {

    String url_upload_link;
    Button btn_submit_link;
    EditText edt_v_title,edt_v_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vlink);

        btn_submit_link=(Button)findViewById(R.id.btn_submit_link);
        edt_v_link=(EditText)findViewById(R.id.edt_v_link);
        edt_v_title=(EditText)findViewById(R.id.edt_v_title);

        btn_submit_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_v_link.getText().toString().length()==0 ||
                        edt_v_title.getText().toString().length()==0){
                    Toast.makeText(UploadVLink.this,"Please Write title and paste link",Toast.LENGTH_SHORT).show();
                }

                else{

                    //http://portalperfect.com/achievers/Models/AddVideo.php?title=My%20First%20Lecture&link=https://www.youtube.com/watch?v=f7iR1sIKE9Y
                    url_upload_link="http://portalperfect.com/achievers/Models/AddVideo.php?title="+edt_v_title.getText().toString().replaceAll(" ","%20")
                    +"&link="+edt_v_link.getText().toString().replaceAll(" ","%20");

                    Log.e("url_upload_link",""+url_upload_link);

                    Toast.makeText(UploadVLink.this, "WAIT..!", Toast.LENGTH_LONG).show();


                    ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_upload_link);
                    complainasync.execute();
                }
            }
        });
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
                returnResultUrl1(res);
            }


        }
    }

    private void returnResultUrl1(String res) {
        parseJsonUrl1(res);

    }

    private void parseJsonUrl1(String res) {
        try {
            // prog_dialog.dismiss();

            JSONObject json = new JSONObject(res);

            String value=json.getString("result");
            Log.e("JSON RES",""+value);

            if (value.equalsIgnoreCase("true")){

                Toast.makeText(UploadVLink.this,"Added",Toast.LENGTH_LONG).show();

                Intent it=new Intent(UploadVLink.this,HomeScreen.class);
                startActivity(it);
            }
            else{
                Toast.makeText(UploadVLink.this,"Couldnot Add Link. Try again later.",Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
