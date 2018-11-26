package com.portalperfect.adminapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class Splashscreen extends AppCompatActivity {

    boolean loginflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        SharedPreferences sp = this.getSharedPreferences("KEY_LOGGDIN", 0);
        loginflag = sp.getBoolean("loggedin", false);

        Timer t=new Timer();

        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                TimerMethod();
            }

        }, 3000, 60*60000);
    }

    private void TimerMethod()
    {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable()
    {
        public void run()
        {



            if(loginflag == true)
            {
                Intent it = new Intent(Splashscreen.this,HomeScreen.class);

                startActivity(it);
            }
            else
            {
                Intent it = new Intent(Splashscreen.this,LoginScreen.class);
                startActivity(it);

            }


        }
    };

}
