package com.portalperfect.adminapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AddResultsPage extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    boolean dateselected=false;
    String selecteddate;


    MyUtility mu;
    Button btn_Date;
    EditText edt_subjectname,edt_outof,edt_passingmarks;
      SparseBooleanArray allmarks ;

        String url_send_marks;
    JSONArray jsonA,json_marks;
    List<List<Integer>> items;
    //---
    int totalstudents;
    String selected_group_name,url_grp_Students;
    JsonArrayRequest jsonArrayRequest ;

    public ArrayList<EditModel> editModelArrayList;
    RequestQueue requestQueue ;

    Button btn_submit_marks;
    //````` JSON DATA OBJECTS
    List<GetDataAdapter> GetDataAdapter1;

    RecyclerView recyclerView;

    RVAddResultsPage recyclerViewadapter;

    RecyclerView.LayoutManager recyclerViewlayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_results_page);

        mu=new MyUtility();
        btn_Date=(Button)findViewById(R.id.btn_Date);
        edt_subjectname=(EditText)findViewById(R.id.edt_subjectname);
        edt_outof=(EditText)findViewById(R.id.edt_outof);

        edt_passingmarks=(EditText)findViewById(R.id.edt_passingmarks);

        SharedPreferences sp_clientid = AddResultsPage.this.getSharedPreferences("GROUPNAME", 0);
        selected_group_name = sp_clientid.getString("selected_group_name", " ");

        url_grp_Students="http://portalperfect.com/achievers/Models/ViewResultGroupByNameDemo.php?groupname="+selected_group_name.replaceAll(" ","%20");


        Log.e("ADD RESULTS","="+url_grp_Students);

        allmarks= new SparseBooleanArray();
        btn_submit_marks=(Button)findViewById(R.id.btn_submit_marks);
        btn_submit_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("SIZE",""+recyclerViewadapter.editModelArrayList.size());

                List<Integer> markslist = new ArrayList <Integer>(recyclerViewadapter.editModelArrayList.size());


                for (int i = 0; i < recyclerViewadapter.editModelArrayList.size(); i++){
                    Log.e("FOR LOOP",""+recyclerViewadapter.editModelArrayList.get(i).getEditTextValue());


                   markslist.add( (Integer.valueOf(recyclerViewadapter.editModelArrayList.get(i).getEditTextValue())));

                }

                Log.e("markslist=",""+markslist);
//----

                json_marks=new JSONArray(markslist);

                Log.e("json_marks  ",""+json_marks);

//---


                items = new ArrayList<List<Integer>>();
                items.add(recyclerViewadapter.getSelectedItems());

                Log.e("Sids=",""+items);

                jsonA = new JSONArray(recyclerViewadapter.getSelectedItems());
                Log.e("Sids jsonA",""+jsonA);


                //http://portalperfect.com/achievers/Models/AddResult.php?sid=[1,2]&groupname=all&marks=[55,66]
                // &subject=accounts&outof=30&examdate=16-11-2018


                if (dateselected == false) {

                    AlertDialog.Builder a1 = new AlertDialog.Builder(AddResultsPage.this);
                    a1.setTitle(R.string.app_name);
                    a1.setMessage("Please Select A Valid Date  ").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                        }

                    }).show();
                } else if(edt_subjectname.getText().toString().length()==0){
                    AlertDialog.Builder a1 = new AlertDialog.Builder(AddResultsPage.this);
                    a1.setTitle(R.string.app_name);
                    a1.setMessage("Please Enter Subject Name ").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                        }

                    }).show();
                }
                else if(edt_outof.getText().toString().length()==0){
                    AlertDialog.Builder a1 = new AlertDialog.Builder(AddResultsPage.this);
                    a1.setTitle(R.string.app_name);
                    a1.setMessage("Please Enter Marks Out Of ").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                        }

                    }).show();
                }
                else if(edt_passingmarks.getText().toString().length()==0){
                    AlertDialog.Builder a1 = new AlertDialog.Builder(AddResultsPage.this);
                    a1.setTitle(R.string.app_name);
                    a1.setMessage("Please Enter Passing Marks ").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                        }

                    }).show();
                }
                else if (mu.isInternetAvailable(AddResultsPage.this) == false) {
                    AlertDialog.Builder a1 = new AlertDialog.Builder(AddResultsPage.this);
                    a1.setTitle("Connection Error !");
                    a1.setMessage("Please Connect To Internet. ").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();


                        }

                    }).show();
                } else {
                    Toast.makeText(AddResultsPage.this, "Please Wait!", Toast.LENGTH_LONG).show();

                    url_send_marks="http://portalperfect.com/achievers/Models/AddResult.php?sid="+jsonA+"&groupname="
                            +selected_group_name.replaceAll(" ","%20")
                            +"&marks="+json_marks
                            +"&subject="+edt_subjectname.getText().toString().replaceAll(" ","%20").replaceAll("[^A-Za-z0-9 ]","%20")
                            +"&outof="+edt_outof.getText().toString().replaceAll(" ","%20")
                            +"&passingmark="+edt_passingmarks.getText().toString().replaceAll(" ","%20")
                            +"&examdate="+selecteddate;
                    Log.e("url_send_marks==",""+url_send_marks);

                    ComplainDetailAsync complainasync = new ComplainDetailAsync(url_send_marks);
                    complainasync.execute();
                }

            }
        });
//`````````````````````````
        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerresult);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddResultsPage.this,
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
    }



    //#################JSON DATA
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(url_grp_Students,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        Toast.makeText(AddResultsPage.this, "Please Wait.", Toast.LENGTH_LONG).show();



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

            totalstudents=array.length();
            editModelArrayList = populateList();

            Log.e("total students="," "+array.length());
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
        recyclerViewadapter = new RVAddResultsPage(GetDataAdapter1,AddResultsPage.this,editModelArrayList );


        recyclerView.setAdapter(recyclerViewadapter);
    }
    private ArrayList<EditModel> populateList(){

        ArrayList<EditModel> list = new ArrayList<>();

        for(int i = 0; i < totalstudents; i++){
            EditModel editModel = new EditModel();
            editModel.setEditTextValue("");
            list.add(editModel);
        }

        return list;
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
                Log.e("log_tag", "Error in http conection  " + e.toString());
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
                Log.e("Response from url = ", res);

            } catch (Exception e) {
                Log.i("log_tag", "Error converting resulturl " + e.toString());
            }
            return res;
        }

        @Override
        protected void onPostExecute(Object result11) {
            super.onPostExecute(result11);
            returnResultUrl(res);

        }
    }

    private void returnResultUrl(String res) {
        parseJsonUrl(res);

    }

    private void parseJsonUrl(String res) {
        try {
            JSONObject json = new JSONObject(res);

            String code = json.getString("result");
            Log.e("code  =", "" + code);

            if (code.equalsIgnoreCase("true")){
                Toast.makeText(AddResultsPage.this,"Added Marks !",Toast.LENGTH_LONG).show();

                Intent it=new Intent(AddResultsPage.this,Result_View_Groups.class);
                startActivity(it);

            }


        } catch(JSONException e){

            e.printStackTrace();
        }



    }

}
