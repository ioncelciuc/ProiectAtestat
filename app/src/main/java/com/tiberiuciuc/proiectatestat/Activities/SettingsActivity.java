package com.tiberiuciuc.proiectatestat.Activities;

import androidx.appcompat.app.AppCompatActivity;

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

        switch (Constants.getInstance().getMAP_TYPE()) {
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

        switch (Constants.getInstance().getURL_TYPE()){
            case "significant":
                rbSignificant.toggle();
                break;
            case "4.5":
                rb4_5Plus.toggle();
            case  "2.5":
                rb2_5Plus.toggle();
            case "1.0":
                rb1_0Plus.toggle();
            case "all":
                rbAll.toggle();
        }

        switch (Constants.getInstance().getURL_PERIOD()){
            case "month":
                rbMonth.toggle();
                break;
            case "week":
                rbWeek.toggle();
            case "day":
                rbDay.toggle();
            case "hour":
                rbHour.toggle();
        }

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.getInstance().setMAP_TYPE(Constants.getInstance().getMAP_TYPE_PREPARE_CHANGES());
                //Constants.getInstance().setURL();

                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("MAP_TYPE", Constants.getInstance().getMAP_TYPE());
                
                editor.putString("URL", Constants.getInstance().getURL());

                editor.apply();

                startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
            }
        });
    }


    public void onMapTypeChosen(View view) {
        switch (view.getId()) {
            case R.id.rb_normal:
                if (rbNormal.isChecked()) {
                    Constants.getInstance().prepareMapTypeChanges(1);
                }
                break;
            case R.id.rb_satellite:
                if (rbSatellite.isChecked()) {
                    Constants.getInstance().prepareMapTypeChanges(2);
                }
                break;
            case R.id.rb_terrain:
                if (rbTerrain.isChecked()) {
                    Constants.getInstance().prepareMapTypeChanges(3);
                }
                break;
            case R.id.rb_hybrid:
                if (rbHybrid.isChecked()) {
                    Constants.getInstance().prepareMapTypeChanges(4);
                }
                break;
        }
    }
}
