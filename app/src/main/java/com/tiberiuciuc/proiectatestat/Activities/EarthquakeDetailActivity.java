package com.tiberiuciuc.proiectatestat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.R;

public class EarthquakeDetailActivity extends AppCompatActivity {

    private EarthQuake earthQuake;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        buildEarthquake();

        textView = findViewById(R.id.textView);
        textView.setText(earthQuake.getPlace());
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
