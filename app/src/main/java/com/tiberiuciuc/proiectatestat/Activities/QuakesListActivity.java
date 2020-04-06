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
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.R;
import com.tiberiuciuc.proiectatestat.Util.Constants;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity implements GetJsonData.OnDataAvailable {
    private static final String TAG = "QuakesListActivity";

    private ArrayList<String> arrayList;
    private ListView listView;
    private ArrayAdapter arrayAdapter;

    private List<EarthQuake> quakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes_list);

        Toolbar toolbar = findViewById(R.id.action_bar_quakes_list);
        setSupportActionBar(toolbar);
        /*
        String earthQuakeListAsJsonString = getIntent().getStringExtra("EARTHQUAKE_LIST");
        quakeList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<EarthQuake>>() {
        }.getType();
        quakeList = gson.fromJson(earthQuakeListAsJsonString, type);
        */
        quakeList = new ArrayList<>();

        arrayList = new ArrayList<>();


        //toolbar.setTitle("Total earthquakes: " + quakeList.size());

        listView = findViewById(R.id.listView);

        //getAllQuakes();

        GetJsonData getJsonData = new GetJsonData(this, Constants.getInstance().getURL());
        getJsonData.execute();
    }

    @Override
    public void onDataAvailable(List<EarthQuake> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            quakeList = data;
            if (quakeList != null) {
                for (int i = 0; i < quakeList.size(); i++)
                    arrayList.add(quakeList.get(i).getPlace());
                getAllQuakes();
            }
        } else {
            //something failed...
            Log.d(TAG, "onDataAvailable: failed with status: " + status.toString());
        }
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
}
