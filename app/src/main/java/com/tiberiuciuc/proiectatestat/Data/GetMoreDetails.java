package com.tiberiuciuc.proiectatestat.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.Model.EarthquakeDetails;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetMoreDetails extends AsyncTask<String, Void, EarthquakeDetails> implements GetRawData.OnDownloadComplete{
    private static final String TAG = "GetMoreDetails";

    private EarthquakeDetails earthquakeDetails;
    private String URL;
    private EarthQuake earthquake;

    private OnMoreDetailsAvailable callback;
    private boolean runningOnSameThread = false;

    public interface OnMoreDetailsAvailable{
        void onMoreDetailsAvailable(EarthquakeDetails earthquakeDetails, DownloadStatus status, EarthQuake earthQuake);
    }

    public GetMoreDetails(OnMoreDetailsAvailable callback, String URL, EarthQuake earthquake) {
        this.URL = URL;
        this.callback = callback;
        this.earthquake = earthquake;
    }

    @Override
    protected void onPostExecute(EarthquakeDetails earthquakeDetails) {
        Log.d(TAG, "onPostExecute: starts");
        if(callback!=null){
            callback.onMoreDetailsAvailable(earthquakeDetails, DownloadStatus.OK, earthquake);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected EarthquakeDetails doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(URL);
        Log.d(TAG, "doInBackground: ends");
        return earthquakeDetails;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDownloadComplete: starts");

        if(downloadStatus == DownloadStatus.OK){
            earthquakeDetails = new EarthquakeDetails();
            StringBuilder stringBuilder = new StringBuilder();
            String text = "";
            try{
                JSONObject response = new JSONObject(data);
                if (response.has("tectonicSummary") && response.getString("tectonicSummary") != null) {
                    JSONObject tectonic = response.getJSONObject("tectonicSummary");
                    if (tectonic.has("text") && tectonic.getString("text") != null) {
                        text = tectonic.getString("text");
                    }
                }
                JSONArray cities = response.getJSONArray("cities");
                for (int i = 0; i < cities.length(); i++) {
                    JSONObject citiesObj = cities.getJSONObject(i);
                    stringBuilder.append("City: " +
                            citiesObj.getString("name") + "\n" +
                            "Distance: " +
                            citiesObj.getString("distance") + " KM\n" +
                            "Population: " +
                            citiesObj.getString("population")
                    );
                    stringBuilder.append("\n\n");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: error processing json data: " + e.getMessage());
                downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            }finally {
                earthquakeDetails.setBuilder(stringBuilder);
                earthquakeDetails.setHtmlText(text);
            }
        }

        if(runningOnSameThread && callback!=null){
            callback.onMoreDetailsAvailable(earthquakeDetails, downloadStatus, earthquake);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
