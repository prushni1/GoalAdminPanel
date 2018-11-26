package com.portalperfect.adminapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddNewStudent extends AppCompatActivity {

    //ADD NEW EMPLOYEE.. NO ADDING STUDENT FROM THIS APP

    String url_add_emp;
    EditText  edt_emp_fullname,edt_emp_mobile,edt_emp_email,edt_emp_password;
    Button btn_upload_emp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);

        edt_emp_fullname=(EditText)findViewById(R.id.edt_emp_fullname);
        edt_emp_mobile=(EditText)findViewById(R.id.edt_emp_mobile);
        edt_emp_email=(EditText)findViewById(R.id.edt_emp_email);
        edt_emp_password=(EditText)findViewById(R.id.edt_emp_password);

        btn_upload_emp=(Button) findViewById(R.id.btn_upload_emp);

        btn_upload_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_emp_fullname.getText().toString().length()==0 ||
                        edt_emp_mobile.getText().toString().length()==0 ||
                        edt_emp_email.getText().toString().length()==0 ||
                        edt_emp_password.getText().toString().length()==0
                        ){
                    Toast.makeText(AddNewStudent.this,"All Fields are compulsary",Toast.LENGTH_SHORT).show();
                }
                else{

                    //http://portalperfect.com/achievers/Models/AddEmployee.php?name=vidur&mobile=9096496490&email=sh@gmail.com&password=123456
                    url_add_emp="http://portalperfect.com/achievers/Models/AddEmployee.php?name="+edt_emp_fullname.getText().toString().replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                    +"&mobile="+edt_emp_mobile.getText().toString().replaceAll(" ","%20")
                            +"&email="+edt_emp_email.getText().toString().replaceAll(" ","%20")
                            +"&password="+edt_emp_password.getText().toString().replaceAll(" ","%20")
                    ;

                    Log.e("url_add_emp",""+url_add_emp);
                    Toast.makeText(AddNewStudent.this,"Adding.. ! Please Wait",Toast.LENGTH_SHORT).show();

                    ComplainDetailAsync complainasync = new ComplainDetailAsync(url_add_emp);
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

            String value=json.getString("result");
            Log.e("JSON RES",""+value);

            if (value.equalsIgnoreCase("true")){

                Toast.makeText(AddNewStudent.this,"Added",Toast.LENGTH_LONG).show();

                Intent it=new Intent(AddNewStudent.this,HomeScreen.class);
                startActivity(it);
            }
            else{
                Toast.makeText(AddNewStudent.this,"Couldnot Add.",Toast.LENGTH_LONG).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
