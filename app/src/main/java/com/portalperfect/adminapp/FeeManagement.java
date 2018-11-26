package com.portalperfect.adminapp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FeeManagement extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   Button btn_view_fees;
   ListView lv_allfees_list;
    ProgressDialog progDialog;

    String eid;

    private ArrayList<String> array_sort= new ArrayList<String>();


    String selected_student_id,url_send_fees;
    String listview_array[],listview_array1[];
    String all,all1,defaultdata;
    int textlength=0;
    Button btn_add_fees;
    EditText edit_stu_name,edt_fees;
    ListView lv_stu_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_allfees_list=(ListView)findViewById(R.id.lv_allfees_list);
        btn_view_fees=(Button)findViewById(R.id.btn_view_fees);

        btn_add_fees=(Button)findViewById(R.id.btn_add_fees);

        edit_stu_name=(EditText)findViewById(R.id.edit_stu_name);
        edt_fees=(EditText)findViewById(R.id.edt_fees);

        lv_stu_list=(ListView)findViewById(R.id.lv_stu_list);

        progDialog = new ProgressDialog(FeeManagement.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progDialog.setMessage("Please Wait.. .");
        progDialog.setCancelable(false);

        progDialog.show();

        Toast.makeText(FeeManagement.this,"Please Select Name from LIST only.",Toast.LENGTH_SHORT).show();

        String list_of_students = "http://portalperfect.com/achievers/Models/ViewStudent.php";
        Log.e("list_of_students","="+list_of_students);
        GetPostcodeJson gpj = new GetPostcodeJson(list_of_students);
        gpj.execute();

        lv_stu_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
            {
                edit_stu_name.setText(array_sort.get(index));
                lv_stu_list.setVisibility(View.GONE);
            }
        });

        edit_stu_name.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                textlength = edit_stu_name.getText().length();
                array_sort.clear();

                for (int i = 0; i < listview_array.length; i++)
                {
                    if (textlength <= listview_array[i].length())
                    {
                        if(edit_stu_name.getText().toString().equalsIgnoreCase((String)listview_array[i].subSequence(0,textlength)))
                        {
                            array_sort.add(listview_array[i]);
                        }
                    }
                }
                if(array_sort.size() >0)
                {
                    lv_stu_list.setVisibility(View.VISIBLE);
                    int height = array_sort.size() * 90;
                    lv_stu_list.setLayoutParams(new LayoutParams(lv_stu_list.getLayoutParams().width, height));
                }
                else
                {
                    lv_stu_list.setVisibility(View.GONE);
                }
                lv_stu_list.setAdapter(new ArrayAdapter<String>(FeeManagement.this,android.R.layout.simple_list_item_1, array_sort));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub

            }
        });

        btn_add_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://portalperfect.com/achievers/Models/AddFees.php?sid=1&amount=5000

                if(edit_stu_name.getText().toString().length()==0 || edt_fees.getText().toString().length()==0){
                    Toast.makeText(FeeManagement.this,"Please Select Name and Fees",Toast.LENGTH_SHORT).show();
                }
                else{


                    SharedPreferences sp = FeeManagement.this.getSharedPreferences("KEY_LOGGDIN", 0);
                    eid = sp.getString("eid", "");


                    selected_student_id=edit_stu_name.getText().toString().replaceAll("[^\\d.]", "");
                Log.e("selected_student_id",""+selected_student_id);

                    url_send_fees="http://portalperfect.com/achievers/Models/AddFees.php?sid="+selected_student_id.replaceAll(" ","%20")
                            +"&amount="+edt_fees.getText().toString().replaceAll(" ","%20")
                    +"&eid="+eid;

                    Toast.makeText(FeeManagement.this,"WAIT..!",Toast.LENGTH_LONG).show();

                    ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_send_fees);
                    complainasync.execute();
                } }
        });


        btn_view_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lv_allfees_list.setVisibility(View.VISIBLE);
                String url_allfees="http://portalperfect.com/achievers/Models/ViewFees.php";

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
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    // -- ASYNC
    private class GetPostcodeJson extends AsyncTask<Object, Object, Object>
    {
        private String url_registeration;
        private InputStream is;
        private String res;

        public GetPostcodeJson(String url_registeration)
        {
            this.url_registeration = url_registeration;
        }

        @Override
        protected Object doInBackground(Object... params)
        {
            progDialog.dismiss();

            try
            {   URL url;
                HttpURLConnection urlConnection ;
                url = new URL(url_registeration);

                urlConnection = (HttpURLConnection)url.openConnection();
                is  = urlConnection.getInputStream();
                Log.e("is",""+is);
                InputStreamReader isw = new InputStreamReader(is);


                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;


                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                res = sb.toString();

                Log.e("res", "" + res);
                Log.e("Response from url = ", res);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                Log.e("log_tag", "Error converting resulturl "+e.toString());
            }
            return res;
        }
        @Override
        protected void onPostExecute(Object result11)
        {

            Log.e("result11", "  "+result11 );


            super.onPostExecute(result11);
            if(result11 != null){
                Log.e("result11 != null", "" );

                returnResultUrl(res);
            }else{
                Log.e("ELSEELSE", "ONPOST     " );

                ErrorMessage();
            }

        }
    }

    private void ErrorMessage() {

        Toast.makeText(FeeManagement.this,"Error!",Toast.LENGTH_LONG).show();
    }

    private void returnResultUrl(String res)
    {
        Log.e("returnResultUrl = ", res);
        parseJsonUrl(res);

    }

    @SuppressLint("WorldReadableFiles")
    private void parseJsonUrl(String res) {

        try {
            JSONArray JArray_stu = new JSONArray(res);


            Log.e("JArray_stu",""+JArray_stu);

            for(int i=0; i<JArray_stu.length() ; i++)
            {
                JSONObject firstjobj = (JSONObject) JArray_stu.get(i);
                defaultdata = firstjobj.getString("fullname")+" "+firstjobj.getString("sid");

                if (all == null)
                {
                    all = defaultdata;
                }
                else
                {
                    all = all + ":" + defaultdata;
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        listview_array = all.split(":");

        listview_array1 = all.split(":");




    }
    //-- ASYNC



    //------SEND FEES API
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

                Toast.makeText(FeeManagement.this,"Added",Toast.LENGTH_LONG).show();

                Intent it=new Intent(FeeManagement.this,HomeScreen.class);
                startActivity(it);
            }
            else{
                Toast.makeText(FeeManagement.this,"Couldnot Add Fees. Inform Admin.",Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
