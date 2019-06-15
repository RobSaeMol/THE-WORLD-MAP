 package com.example.googlemaps;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.LambdaConversionException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng position ;
    String city;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONObject settings = null;
        try {
            settings = new JSONObject(getIntent().getStringExtra("app_settings"));

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (settings.has("language")) {
                Locale mylocale = new Locale(settings.getString("language"));
                Locale.setDefault(mylocale);

                Configuration myconfig = new Configuration();
                myconfig.setLocale(mylocale);

                getApplicationContext().createConfigurationContext(myconfig);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.mapstyle));

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                position = latLng;
                (new asyncnetworktask()).execute(latLng);




            }
        });
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    void countryName ( String country){
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        //builder.setMessage("you clicked on " + country + "  (lat " + position.latitude + " long " + position.longitude+")");
        builder.setMessage(country);
        builder.setTitle("clicked location");
        builder.create().show();
    }

    private class asyncnetworktask extends AsyncTask<LatLng, Void, JSONObject> {


        protected String apikey = "9f5d9fff00849b";
        private String url = "https://eu1.locationiq.com/v1/reverse.php";

        @Override
        protected JSONObject doInBackground(LatLng... positions) {
            try {

                HttpURLConnection connection = (HttpURLConnection) new URL(
                        url + "?key=" + apikey + "&lat=" +  positions[0].latitude + "&lon=" +  positions[0].longitude + "&format=json"
                ).openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String SmallChunk;
                StringBuilder builder = new StringBuilder();
                while ((SmallChunk = reader.readLine()) != null) {
                    builder.append(SmallChunk);

                }

                reader.close();
                connection.disconnect();

                String finalResult = builder.toString();

                return new JSONObject(finalResult);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                Log.d("MAPONCLICK", jsonObject.getJSONObject("address").getString("country"));
                (new asyncnetworktask2()).execute(jsonObject.getJSONObject("address").getString("country"));
               // city(jsonObject.getJSONObject("address").getString("county"));
            } catch (Exception e) {
            }


        }
    }


    private class asyncnetworktask2 extends AsyncTask<String, Void, JSONArray> {


        String url = "https://restcountries.eu/rest/v2/name/";

        @Override
        protected JSONArray doInBackground(String... names) {
            try {

                HttpURLConnection connection = (HttpURLConnection) new URL(
                        url + names[0]
                ).openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String SmallChunk;
                StringBuilder builder = new StringBuilder();
                while ((SmallChunk = reader.readLine()) != null) {
                    builder.append(SmallChunk);

                }

                reader.close();
                connection.disconnect();

                String finalResult = builder.toString();

                return new JSONArray(finalResult);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return  null;
        }


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            try {
               // countryName("you clicked on " + jsonArray.getJSONObject(0).getString("name") + " wich has a population of " + jsonArray.getJSONObject(0).getInt("population"));
                Intent newIntent = new Intent(getApplicationContext(),info.class);
                startActivity(newIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
