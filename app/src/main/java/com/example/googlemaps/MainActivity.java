package com.example.googlemaps;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    JSONObject settings;
    Button button;
    ConstraintLayout earth;
    ConstraintLayout whiteCircle;
    String TAG = "MAPSAPP";
    ObjectAnimator buttonSpin;
    ObjectAnimator bscaleY;
    ObjectAnimator bscaleX;
    ObjectAnimator planetX;
    ObjectAnimator planetY;
//    ValueAnimator colorAnimator;
    ObjectAnimator whiteAlpha;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            File file = new File(getFilesDir(), "settings.json");
            file.delete();
            Log.d(TAG, "file deleted");
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("settings.json")));
            settings = new JSONObject(reader.readLine());

        } catch (FileNotFoundException e) {


            try {
                settings = new JSONObject();
                settings.put("theme", "other");
                settings.put("language","en-GB");
                settings.put("API Key ", "9f5d9fff00849b");
                FileOutputStream output = openFileOutput("settings.json", Context.MODE_PRIVATE);
                output.write(settings.toString().getBytes());
                output.close();

            } catch (Exception e2) {
                e.printStackTrace();
            }
        } catch (Exception e) {
e.printStackTrace();
        }
        try {
            if (settings.getString("theme").equals("other")) {
                setTheme(R.style.MyTheme);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.opening);
        button = findViewById(R.id.button);
        earth = findViewById(R.id.earth);
        whiteCircle = findViewById(R.id.whiteAlpha);
        ImageButton settingsbutton = findViewById(R.id.settingsbutton);
        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(),settingsjava.class);
                newIntent.putExtra("app_settings",settings.toString());
                startActivity(newIntent);



            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSpin.start();
                bscaleY.start();
                bscaleX.start();
                planetX.start();
                planetY.start();
                whiteAlpha.start();


            }
        });

        buttonSpin = ObjectAnimator.ofFloat(button,"rotation",360);
        buttonSpin.setDuration(1800);
        buttonSpin.setRepeatMode(ValueAnimator.RESTART);
        bscaleY = ObjectAnimator.ofFloat(button,"scaleY",0.002f);
        bscaleY.setDuration(900);
        bscaleY.setRepeatMode(ValueAnimator.RESTART);
        bscaleX = ObjectAnimator.ofFloat(button,"scaleX",0.001f);
        bscaleX.setDuration(900);
        bscaleX.setRepeatMode(ValueAnimator.RESTART);
        planetX = ObjectAnimator.ofFloat(earth, "scaleX",10);
        planetX.setDuration(1000);
        planetX.setRepeatMode(ValueAnimator.RESTART);
        planetY = ObjectAnimator.ofFloat(earth, "scaleY",10);
        planetY.setDuration(1000);
        planetY.setRepeatMode(ValueAnimator.RESTART);
        whiteAlpha = ObjectAnimator.ofFloat(whiteCircle, "alpha", 1);
        whiteAlpha.setDuration(1000);
        whiteAlpha.setRepeatMode(ValueAnimator.RESTART);



        buttonSpin.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent newIntent = new Intent(getApplicationContext(),MapsActivity.class);
                newIntent.putExtra("app_settings",settings.toString());
                startActivity(newIntent);

          }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        button.setRotation(0);
        button.setScaleY(1);
        button.setScaleX(1);
        earth.setScaleX(1);
        earth.setScaleY(1);
        whiteCircle.setAlpha(0);
        whiteCircle.setAlpha(0);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
