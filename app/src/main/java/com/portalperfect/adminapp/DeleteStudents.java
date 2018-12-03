package com.portalperfect.adminapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.portalperfect.adminapp.adapters.GetDataAdapter;
import com.portalperfect.adminapp.adapters.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.portalperfect.adminapp.adapters.GetDataAdapter;
import com.portalperfect.adminapp.adapters.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeleteStudents extends AppCompatActivity {
    String group_url;

    JSONArray jsonA;
    Button btn_delete_stu;

    //````` JSON DATA OBJECTS
    List<GetDataAdapter> GetDataAdapter1;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;
    RecyclerViewAdapter rv;
    List<List<Integer>> items;


    String student_url,selected_group_name;

    private List<Integer> selectedIds = new ArrayList<>();
    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    JsonArrayRequest jsonArrayRequest ;

    String selected_menu;
    RequestQueue requestQueue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_students);
        btn_delete_stu=(Button)findViewById(R.id.btn_delete_stu);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_delete);

        SharedPreferences sp = this.getSharedPreferences("ADDORDELETE", 0);
        selected_menu = sp.getString("selected_menu", "");


        SharedPreferences sp_clientid = DeleteStudents.this.getSharedPreferences("GROUPNAME", 0);
        selected_group_name = sp_clientid.getString("selected_group_name", " ");


        if(selected_menu.equalsIgnoreCase("delte")){
 student_url="http://portalperfect.com/achievers/Models/students_list_new.php?groupname="+selected_group_name.replaceAll(" ","%20");

 group_url="http://portalperfect.com/achievers/Models/DeleteGroupMember.php?groupname="+selected_group_name.replaceAll(" ","%20");


        }
        else if(selected_menu.equalsIgnoreCase("addstudents")){

            btn_delete_stu.setText("Add Students");
            student_url="http://portalperfect.com/achievers/Models/ViewStudent.php";
            group_url="http://portalperfect.com/achievers/Models/AddResultGroup.php?groupname="+selected_group_name.replaceAll(" ","%20");



        }


//`````````````````````````
        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_delete);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        GetDataAdapter1.clear();

        JSON_DATA_WEB_CALL();


//```````````````````````````````


        btn_delete_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        DeleteStudents.this);

                alertDialog.setTitle("Admin Achievers Academy");

                alertDialog.setMessage("Are you sure ?");

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int total=recyclerViewadapter.getItemCount();
                                Log.e("total",""+total);

                                items = new ArrayList<List<Integer>>();
                                items.add(recyclerViewadapter.getSelectedItems());

                                Log.e("getSelectedItems",""+recyclerViewadapter.getSelectedItems());
                                Log.e("items items",""+items);

                                jsonA = new JSONArray(recyclerViewadapter.getSelectedItems());
                                Log.e("items jsonA",""+jsonA);

                                group_url=group_url+"&sid="+jsonA;

                                Log.e("LINK="," "+group_url);

                                ComplainDetailAsync complainasync = new ComplainDetailAsync(group_url);
                                complainasync.execute();
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

            }
        });
    }





    //#################JSON DATA
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(student_url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        Toast.makeText(DeleteStudents.this, "Please Wait.", Toast.LENGTH_LONG).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for (int i = 0; i < array.length(); i++) {

            Log.e("ARRAY lgth"," "+array.length());
            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();



            JSONObject json = null;
            try {
                json = array.getJSONObject(i);


                GetDataAdapter2.setPhone_number(json.getString("sid"));

                GetDataAdapter2.setName(json.getString("fullname"));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);
        }
        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1,DeleteStudents.this );


        recyclerView.setAdapter(recyclerViewadapter);
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
            JSONObject json = new JSONObject(res);

            String code = json.getString("result");
            Log.e("code  =", "" + code);

            if (code.equalsIgnoreCase("true")){
                Toast.makeText(DeleteStudents.this,"All Changes Made !",Toast.LENGTH_LONG).show();

                Intent it=new Intent(DeleteStudents.this,Result_View_Groups.class);
                startActivity(it);

            }

            if (code.equalsIgnoreCase("resultexist")){
                Toast.makeText(DeleteStudents.this,"Sorry you can't delete this student, Result of this student exist!",Toast.LENGTH_LONG).show();

            }

            //Sorry you can't delete this student, Result of this student exist.!


        } catch(JSONException e){

            e.printStackTrace();
        }



    }



}
