package com.tiberiuciuc.proiectatestat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tiberiuciuc.proiectatestat.R;
import com.tiberiuciuc.proiectatestat.Util.Constants;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    private RadioButton rbNormal;
    private RadioButton rbSatellite;
    private RadioButton rbTerrain;
    private RadioButton rbHybrid;

    private RadioGroup radioGroupType;
    private RadioButton rbSignificant;
    private RadioButton rb4_5Plus;
    private RadioButton rb2_5Plus;
    private RadioButton rb1_0Plus;
    private RadioButton rbAll;

    private RadioGroup radioGroupTimePeriod;
    private RadioButton rbMonth;
    private RadioButton rbWeek;
    private RadioButton rbDay;
    private RadioButton rbHour;

    private Button applyChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.action_bar_settings);
        setSupportActionBar(toolbar);

        radioGroup = findViewById(R.id.radio_group);
        rbNormal = findViewById(R.id.rb_normal);
        rbSatellite = findViewById(R.id.rb_satellite);
        rbTerrain = findViewById(R.id.rb_terrain);
        rbHybrid = findViewById(R.id.rb_hybrid);

        radioGroupType = findViewById(R.id.radio_group_type);
        rbSignificant = findViewById(R.id.rb_significant);
        rb4_5Plus = findViewById(R.id.rb_M4_5PLUS);
        rb2_5Plus = findViewById(R.id.rb_M2_5PLUS);
        rb1_0Plus = findViewById(R.id.rb_M1_0PLUS);
        rbAll = findViewById(R.id.rb_all_earthquakes);

        radioGroupTimePeriod = findViewById(R.id.radio_group_time_period);
        rbMonth = findViewById(R.id.rb_past_month);
        rbWeek = findViewById(R.id.rb_past_week);
        rbDay = findViewById(R.id.rb_past_day);
        rbHour = findViewById(R.id.rb_past_hour);

        applyChanges = findViewById(R.id.btn_apply_changes);

        SharedPreferences getSharedData = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        switch (getSharedData.getInt("MAP_TYPE", 4)) {
            case 1:
                rbNormal.toggle();
                break;
            case 2:
                rbSatellite.toggle();
                break;
            case 3:
                rbTerrain.toggle();
                break;
            case 4:
                rbHybrid.toggle();
                break;
        }

        switch (getSharedData.getString("URL_TYPE", "significant")) {
            case "significant":
                rbSignificant.toggle();
                break;
            case "4.5":
                rb4_5Plus.toggle();
                break;
            case "2.5":
                rb2_5Plus.toggle();
                break;
            case "1.0":
                rb1_0Plus.toggle();
                break;
            case "all":
                rbAll.toggle();
                break;
        }

        switch (getSharedData.getString("URL_PERIOD", "month")) {
            case "month":
                rbMonth.toggle();
                break;
            case "week":
                rbWeek.toggle();
                break;
            case "day":
                rbDay.toggle();
                break;
            case "hour":
                rbHour.toggle();
                break;
        }

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("MAP_TYPE", Constants.getInstance().getMAP_TYPE());

                editor.putString("URL_TYPE", Constants.getInstance().getURL_TYPE());
                editor.putString("URL_PERIOD", Constants.getInstance().getURL_PERIOD());
                String buildUrl = Constants.getInstance().getURL_BASE() +
                        Constants.getInstance().getURL_TYPE() +
                        "_" +
                        Constants.getInstance().getURL_PERIOD() +
                        Constants.getInstance().getURL_END();
                Constants.getInstance().setURL(buildUrl);
                editor.putString("URL", Constants.getInstance().getURL());

                editor.apply();

                startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                finish();
            }
        });
    }


    public void onMapTypeChosen(View view) {
        switch (view.getId()) {
            case R.id.rb_normal:
                if (rbNormal.isChecked()) {
                    Constants.getInstance().setMAP_TYPE(1);
                }
                break;
            case R.id.rb_satellite:
                if (rbSatellite.isChecked()) {
                    Constants.getInstance().setMAP_TYPE(2);
                }
                break;
            case R.id.rb_terrain:
                if (rbTerrain.isChecked()) {
                    Constants.getInstance().setMAP_TYPE(3);
                }
                break;
            case R.id.rb_hybrid:
                if (rbHybrid.isChecked()) {
                    Constants.getInstance().setMAP_TYPE(4);
                }
                break;
        }
    }

    public void onEarthquakeTypeChosen(View view) {
        switch (view.getId()) {
            case R.id.rb_significant:
                if (rbSignificant.isChecked()) {
                    Constants.getInstance().setURL_TYPE("significant");
                }
                break;
            case R.id.rb_M4_5PLUS:
                if (rb4_5Plus.isChecked()) {
                    Constants.getInstance().setURL_TYPE("4.5");
                }
                break;
            case R.id.rb_M2_5PLUS:
                if (rb2_5Plus.isChecked()) {
                    Constants.getInstance().setURL_TYPE("2.5");
                }
                break;
            case R.id.rb_M1_0PLUS:
                if (rb1_0Plus.isChecked()) {
                    Constants.getInstance().setURL_TYPE("1.0");
                }
                break;
            case R.id.rb_all_earthquakes:
                if (rbAll.isChecked()) {
                    Constants.getInstance().setURL_TYPE("all");
                }
                break;
        }
    }

    public void onEarthquakeTimePeriodChosen(View view) {
        switch (view.getId()) {
            case R.id.rb_past_month:
                if (rbMonth.isChecked()) {
                    Constants.getInstance().setURL_PERIOD("month");
                }
                break;
            case R.id.rb_past_week:
                if (rbWeek.isChecked()) {
                    Constants.getInstance().setURL_PERIOD("week");
                }
                break;
            case R.id.rb_past_day:
                if (rbDay.isChecked()) {
                    Constants.getInstance().setURL_PERIOD("day");
                }
                break;
            case R.id.rb_past_hour:
                if (rbHour.isChecked()) {
                    Constants.getInstance().setURL_PERIOD("hour");
                }
                break;
        }
    }
}
