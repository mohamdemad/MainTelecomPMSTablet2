package com.MainTelecom_Tablet.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import main.activities.App_Home;

/**
 * Created by MOHAMED on 07/05/2016.
 */
public class SplashActivity extends ActionBarActivity {

    ImageView iv1;
    TextView textView ;
    AnimationDrawable Anim;
    //35
    String[] nnn = new String[]{"W","e"," ","H","e","l","p"," ","Y","o","u"," ","T","o"," ","M","a","n","a","g","e"," ","Y","o","u","r",
                                 " ","B","u","s","i","n","e","s","s"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash);
        iv1 = (ImageView) findViewById(R.id.imageView1);
        textView = (TextView) findViewById(R.id.textView17);

//        try {
//            BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_048);
//            BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_049);
//            BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_050);
//            BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_051);
//            BitmapDrawable frame5 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_052);
//            BitmapDrawable frame6 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_053);
//            BitmapDrawable frame7 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_054);
//            BitmapDrawable frame8 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_055);
//            BitmapDrawable frame9 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_056);
//            BitmapDrawable frame10 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_057);
//            BitmapDrawable frame11= (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_058);
//            BitmapDrawable frame12= (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_059);
//            BitmapDrawable frame13= (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_060);
//            BitmapDrawable frame14= (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_061);
//            BitmapDrawable frame15= (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_062);
//            BitmapDrawable frame16 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_063);
//            BitmapDrawable frame17 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_064);
//            BitmapDrawable frame18 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_065);
//            BitmapDrawable frame19 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_066);
//            BitmapDrawable frame20 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_067);
//            BitmapDrawable frame21 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_068);
//            BitmapDrawable frame22 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_069);
//            BitmapDrawable frame23 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_070);
//            BitmapDrawable frame24 = (BitmapDrawable) getResources().getDrawable(R.drawable.cm10_071);
//
//
//            Anim = new AnimationDrawable();
//            Anim.addFrame(frame1, 0);
//            Anim.addFrame(frame2, 0);
//            Anim.addFrame(frame3, 0);
//            Anim.addFrame(frame4, 0);
//            Anim.addFrame(frame5, 0);
//            Anim.addFrame(frame6, 0);
//            Anim.addFrame(frame7, 0);
//            Anim.addFrame(frame8, 0);
//            Anim.addFrame(frame9, 0);
//            Anim.addFrame(frame10, 0);
//            Anim.addFrame(frame11, 0);
//            Anim.addFrame(frame12, 0);
//            Anim.addFrame(frame13, 0);
//            Anim.addFrame(frame14, 0);
//            Anim.addFrame(frame15, 0);
//            Anim.addFrame(frame16, 0);
//            Anim.addFrame(frame17, 0);
//            Anim.addFrame(frame18, 0);
//            Anim.addFrame(frame19, 0);
//            Anim.addFrame(frame20, 0);
//            Anim.addFrame(frame21, 0);
//            Anim.addFrame(frame22, 0);
//            Anim.addFrame(frame23, 0);
//            Anim.addFrame(frame24, 0);
//
//            Anim.setOneShot(false);
//            iv1.setBackgroundDrawable(Anim);
//            Anim.start();
//
            final Handler h = new Handler();
            final Runnable r = new Runnable() {
                int count = 0;
                String s ="";
                @Override
                public void run() {

                    if(count!=35) {
                        s += nnn[count];
                        textView.setText(s);
                        h.postDelayed(this, 100); //ms
                        count++;
                    }
                }
            };
            h.postDelayed(r, 100); // one second in ms
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                public void run() {
                    Log.d("IntroCheck : ",SaveSharedPreference.getIntroCheck(SplashActivity.this));
                    System.out.println("IntroCheck : "+SaveSharedPreference.getIntroCheck(SplashActivity.this));

                    if(SaveSharedPreference.getUserKeepLogin(SplashActivity.this).equalsIgnoreCase("check")||
                            SaveSharedPreference.getIntroCheck(SplashActivity.this).equalsIgnoreCase("checked")){
                        Intent intent_A = new Intent(SplashActivity.this, App_Home.class);
                        intent_A.addCategory(Intent.CATEGORY_HOME);
                        intent_A.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_A);

                    }else if (SaveSharedPreference.getIntroCheck(SplashActivity.this).equalsIgnoreCase("checked")){
                        Intent intent_L = new Intent(SplashActivity.this, LoginActivity.class);
                        intent_L.addCategory(Intent.CATEGORY_HOME);
                        intent_L.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_L);

                    }else {

                        finish();
                        //Intent intent_I = new Intent(SplashActivity.this, Intro.class);
                        Intent intent_I = new Intent(SplashActivity.this, LoginActivity.class);
                        intent_I.addCategory(Intent.CATEGORY_HOME);
                        intent_I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_I);
                    }


                }
            }, 5500);

//        } catch (Exception e) {
//            // TODO: handle exception
//        }


    }
}

