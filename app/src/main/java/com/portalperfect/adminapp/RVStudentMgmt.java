package com.portalperfect.adminapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.portalperfect.adminapp.EditModel;
import com.portalperfect.adminapp.HomeScreen;
import com.portalperfect.adminapp.R;
import com.portalperfect.adminapp.SetGoal;
import com.portalperfect.adminapp.adapters.GetDataAdapter;
import com.portalperfect.adminapp.adapters.RVPreviousSubjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RVStudentMgmt extends RecyclerView.Adapter<RVStudentMgmt.ViewHolder> {

    Context context;

    String url_change_Status;

    String status;

    List<GetDataAdapter> getDataAdapter;

    public RVStudentMgmt(List<GetDataAdapter> getDataAdapter, Context context ) {

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_student_mgmt, parent, false);

      ViewHolder viewHolder = new  ViewHolder(v);


        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);


        //sid= phone name=fullname subject=status

        holder.tv_s_id.setText(getDataAdapter1.getPhone_number());
        holder.tv_name.setText(getDataAdapter1.getName() );

          status=getDataAdapter1.getSubject();

          Log.e("status servr",""+status);

        if(status.equalsIgnoreCase("0")){


            holder.btn_enable_disable.setBackgroundResource(R.color.red);
            holder.btn_enable_disable.setText("Disabled");
        }
        else
        {

            holder.btn_enable_disable.setBackgroundResource(R.color.green);
            holder.btn_enable_disable.setText("Enabled");
        }

        holder.setIsRecyclable(false);
        }




    @Override
    public int getItemCount() {
        return getDataAdapter.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        public TextView tv_s_id,tv_name,tv_status ;

        public Button btn_enable_disable;

        public ViewHolder(View itemView) {

            super(itemView);
            view = itemView;

            tv_s_id = (TextView) itemView.findViewById(R.id.tv_s_id);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);


            btn_enable_disable=(Button)itemView.findViewById(R.id.btn_enable_disable);


            btn_enable_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = v.getContext().getSharedPreferences("RVSTu_id", 0);
                    SharedPreferences.Editor spe_user_name = sp.edit();
                    spe_user_name.putString("rv_sid", tv_s_id.getText().toString());
                    spe_user_name.putString("rv_status", status);

                    spe_user_name.commit();

                    Intent it=new Intent(v.getContext(),StudentDetails.class);
                    v.getContext().startActivity(it);
                }
            });

        }

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
                Toast.makeText(context, "Saved all changes!", Toast.LENGTH_LONG).show();


            }
            else{
                Toast.makeText(context, "Failed..!", Toast.LENGTH_LONG).show();



            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    }
