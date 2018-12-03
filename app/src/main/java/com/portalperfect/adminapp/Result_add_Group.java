package com.portalperfect.adminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Result_add_Group extends AppCompatActivity  implements DataInterface{

    String group_url;
    JSONArray jsonA;
    EditText group_name;

    String student_url="http://portalperfect.com/achievers/Models/ViewStudent.php";

    //````` JSON DATA OBJECTS
    List<GetDataAdapter> GetDataAdapter1;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;
    RecyclerViewAdapter rv;
    Button btn_create_group;


    List<List<Integer>> items;

    private List<Integer> selectedIds = new ArrayList<>();
    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_add__group);

        group_name=(EditText)findViewById(R.id.group_name);

        btn_create_group=(Button)findViewById(R.id.btn_create_group);


        Toast.makeText(Result_add_Group.this,"LOADING...",Toast.LENGTH_LONG).show();

        Toast.makeText(Result_add_Group.this,"Please Wait...",Toast.LENGTH_LONG).show();

        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int total=recyclerViewadapter.getItemCount();
                Log.e("total",""+total);

                items = new ArrayList<List<Integer>>();
                items.add(recyclerViewadapter.getSelectedItems());

                Log.e("getSelectedItems",""+recyclerViewadapter.getSelectedItems());
                Log.e("items items",""+items);

                jsonA = new JSONArray(recyclerViewadapter.getSelectedItems());
                Log.e("items jsonA",""+jsonA);


                if(group_name.getText().toString().length()==0){
                    Toast.makeText(Result_add_Group.this,"Please add Group Name",Toast.LENGTH_LONG).show();

                }
                else{
                    group_url="http://portalperfect.com/achievers/Models/AddResultGroup.php?groupname="+group_name.getText().toString().replaceAll(" ","%20")
                            +"&sid="+jsonA;

                    Log.e("Add new group"," "+group_url);

                    ComplainDetailAsync complainasync = new ComplainDetailAsync(group_url);
                    complainasync.execute();



                }

            }
        });


//`````````````````````````
        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.student_rv);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        GetDataAdapter1.clear();

        JSON_DATA_WEB_CALL();


//```````````````````````````````

}

















    //#################JSON DATA
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(student_url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                            JSON_PARSE_DATA_AFTER_WEBCALL(response);

                            Toast.makeText(Result_add_Group.this, "Please Wait.", Toast.LENGTH_LONG).show();



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
            recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1,Result_add_Group.this );


            recyclerView.setAdapter(recyclerViewadapter);
  }


    @Override
    public void getSelectedItems(String items) {

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
                Toast.makeText(Result_add_Group.this,"Created Group !",Toast.LENGTH_LONG).show();

                Intent it=new Intent(Result_add_Group.this,Result_View_Groups.class);
                startActivity(it);

            }


        } catch(JSONException e){

            e.printStackTrace();
        }



    }
}
