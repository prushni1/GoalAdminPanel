package com.portalperfect.adminapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.portalperfect.adminapp.adapters.GetDataAdapter;
import com.portalperfect.adminapp.adapters.RVAddResultsPage;
import com.portalperfect.adminapp.adapters.RVPreviousSubjects;
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
import java.util.HashMap;
import java.util.List;

public class View_Previous_subject_marks extends AppCompatActivity {

    //````` JSON DATA OBJECTS
    List<GetDataAdapter> GetDataAdapter1;
    RequestQueue requestQueue ;
    RecyclerView recyclerView;

    RVPreviousSubjects recyclerViewadapter;

    RecyclerView.LayoutManager recyclerViewlayoutManager;


    TextView tv_selected_sub;
    //----
    ArrayList<HashMap<String, String>> pendingList;
    JsonObjectRequest jsonArrayRequest ;

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

        jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url_previoussubject,
                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                         Toast.makeText(View_Previous_subject_marks.this, "Please Wait.", Toast.LENGTH_LONG).show();


                        try {
                            JSONArray array = response.getJSONArray("resulbysubject");
                            Log.e("array", " =" + array);
                             Log.e("length", " =" + array.length());


                            for(int i=0;i<array.length();i++) {
                                GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

                                JSONObject jsonobj=null;
                                try{

                                jsonobj= array.getJSONObject(i);
                                Log.e("jsonobj", " =" + jsonobj);

                                // MARKS=PHONE NAME=NAME OUTOF=CITY  NEWID=ID  TESTRESULT=TESTRESULT
                                GetDataAdapter2.setPhone_number((jsonobj.getString("marks")));

                                GetDataAdapter2.setName(jsonobj.getString("fullname"));
                                GetDataAdapter2.setCity(jsonobj.getString("outof"));
                                GetDataAdapter2.setNewid( (jsonobj.getString("sid")));
                                GetDataAdapter2.setTestresult(jsonobj.getString("testresult"));
                                    GetDataAdapter2.setSubject(jsonobj.getString("subject"));

                                    Log.e("GetDataAdapter2", " =" + GetDataAdapter2);

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                }
                                GetDataAdapter1.add(GetDataAdapter2);
                            }
                            recyclerViewadapter = new RVPreviousSubjects(GetDataAdapter1,View_Previous_subject_marks.this );


                            recyclerView.setAdapter(recyclerViewadapter);

                        } catch (JSONException e) {
                            Log.e("CANT TRY","SO CATCH");
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.e("Error Listner","");


                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }




}

