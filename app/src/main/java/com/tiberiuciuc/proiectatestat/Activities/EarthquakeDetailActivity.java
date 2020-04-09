package com.tiberiuciuc.proiectatestat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.tiberiuciuc.proiectatestat.Data.GetJsonDetailLink;
import com.tiberiuciuc.proiectatestat.Data.GetMoreDetails;
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.Model.EarthquakeDetails;
import com.tiberiuciuc.proiectatestat.R;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;

import java.util.Date;

public class EarthquakeDetailActivity extends AppCompatActivity implements GetMoreDetails.OnMoreDetailsAvailable, GetJsonDetailLink.OnDetailLinkAvailable {
    private static final String TAG = "EarthquakeDetailActivity";

    private EarthQuake earthQuake;
    private Toolbar toolbar;
    private TextView magnitude;
    private TextView date;
    private TextView coord;
    private TextView cities;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        buildEarthquake();

        toolbar = findViewById(R.id.action_bar_earthquake_detail);
        setSupportActionBar(toolbar);
        toolbar.setTitle(earthQuake.getPlace());

        magnitude = findViewById(R.id.text_view_mag);
        date = findViewById(R.id.text_view_date);
        coord = findViewById(R.id.text_view_coordinates);
        cities = findViewById(R.id.surrounding_cities);
        webView = findViewById(R.id.web_view);

        magnitude.setText(String.format("Magnitude: %s", earthQuake.getMagnitude()));
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = "Date: " + dateFormat.format(new Date(earthQuake.getTime()).getTime());
        date.setText(formattedDate);
        coord.setText(String.format("Coordinates: %s ,%s", earthQuake.getLat(), earthQuake.getLon()));

        coord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(earthQuake.getLat() + ", " + earthQuake.getLon());
                Toast.makeText(EarthquakeDetailActivity.this, "Coordinates copied!", Toast.LENGTH_SHORT).show();
            }
        });

        getDetailLink(earthQuake.getDetailLink(), earthQuake);
    }

    private void getDetailLink(String url, EarthQuake earthQuake){
        GetJsonDetailLink getJsonDetailLink = new GetJsonDetailLink(
                this,
                url,
                earthQuake
        );
        getJsonDetailLink.execute();
    }

    @Override
    public void onDetailLinkAvailable(String detailLink, DownloadStatus status, EarthQuake earthQuake) {
        GetMoreDetails getMoreDetails = new GetMoreDetails(
                this,
                detailLink,
                earthQuake
        );
        getMoreDetails.execute();
    }

    @Override
    public void onMoreDetailsAvailable(EarthquakeDetails earthquakeDetails, DownloadStatus status, EarthQuake earthQuake) {
        Log.d(TAG, "onMoreDetailsAvailable: starts");
        if (status == DownloadStatus.OK && earthquakeDetails != null) {

            Log.d("DETAILS", earthquakeDetails.getBuilder().toString());

            if (!earthquakeDetails.getHtmlText().equals("")) {
                webView.loadDataWithBaseURL(
                        null,
                        earthquakeDetails.getHtmlText(), //String data
                        "text/html",
                        "UTF-8",
                        null
                );
            }

            cities.setText(earthquakeDetails.getBuilder());

        } else {
            Log.d(TAG, "onMoreDetailsAvailable: failed with status: " + status);
        }
    }

    private void buildEarthquake() {
        earthQuake = new EarthQuake();
        earthQuake.setPlace(getIntent().getStringExtra("EQ_PLACE"));
        earthQuake.setMagnitude(Double.valueOf(getIntent().getStringExtra("EQ_MAG")));
        earthQuake.setTime(Long.valueOf(getIntent().getStringExtra("EQ_TIME")));
        earthQuake.setDetailLink(getIntent().getStringExtra("EQ_LINK"));
        earthQuake.setType(getIntent().getStringExtra("EQ_TYPE"));
        earthQuake.setLat(Double.valueOf(getIntent().getStringExtra("EQ_LAT")));
        earthQuake.setLon(Double.valueOf(getIntent().getStringExtra("EQ_LON")));
    }
}
