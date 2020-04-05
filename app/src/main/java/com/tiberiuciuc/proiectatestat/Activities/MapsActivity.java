package com.tiberiuciuc.proiectatestat.Activities;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tiberiuciuc.proiectatestat.Activities.DownloadStatus;
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.R;
import com.tiberiuciuc.proiectatestat.UI.CustomInfoWindow;
import com.tiberiuciuc.proiectatestat.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GetJsonData.OnDataAvailable {
    private final static String TAG = "MapsActivity";

    private GoogleMap mMap;
    private RequestQueue queue;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private FloatingActionButton fabShowList;
    private FloatingActionButton fabSettings;

    private List<EarthQuake> quakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get data saved on device back
        SharedPreferences getSharedData = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        Constants.getInstance().setMAP_TYPE(getSharedData.getInt("MAP_TYPE", 4));
        Constants.getInstance().setURL(getSharedData.getString("URL", "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson"));
        Constants.getInstance().setURL_TYPE(getSharedData.getString("URL_TYPE", "significant"));
        Constants.getInstance().setURL_PERIOD(getSharedData.getString("URL_PERIOD", "month"));

        //The rest of the app is now loading

        quakeList = new ArrayList<EarthQuake>();

        fabShowList = findViewById(R.id.floating_action_show_list);
        fabSettings = findViewById(R.id.floating_action_settings);

        fabShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, QuakesListActivity.class));
            }
        });
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, SettingsActivity.class));
            }
        });

        queue = Volley.newRequestQueue(this);

        //getEarthQuakes();


    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetJsonData getJsonData = new GetJsonData(this, Constants.getInstance().getURL());
        getJsonData.execute("");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(List<EarthQuake> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            showEarthQuakesOnMap(data);
        } else {
            //something failed...
            Log.d(TAG, "onDataAvailable: failed with status: " + status.toString());
        }
    }

    private void showEarthQuakesOnMap(List<EarthQuake> data) {
        EarthQuake earthQuake = new EarthQuake();
        for (int i = 0; i < data.size(); i++) {
            earthQuake = (EarthQuake) data.get(i);
            //format the date
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(earthQuake.getTime()).getTime());

            //add object to map
            MarkerOptions markerOptions = new MarkerOptions();
            if (earthQuake.getMagnitude() <= 2)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            else if (earthQuake.getMagnitude() <= 5)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            else if (earthQuake.getMagnitude() <= 8)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            else
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerOptions.title(earthQuake.getPlace());
            markerOptions.position(new LatLng(earthQuake.getLat(), earthQuake.getLon()));
            markerOptions.snippet(
                    "Magnitude: " + earthQuake.getMagnitude() + "\n" +
                            "Date: " + formattedDate
            );

            Marker marker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(earthQuake.getLat(), earthQuake.getLon()), 1));
            marker.setTag(earthQuake.getDetailLink());
        }
    }

    private void getEarthQuakes() {

        final EarthQuake earthQuake = new EarthQuake();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.getInstance().getURL(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray features = response.getJSONArray("features");
                            for (int i = 0; i < features.length(); i++) {
                                //get the json
                                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
                                JSONArray coordinates = geometry.getJSONArray("coordinates");
                                double lon = coordinates.getDouble(0);
                                double lat = coordinates.getDouble(1);

                                //place json data in an object
                                earthQuake.setPlace(properties.getString("place"));
                                earthQuake.setType(properties.getString("type"));
                                earthQuake.setTime(properties.getLong("time"));
                                earthQuake.setMagnitude(properties.getDouble("mag"));
                                earthQuake.setDetailLink(properties.getString("detail"));
                                earthQuake.setLat(lat);
                                earthQuake.setLon(lon);

                                quakeList.add(earthQuake);

                                //format the date
                                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                                String formattedDate = dateFormat.format(new Date(earthQuake.getTime()).getTime());

                                //add object to map
                                MarkerOptions markerOptions = new MarkerOptions();
                                if (earthQuake.getMagnitude() <= 2)
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                else if (earthQuake.getMagnitude() <= 5)
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                else if (earthQuake.getMagnitude() <= 8)
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                else
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                markerOptions.title(earthQuake.getPlace());
                                markerOptions.position(new LatLng(lat, lon));
                                markerOptions.snippet(
                                        "Magnitude: " + earthQuake.getMagnitude() + "\n" +
                                                "Date: " + formattedDate
                                );

                                Marker marker = mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 1));
                                marker.setTag(earthQuake.getDetailLink());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(jsonObjectRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(Constants.getInstance().getMAP_TYPE());
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        getQuakeDetails(marker.getTag().toString());

    }

    private void getQuakeDetails(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String detailsUrl = "";
                        try {
                            JSONObject properties = response.getJSONObject("properties");
                            JSONObject products = properties.getJSONObject("products");
                            JSONArray geoserve = products.getJSONArray("geoserve");
                            for (int i = 0; i < geoserve.length(); i++) {
                                JSONObject geoserveObj = geoserve.getJSONObject(i);
                                JSONObject contentObj = geoserveObj.getJSONObject("contents");
                                JSONObject geoJsonObj = contentObj.getJSONObject("geoserve.json");

                                detailsUrl = geoJsonObj.getString("url");
                            }
                            Log.d("DETAILS_URL", detailsUrl);
                            getMoreDetails(detailsUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    public void getMoreDetails(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.popup, null);

                        Button dismissButton = view.findViewById(R.id.dismissPop);
                        Button dismissButtonTop = view.findViewById(R.id.dismissPopTop);
                        TextView popTitle = findViewById(R.id.popTitle);
                        TextView popList = view.findViewById(R.id.popList);
                        WebView htmlPop = view.findViewById(R.id.htmlWebView);

                        StringBuilder stringBuilder = new StringBuilder();
                        try {
                            if (response.has("tectonicSummary") && response.getString("tectonicSummary") != null) {
                                JSONObject tectonic = response.getJSONObject("tectonicSummary");
                                if (tectonic.has("text") && tectonic.getString("text") != null) {
                                    String text = tectonic.getString("text");
                                    htmlPop.loadDataWithBaseURL(
                                            null,
                                            text, //String data
                                            "text/html",
                                            "UTF-8",
                                            null
                                    );
                                }
                            }
                            JSONArray cities = response.getJSONArray("cities");
                            for (int i = 0; i < cities.length(); i++) {
                                JSONObject citiesObj = cities.getJSONObject(i);
                                stringBuilder.append("City: " +
                                        citiesObj.getString("name") + "\n" +
                                        "Distance: " +
                                        citiesObj.getString("distance") + " KM\n" +
                                        "Population: " +
                                        citiesObj.getString("population")
                                );
                                stringBuilder.append("\n\n");
                            }
                            //if (earthQuake.getPlace() != null)
                            //popTitle.setText(earthQuake.getPlace());

                            popList.setText(stringBuilder);

                            dismissButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dismissButtonTop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });


                            dialogBuilder.setView(view);
                            dialog = dialogBuilder.create();
                            dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private long backPressedTime;

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            backPressedTime = System.currentTimeMillis();
        }
    }
}
