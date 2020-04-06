package com.tiberiuciuc.proiectatestat.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransformJsonInQuakeList extends AsyncTask<String, Void, List<EarthQuake>> {
    private static final String TAG = "TransformJsonInQuakeList";

    private String jsonQuakeList;
    private List<EarthQuake> quakeList;

    private OnQuakeListAvailable callback;

    public interface OnQuakeListAvailable {
        void onQuakeListAvailable(List<EarthQuake> quakeList);
    }

    public TransformJsonInQuakeList(OnQuakeListAvailable callback, String jsonQuakeList) {
        this.jsonQuakeList = jsonQuakeList;
        this.callback = callback;
    }

    @Override
    protected List<EarthQuake> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        quakeList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<EarthQuake>>() {
        }.getType();
        quakeList = gson.fromJson(jsonQuakeList, type);
        Log.d(TAG, "doInBackground: ends");
        return quakeList;
    }

    @Override
    protected void onPostExecute(List<EarthQuake> list) {
        Log.d(TAG, "onPostExecute: starts");
        if (callback != null) {
            callback.onQuakeListAvailable(quakeList);
        }
        Log.d(TAG, "onPostExecute: ends");
    }
}
