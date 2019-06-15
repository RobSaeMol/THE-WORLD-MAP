package com.example.googlemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class settingsjava extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] spinner1 = new String[] { "dark Theme", "Light Theme" };
    JSONObject settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        if (getIntent().hasExtra("app_settings")) {
            try {
                settings = new JSONObject(getIntent().getStringExtra("app_settings"));
                Spinner AppTheme = findViewById(R.id.AppTheme);
                EditText APIKEY = findViewById(R.id.apikey);
                if (settings.getString("API Key").equals("9f5d9fff00849b")) {
                    APIKEY.setText(R.string.map_api_key);

                }else {
                    APIKEY.setText(settings.getString("API Key"));
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        spinner1);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                AppTheme.setAdapter(spinnerAdapter);
                AppTheme.setOnItemSelectedListener(this);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }






        ImageButton backToMap = findViewById(R.id.backToMap);

        backToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(getApplicationContext(),MainActivity.class);
                newIntent.putExtra("app_settings",settings.toString());
                startActivity(newIntent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
