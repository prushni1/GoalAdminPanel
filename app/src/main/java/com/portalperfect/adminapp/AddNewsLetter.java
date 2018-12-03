package com.portalperfect.adminapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddNewsLetter extends AppCompatActivity {

    String url_add_news;
    EditText edt_news_subject,edt_news_details;
    Button btn_submit_news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news_letter);

        edt_news_subject=(EditText)findViewById(R.id.edt_news_subject);
        edt_news_details=(EditText)findViewById(R.id.edt_news_details);
        btn_submit_news=(Button) findViewById(R.id.btn_submit_news);


        edt_news_subject.setOnKeyListener(new OnKeyListener()
        {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event)
            {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_news_subject.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        } );


        edt_news_details.setOnKeyListener(new OnKeyListener()
        {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event)
            {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_news_details.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        } );

        btn_submit_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_news_details.getText().toString().length()==0 || edt_news_subject.getText().toString().length()==0){

                    Toast.makeText(AddNewsLetter.this,"Please Write All Details.",Toast.LENGTH_SHORT).show();

                }
                else {

//http://portalperfect.com/achievers/Models/AddNews.php?title=My%20first%20Title%20News&description=my%20news%20title


                    url_add_news="http://portalperfect.com/achievers/Models/AddNews.php?title="+edt_news_subject.getText().toString().replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                    +"&description="+edt_news_details.getText().toString().replaceAll("[^A-Za-z0-9 ]","%20").replace(" ","%20")
                    ;
                    Toast.makeText(AddNewsLetter.this, "WAIT..!", Toast.LENGTH_LONG).show();

                    Log.e("News Api",""+url_add_news);

                    ComplainDetailAsync complainasync = new  ComplainDetailAsync(url_add_news);
                    complainasync.execute();
                }


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addnewsletter, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewnews:

                Intent it=new Intent(AddNewsLetter.this,ViewAllNews.class);
                startActivity(it);

                return true;

            default:
                return super.onOptionsItemSelected(item);
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

                Toast.makeText(AddNewsLetter.this,"Added",Toast.LENGTH_LONG).show();

                Intent it=new Intent(AddNewsLetter.this,HomeScreen.class);
                startActivity(it);
            }
            else{
                Toast.makeText(AddNewsLetter.this,"Couldnot Add News.",Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
