package com.portalperfect.adminapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class SetGoal extends AppCompatActivity {

    String url_goal;
    ListView lv_stu_list;
    //--

    private ArrayList<String> array_sort= new ArrayList<String>();


    String selected_student_id,url_send_fees;
    String listview_array[],listview_array1[];
    String all,all1,defaultdata;
    int textlength=0;
    //--
    String url_set_goal;
    EditText edt_student_nm,edt_select_subject,edt_student_goal;
    Button btn_submit_goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        edt_student_nm=(EditText)findViewById(R.id.edt_student_nm);

        edt_select_subject=(EditText)findViewById(R.id.edt_select_subject);
        edt_student_goal=(EditText)findViewById(R.id.edt_student_goal);
        btn_submit_goal=(Button) findViewById(R.id.btn_submit_goal);

        lv_stu_list=(ListView)findViewById(R.id.lv_stu_list);

        lv_stu_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
            {
                edt_student_nm.setText(array_sort.get(index));
                lv_stu_list.setVisibility(View.GONE);
            }
        });



        edt_student_nm.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
                textlength = edt_student_nm.getText().length();
                array_sort.clear();

                for (int i = 0; i < listview_array.length; i++)
                {
                    if (textlength <= listview_array[i].length())
                    {
                        if(edt_student_nm.getText().toString().equalsIgnoreCase((String)listview_array[i].subSequence(0,textlength)))
                        {
                            array_sort.add(listview_array[i]);
                        }
                    }
                }
                if(array_sort.size() >0)
                {
                    lv_stu_list.setVisibility(View.VISIBLE);
                    int height = array_sort.size() * 90;
                    lv_stu_list.setLayoutParams(new LinearLayout.LayoutParams(lv_stu_list.getLayoutParams().width, height));
                }
                else
                {
                    lv_stu_list.setVisibility(View.GONE);
                }
                lv_stu_list.setAdapter(new ArrayAdapter<String>(SetGoal.this,android.R.layout.simple_list_item_1, array_sort));
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



        //--

        String list_of_students = "http://portalperfect.com/achievers/Models/ViewStudent.php";
        Log.e("list_of_students","="+list_of_students);
        GetPostcodeJson gpj = new  GetPostcodeJson(list_of_students);
        gpj.execute();
        //--

        btn_submit_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((edt_select_subject.getText().toString().length()==0||
                edt_student_goal.getText().toString().length()==0||
                edt_student_nm.getText().toString().length()==0)){

                    Toast.makeText(SetGoal.this,"Please Write All Details.",Toast.LENGTH_SHORT).show();

                }
                else {


                    String selected_student_id = edt_student_nm.getText().toString().replaceAll("[^\\d.]", "");
                    Log.e("selected_student_id", "" + selected_student_id);

                    SharedPreferences sp = SetGoal.this.getSharedPreferences("KEY_LOGGDIN", 0);
                  String  eid = sp.getString("eid", "");
//http://portalperfect.com/achievers/Models/AddGoal.php?sid=1&eid=2&goaldetail=my%20first%20goal

                    url_goal="http://portalperfect.com/achievers/Models/AddGoal.php?sid="+selected_student_id+"&eid="+eid
                            +"&subject="+edt_select_subject.getText().toString().replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                    +"&goaldetail="+edt_student_goal.getText().toString().replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                    ;

                    Toast.makeText(SetGoal.this, "WAIT..!", Toast.LENGTH_LONG).show();

                    Log.e("url_goal Api",""+url_goal);
                     ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_goal);
                    complainasync.execute();

                }
            }
        });
    }



    //--- GET STU LIST


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

        Toast.makeText(SetGoal.this,"Error!",Toast.LENGTH_LONG).show();
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

                Toast.makeText(SetGoal.this,"Added",Toast.LENGTH_LONG).show();

                Intent it=new Intent(SetGoal.this,HomeScreen.class);
                startActivity(it);
            }
            else{
                Toast.makeText(SetGoal.this,"Couldnot Add Goal.",Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
