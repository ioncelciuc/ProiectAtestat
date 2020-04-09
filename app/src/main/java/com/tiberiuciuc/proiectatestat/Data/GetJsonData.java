package com.tiberiuciuc.proiectatestat.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GetJsonData extends AsyncTask<String, Void, List<EarthQuake>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetJsonData";

    private List<EarthQuake> quakeList = null;
    private String URL = null;

    private OnDataAvailable callback;

    public interface OnDataAvailable {
        void onDataAvailable(List<EarthQuake> data, DownloadStatus status);
    }

    public GetJsonData(OnDataAvailable callback, String URL) {
        this.callback = callback;
        this.URL = URL;
    }

    @Override
    protected void onPostExecute(List<EarthQuake> list) {
        Log.d(TAG, "onPostExecute: starts");

        if (callback != null) {
            callback.onDataAvailable(list, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<EarthQuake> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(URL);
        Log.d(TAG, "doInBackground: ends");
        return quakeList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDownloadComplete: Starts");

        if (downloadStatus == DownloadStatus.OK) {
            quakeList = new ArrayList<>();
            try {
                JSONObject response = new JSONObject(data);
                JSONArray features = response.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    //get the json
                    JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                    JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    double lon = coordinates.getDouble(0);
                    double lat = coordinates.getDouble(1);
                    String place = properties.getString("place");
                    String type = properties.getString("type");
                    long time = properties.getLong("time");
                    double magnitude = Double.valueOf(new DecimalFormat("#.#").format(properties.getDouble("mag")));
                    String detailLink = properties.getString("detail");

                    EarthQuake earthQuake = new EarthQuake(place, magnitude, time, detailLink, type, lat, lon);
                    quakeList.add(earthQuake);

                    Log.d(TAG, "onDownloadComplete: " + earthQuake.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: error processing json data: " + e.getMessage());
                downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
