package com.portalperfect.adminapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.portalperfect.adminapp.adapters.GetDataAdapter;
import com.portalperfect.adminapp.adapters.RVAddResultsPage;
import com.portalperfect.adminapp.adapters.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddAttendance extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    boolean dateselected=false;
    String selecteddate;
    JSONArray jsonA;

    MyUtility mu;
    Button btn_Date;
    EditText edt_subjectname,edt_outof,edt_passingmarks;


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
//---------
    String selected_group_name,url_grp_Students;
    Button btn_submit_atten;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);


        mu=new MyUtility();
        btn_Date=(Button)findViewById(R.id.btn_Date);
        btn_submit_atten=(Button)findViewById(R.id.btn_submit_atten);



        Toast.makeText(AddAttendance.this,"Please Wait...",Toast.LENGTH_LONG).show();

        Toast.makeText(AddAttendance.this,"Tick for students who are present",Toast.LENGTH_LONG).show();

        SharedPreferences sp_clientid = AddAttendance.this.getSharedPreferences("AttendnceGROUPNAME", 0);
        selected_group_name = sp_clientid.getString("attendnce_group_name", " ");

        url_grp_Students="http://portalperfect.com/achievers/Models/attendenceStudentList.php?groupname="+selected_group_name.replaceAll(" ","%20");

        Log.e("LIST OF STU","="+url_grp_Students);


//`````````````````````````
        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycleratten);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        GetDataAdapter1.clear();

        JSON_DATA_WEB_CALL();


//```````````````````````````````


        btn_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAttendance.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {



                                btn_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                //&date=2018-01-01  +"&date="+selecteddate
                                selecteddate=year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                dateselected=true;
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                                    Date currentdate = new Date();

                                    String dateInString = dayOfMonth+"/"+(monthOfYear + 1)+"/"+year;
                                    Date SelectedDate = dateFormat.parse(dateInString);
                                    //  Toast.makeText(getApplicationContext(), " today="+dateFormat.format(currentdate)+" selected= "+dateFormat.format(SelectedDate), Toast.LENGTH_SHORT).show();

                                    currentdate=dateFormat.parse(dateFormat.format(currentdate));



                                }

                                catch ( Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        btn_submit_atten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateselected == false) {

                    AlertDialog.Builder a1 = new AlertDialog.Builder(AddAttendance.this);
                    a1.setTitle(R.string.app_name);
                    a1.setMessage("Please Select A Valid Date  ").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                        }

                    }).show();
                }
                else{
                    int total=recyclerViewadapter.getItemCount();
                    Log.e("total",""+total);

                    items = new ArrayList<List<Integer>>();
                    items.add(recyclerViewadapter.getSelectedItems());

                    Log.e("getSelectedItems",""+recyclerViewadapter.getSelectedItems());
                    Log.e("items items",""+items);

                    jsonA = new JSONArray(recyclerViewadapter.getSelectedItems());
                    Log.e("items jsonA",""+jsonA);

//http://portalperfect.com/achievers/Models/AddStudentAttendance.php?sid=[1,2,3,4]&hrpname=AA&eid=12&date=2018-11-29

                    SharedPreferences sp = AddAttendance.this.getSharedPreferences("KEY_LOGGDIN", 0);
                    String eid = sp.getString("eid", "");

                    String  addattendnce_url="http://portalperfect.com/achievers/Models/AddStudentAttendance.php?sid=" +jsonA+
         "&grpname="+selected_group_name.replaceAll(" ","%20")
                                +"&eid="+eid
                               +"&date="+selecteddate;

                        Log.e("addattendnce_url"," "+addattendnce_url);

                         ComplainDetailAsync complainasync = new  ComplainDetailAsync(addattendnce_url);
                        complainasync.execute();




                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addattendance, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewattendance:

                Intent it=new Intent(AddAttendance.this,AttendanceDates.class);
                startActivity(it);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //#################JSON DATA
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(url_grp_Students,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        Toast.makeText(AddAttendance.this, "Please Wait.", Toast.LENGTH_LONG).show();



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
        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1,AddAttendance.this );


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
                Toast.makeText(AddAttendance.this,"Added !",Toast.LENGTH_LONG).show();

                Intent it=new Intent(AddAttendance.this,AttendanceManagement.class);
                startActivity(it);

            }


        } catch(JSONException e){

            e.printStackTrace();
        }



    }














}
