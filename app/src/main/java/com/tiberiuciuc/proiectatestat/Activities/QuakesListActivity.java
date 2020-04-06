package com.tiberiuciuc.proiectatestat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiberiuciuc.proiectatestat.Data.GetJsonData;
import com.tiberiuciuc.proiectatestat.Data.TransformJsonInQuakeList;
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.R;
import com.tiberiuciuc.proiectatestat.Util.Constants;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity implements TransformJsonInQuakeList.OnQuakeListAvailable {
    private static final String TAG = "QuakesListActivity";

    private ListView listView;
    private ArrayAdapter arrayAdapter;

    private List<EarthQuake> quakeList;
    private ArrayList<String> arrayList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes_list);

        toolbar = findViewById(R.id.action_bar_quakes_list);
        setSupportActionBar(toolbar);

        quakeList = new ArrayList<>();
        arrayList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        String earthQuakeListAsJsonString = getIntent().getStringExtra("EARTHQUAKE_LIST");
        TransformJsonInQuakeList jsonInQuakeList = new TransformJsonInQuakeList(this, earthQuakeListAsJsonString);
        jsonInQuakeList.execute();
    }

    void getAllQuakes() {
        /*
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(earthQuake.getTime()).getTime());
        */
        arrayAdapter = new ArrayAdapter<>(QuakesListActivity.this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(QuakesListActivity.this, "Clicked: " + i, Toast.LENGTH_SHORT).show();
                //a way to view more details
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQuakeListAvailable(List<EarthQuake> quakeList) {
        this.quakeList = quakeList;
        for (int i = 0; i < this.quakeList.size(); i++)
            arrayList.add(this.quakeList.get(i).getPlace());
        toolbar.setTitle("Total Earthquakes: " + quakeList.size());
        getAllQuakes();
    }
}
