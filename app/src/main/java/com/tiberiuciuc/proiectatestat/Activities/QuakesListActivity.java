package com.tiberiuciuc.proiectatestat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.tiberiuciuc.proiectatestat.UI.MyRecyclerViewAdapter;
import com.tiberiuciuc.proiectatestat.UI.RecyclerViewOnClickListener;
import com.tiberiuciuc.proiectatestat.Util.Constants;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity implements
        TransformJsonInQuakeList.OnQuakeListAvailable,
        RecyclerViewOnClickListener.OnRecyclerClickListener {
    private static final String TAG = "QuakesListActivity";

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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
        recyclerView = findViewById(R.id.recyclerView);

        String earthQuakeListAsJsonString = getIntent().getStringExtra("EARTHQUAKE_LIST");
        TransformJsonInQuakeList jsonInQuakeList = new TransformJsonInQuakeList(this, earthQuakeListAsJsonString);
        jsonInQuakeList.execute();
    }

    void getAllQuakes() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, recyclerView, this));
        adapter = new MyRecyclerViewAdapter(this, quakeList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onQuakeListAvailable(List<EarthQuake> quakeList) {
        this.quakeList = quakeList;
        for (int i = 0; i < this.quakeList.size(); i++)
            arrayList.add(this.quakeList.get(i).getPlace());
        toolbar.setTitle("Total Earthquakes: " + quakeList.size());
        getAllQuakes();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EarthquakeDetailActivity.class);
        //earthquakeToJson
        startActivity(intent);
    }
}
