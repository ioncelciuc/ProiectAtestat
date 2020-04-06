package com.tiberiuciuc.proiectatestat.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.tiberiuciuc.proiectatestat.Model.EarthQuake;
import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetJsonDetailLink extends AsyncTask<String, Void, String> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetJsonDetailLink";

    private String link = null;
    private String URL = null;

    OnDetailLinkAvailable callback;
    private boolean runningOnSameThread = false;
    EarthQuake earthQuake;

    public interface OnDetailLinkAvailable {
        void onDetailLinkAvailable(String detailLink, DownloadStatus status, EarthQuake earthQuake);
    }

    public GetJsonDetailLink(OnDetailLinkAvailable callback, String URL, EarthQuake earthQuake) {
        this.callback = callback;
        this.URL = URL;
        this.earthQuake = earthQuake;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");

        if (callback != null) {
            callback.onDetailLinkAvailable(link, DownloadStatus.OK, earthQuake);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(URL);
        Log.d(TAG, "doInBackground: ends");
        return link;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus downloadStatus) {
        if(downloadStatus == DownloadStatus.OK){
            link = "";
            try {
                JSONObject response = new JSONObject(data);
                JSONObject properties = response.getJSONObject("properties");
                JSONObject products = properties.getJSONObject("products");
                JSONArray geoserve = products.getJSONArray("geoserve");
                for (int i = 0; i < geoserve.length(); i++) {
                    JSONObject geoserveObj = geoserve.getJSONObject(i);
                    JSONObject contentObj = geoserveObj.getJSONObject("contents");
                    JSONObject geoJsonObj = contentObj.getJSONObject("geoserve.json");

                    link = geoJsonObj.getString("url");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: error processing json data: " + e.getMessage());
                downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(runningOnSameThread && callback!=null){
            callback.onDetailLinkAvailable(link, downloadStatus, earthQuake);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
