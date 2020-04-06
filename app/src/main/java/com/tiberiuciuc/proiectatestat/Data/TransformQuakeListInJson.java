package com.tiberiuciuc.proiectatestat.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.tiberiuciuc.proiectatestat.Model.EarthQuake;

import java.util.ArrayList;
import java.util.List;

public class TransformQuakeListInJson extends AsyncTask<List<EarthQuake>, Void, String> {
    private static final String TAG = "TransformQuakeListInJson";

    private String data = null;
    private List<EarthQuake> quakeList = null;

    private OnJsonAvailable callback;

    public interface OnJsonAvailable{
        void onJsonAvailable(String data);
    }

    public TransformQuakeListInJson(OnJsonAvailable callback, List<EarthQuake> quakeList) {
        this.quakeList = quakeList;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(List<EarthQuake>... lists) {
        Log.d(TAG, "doInBackground: starts");
        Gson gson = new Gson();
        data = gson.toJson(quakeList);
        Log.d(TAG, "doInBackground: ends");
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");
        if (callback!=null){
            callback.onJsonAvailable(data);
        }
        Log.d(TAG, "onPostExecute: ends");
    }
}
