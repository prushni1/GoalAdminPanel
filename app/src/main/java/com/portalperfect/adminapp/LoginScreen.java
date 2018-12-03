package com.portalperfect.adminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginScreen extends AppCompatActivity {

    ProgressBar progressBar;
    String json_value;
    String url_login;
    EditText edt_userid,edt_password;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        edt_userid=(EditText)findViewById(R.id.edt_userid);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_userid.getText().toString().length()==0 || edt_password.getText().toString().length()==0){
                    Toast.makeText(LoginScreen.this,"All fields are compulsary",Toast.LENGTH_SHORT).show();
                }
                else{


                    progressBar.setVisibility(View.VISIBLE);

                    //http://portalperfect.com/achievers/Models/Employeelogin.php?email=sh@gmail.com&password=123456
                    url_login="http://portalperfect.com/achievers/Models/Employeelogin.php?email="+edt_userid.getText().toString().replaceAll(" ","%20")
                    +"&password="+edt_password.getText().toString();
                    Log.e("url_login ",""+url_login);

                    Toast.makeText(LoginScreen.this,"WAIT..!",Toast.LENGTH_LONG).show();

                    ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_login);
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

            progressBar.setVisibility(View.INVISIBLE);

            JSONObject json = new JSONObject(res);
            Log.e("JSON jsonjson",""+json);

            String value=json.getString("result");
            Log.e("JSON RES",""+value);
            json_value=value;

            if (value.equalsIgnoreCase("true")){

                Toast.makeText(LoginScreen.this,"Logged In",Toast.LENGTH_LONG).show();

                boolean loggedin=true;

                //"eid":"2","name":"vidur","mobile":"9096496490","email":"sh@gmail.com","password":"123456","user":"employee"}
                String eid=json.getString("eid");
                String name=json.getString("name");
                String mobile=json.getString("mobile");
                String email=json.getString("email");
                String user=json.getString("user");

                SharedPreferences sp_user_name = LoginScreen.this.getSharedPreferences("KEY_LOGGDIN", 0);
                SharedPreferences.Editor spe_user_name = sp_user_name.edit();
                spe_user_name.putString("eid", eid);
                spe_user_name.putString("name", name);
                spe_user_name.putString("mobile", mobile);
                spe_user_name.putString("email", email);
                spe_user_name.putString("user", user);
                spe_user_name.putBoolean("loggedin", loggedin);

                spe_user_name.commit();

                Intent it=new Intent(LoginScreen.this,HomeScreen.class);
                startActivity(it);
            }
            else{
                Toast.makeText(LoginScreen.this," "+json_value,Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}


