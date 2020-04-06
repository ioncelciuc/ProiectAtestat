package com.tiberiuciuc.proiectatestat.Data;

import android.os.AsyncTask;
import android.util.Log;


import com.tiberiuciuc.proiectatestat.Util.DownloadStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus downloadStatus;
    private OnDownloadComplete callback;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus downloadStatus);
    }

    public GetRawData(OnDownloadComplete callback) {
        downloadStatus = DownloadStatus.IDLE;
        this.callback = callback;
    }

    void runInSameThread(String URL) {
        Log.d(TAG, "runInSameThread: starts");
        if (callback != null) {
            String result = doInBackground(URL);
            callback.onDownloadComplete(result, downloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts");
        if (callback != null) {
            callback.onDownloadComplete(s, downloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: the response code was: " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            downloadStatus = DownloadStatus.OK;
            return result.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: Exception encountered while reading data: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }

        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
