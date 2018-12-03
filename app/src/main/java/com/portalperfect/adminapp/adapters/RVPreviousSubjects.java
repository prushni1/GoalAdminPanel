package com.portalperfect.adminapp.adapters;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RVPreviousSubjects extends RecyclerView.Adapter<RVPreviousSubjects.ViewHolder> {


    Context context;

    String url_goal_rv;


    List<GetDataAdapter> getDataAdapter;

    public RVPreviousSubjects(List<GetDataAdapter> getDataAdapter, Context context ) {

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;


    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_previous_subjects, parent, false);

         ViewHolder viewHolder = new  ViewHolder(v);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        GetDataAdapter getDataAdapter1 = getDataAdapter.get(position);
//tv_marks,tv_outof,tv_passorfail


        /**
         *  GetDataAdapter2.setPhone_number((jsonobj.getString("marks")));

         GetDataAdapter2.setName(jsonobj.getString("fullname"));
         GetDataAdapter2.setCity(jsonobj.getString("outof"));
         GetDataAdapter2.setId(Integer.parseInt(jsonobj.getString("sid")));
         GetDataAdapter2.setSubject(jsonobj.getString("testresult"));
         **/
        holder.tv_marks.setText("Name : "+getDataAdapter1.getName()+"\n"+"Marks : "+getDataAdapter1.getPhone_number());
        holder.tv_outof.setText("Out Of : "+getDataAdapter1.getCity());
        holder.tv_passorfail.setText("Status : "+getDataAdapter1.getTestresult());

         holder.tv_sid.setText(getDataAdapter1.getNewid());
         holder.tv_subject.setText(getDataAdapter1.getSubject());

        String passorfail=getDataAdapter1.getTestresult();

if (passorfail.equalsIgnoreCase("pass")){

    holder.btn_add_goal.setVisibility(View.GONE);
    holder.img_passorfail.setBackgroundResource(R.drawable.img_pass);
}
else{


    holder.btn_add_goal.setVisibility(View.VISIBLE);
    holder.img_passorfail.setBackgroundResource(R.drawable.failed);

    holder.btn_add_goal.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {



            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog.setTitle("Set Goal");
            alertDialog.setMessage("Set Goal?");

            final EditText input = new EditText(v.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setIcon(R.drawable.img_pass);

            alertDialog.setPositiveButton("Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String goal = input.getText().toString();
                            Toast.makeText(v.getContext(), "Goal Set ="+goal , Toast.LENGTH_SHORT).show();
                           // Toast.makeText(v.getContext(), "For id ="+holder.tv_sid.getText().toString() , Toast.LENGTH_SHORT).show();

                            SharedPreferences sp = v.getContext().getSharedPreferences("KEY_LOGGDIN", 0);
                            String  eid = sp.getString("eid", "");

url_goal_rv="http://portalperfect.com/achievers/Models/AddGoal.php?sid="+holder.tv_sid.getText().toString()+"&eid="+eid
                                    +"&subject="+holder.tv_subject.getText().toString().replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                                    +"&goaldetail="+goal.replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                            ;

                            Log.e("url_goal_rv",""+url_goal_rv);
                            Toast.makeText(v.getContext(), "Adding..!", Toast.LENGTH_LONG).show();
                            ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_goal_rv);
                            complainasync.execute();



                        }
                    });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();



        }
    });
}



    }





    @Override
    public int getItemCount() {
        return getDataAdapter.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        public TextView tv_marks,tv_outof,tv_passorfail,tv_sid,tv_subject;
        public ImageView img_passorfail;
        public Button btn_add_goal;

        public ViewHolder(View itemView) {

            super(itemView);
            view = itemView;

            tv_sid=(TextView)itemView.findViewById(R.id.tv_sid);
            tv_marks = (TextView) itemView.findViewById(R.id.tv_marks);
            tv_outof = (TextView) itemView.findViewById(R.id.tv_outof);
            tv_passorfail = (TextView) itemView.findViewById(R.id.tv_passorfail);

            img_passorfail=(ImageView)itemView.findViewById(R.id.img_passorfail);

            btn_add_goal=(Button)itemView.findViewById(R.id.btn_add_goal);
            tv_subject=(TextView)itemView.findViewById(R.id.tv_subject);


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
                Toast.makeText(context, "Added..!", Toast.LENGTH_LONG).show();


            }
            else{
                Toast.makeText(context, "Failed to add goal..!", Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
