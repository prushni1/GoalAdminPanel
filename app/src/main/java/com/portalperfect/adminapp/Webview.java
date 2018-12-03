package com.portalperfect.adminapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.webkit.WebView;
import android.widget.TextView;

public class Webview extends AppCompatActivity {
   TextView txt_download;

    String nav_selcted,export_url;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        txt_download=(TextView)findViewById(R.id.txt_download);

        SharedPreferences sp = Webview.this.getSharedPreferences("SELECTED_NAV", 0);
        nav_selcted = sp.getString("nav_Selection", " ");

        if(nav_selcted.equalsIgnoreCase("student")){
            export_url="http://portalperfect.com/achievers/Models/ExportStudent.php";
        }else   if(nav_selcted.equalsIgnoreCase("fees")){
            export_url="http://portalperfect.com/achievers/Models/ExportFees.php";
        }
        else   if(nav_selcted.equalsIgnoreCase("result")){
            export_url="http://portalperfect.com/achievers/Models/ExportResult.php";
        }

        txt_download.setText(export_url);
        txt_download.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
