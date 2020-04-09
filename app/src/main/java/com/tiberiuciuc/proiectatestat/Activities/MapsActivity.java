package com.tiberiuciuc.proiectatestat.Activities;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.tiberiuciuc.proiectatestat.Data.GetJsonData;
import com.tiberiuciuc.proiectatestat.Data.GetJsonDetailLink;
import com.tiberiuciuc.proiectatestat.Data.GetMoreDetails;
import com.tiberiuciuc.proiectatestat.Data.TransformQuakeListInJson;
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.Model.EarthquakeDetails;
import com.tiberiuciuc.proiectatestat.R;
import com.tiberiuciuc.proiectatestat.UI.CustomInfoWindow;
import com.tiberiuciuc.proiectatestat.Util.Constants;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GetJsonData.OnDataAvailable,
        GetJsonDetailLink.OnDetailLinkAvailable,
        GetMoreDetails.OnMoreDetailsAvailable,
        TransformQuakeListInJson.OnJsonAvailable {
    private final static String TAG = "MapsActivity";

    private GoogleMap mMap;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private FloatingActionButton fabShowList;
    private FloatingActionButton fabSettings;
    private FloatingActionButton fabRefresh;

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

        fabRefresh = findViewById(R.id.floating_action_refresh);
        fabShowList = findViewById(R.id.floating_action_show_list);
        fabSettings = findViewById(R.id.floating_action_settings);

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
            }
        });
        fabShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transformQuakeListInJson();
            }
        });
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, SettingsActivity.class));
            }
        });

        //getEarthQuakes();
        GetJsonData getJsonData = new GetJsonData(this, Constants.getInstance().getURL());
        getJsonData.execute();
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
    public void onDataAvailable(List<EarthQuake> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            quakeList = data;
            if (quakeList != null) showEarthQuakesOnMap(quakeList);
        } else {
            //something failed...
            Log.d(TAG, "onDataAvailable: failed with status: " + status.toString());
        }
    }

    private void showEarthQuakesOnMap(List<EarthQuake> data) {
        EarthQuake earthQuake;
        for (int i = 0; i < data.size(); i++) {
            earthQuake = data.get(i);
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        boolean foundEarthquake = false;
        for (int i = 0; i < quakeList.size() && !foundEarthquake; i++) {
            LatLng latLng = new LatLng(quakeList.get(i).getLat(), quakeList.get(i).getLon());
            if (latLng.equals(marker.getPosition())) {
                GetJsonDetailLink getJsonDetailLink = new GetJsonDetailLink(
                        this,
                        marker.getTag().toString(),
                        quakeList.get(i)
                );
                getJsonDetailLink.execute();
                foundEarthquake = true;
            }
        }
    }

    @Override
    public void onDetailLinkAvailable(String detailLink, DownloadStatus status, EarthQuake earthQuake) {
        if (status == DownloadStatus.OK && detailLink != null) {
            getMoreDetails(detailLink, earthQuake);
        }
    }

    public void getMoreDetails(String url, EarthQuake earthQuake) {
        GetMoreDetails getMoreDetails = new GetMoreDetails(
                this,
                url,
                earthQuake
        );
        getMoreDetails.execute();
    }

    @Override
    public void onMoreDetailsAvailable(EarthquakeDetails earthquakeDetails, DownloadStatus status, EarthQuake earthQuake) {
        Log.d(TAG, "onMoreDetailsAvailable: starts");
        if (status == DownloadStatus.OK && earthquakeDetails != null) {
            buildDialog(earthquakeDetails, earthQuake);
        } else {
            Log.d(TAG, "onMoreDetailsAvailable: failed with status: " + status);
        }
        Log.d(TAG, "onMoreDetailsAvailable: ends");
    }

    private void buildDialog(EarthquakeDetails earthquakeDetails, final EarthQuake earthQuake) {
        Log.d(TAG, "buildDialog: starts");
        dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        Button dismissButton = view.findViewById(R.id.dismiss_pop);
        Button dismissButtonTop = view.findViewById(R.id.dismiss_pop_top);
        TextView popTitle = view.findViewById(R.id.pop_title);
        TextView popMagnitude = view.findViewById(R.id.pop_magnitude);
        TextView popDate = view.findViewById(R.id.pop_date);
        TextView popLatLon = view.findViewById(R.id.pop_lat_lon);
        TextView popList = view.findViewById(R.id.pop_list);
        WebView htmlPop = view.findViewById(R.id.html_web_view);

        if (!earthquakeDetails.getHtmlText().equals("")) {
            htmlPop.loadDataWithBaseURL(
                    null,
                    earthquakeDetails.getHtmlText(), //String data
                    "text/html",
                    "UTF-8",
                    null
            );
        }

        popTitle.setText(earthQuake.getPlace());
        String popLatLonText = "Coordinates: " + earthQuake.getLat() + ", " + earthQuake.getLon();
        popLatLon.setText(popLatLonText);
        String popMagText = "Magnitude: " + earthQuake.getMagnitude();
        popMagnitude.setText(popMagText);
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = "Date: " + dateFormat.format(new Date(earthQuake.getTime()).getTime());
        popDate.setText(formattedDate);
        popList.setText(earthquakeDetails.getBuilder());

        popLatLon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(earthQuake.getLat() + ", " + earthQuake.getLon());
                Toast.makeText(MapsActivity.this, "Coordinates copied!", Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    private void transformQuakeListInJson(){
        TransformQuakeListInJson quakeListInJson = new TransformQuakeListInJson(this, quakeList);
        quakeListInJson.execute();
    }

    @Override
    public void onJsonAvailable(String data) {
        Intent intent = new Intent(MapsActivity.this, QuakesListActivity.class);
        intent.putExtra("EARTHQUAKE_LIST", data);
        startActivity(intent);
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
