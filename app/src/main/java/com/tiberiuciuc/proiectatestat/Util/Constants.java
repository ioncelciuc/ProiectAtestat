package com.tiberiuciuc.proiectatestat.Util;

import android.content.SharedPreferences;

import java.net.ContentHandler;
//Singleton class
public class Constants {

    //----------------------------SINGLETON CLASS-------------------------------------

    //instanta privata, asa ca poate fi accesata doar prin getInstance();
    private static Constants instance;

    private Constants(){
        //constructor privat
    }

    //functie sincronizata pentru a putea fi controlat accesul simultan
    synchronized public static Constants getInstance()
    {
        //daca nu e nicio instanta a clasei, se initializeaza
        if (instance == null) instance = new Constants();
        return instance;
    }

    //------------------------------SINGLETON CLASS-----------------------------

    public static final String SETTINGS = "settings_prefs";

    private int MAP_TYPE = 4;

    private String URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
    private String URL_BASE = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/";
    private String URL_TYPE = "significant";
    private String URL_PERIOD = "month";
    private String URL_END = ".geojson";

    //MAP TYPE
    public int getMAP_TYPE() {
        return MAP_TYPE;
    }

    public void setMAP_TYPE(int MAP_TYPE) {
        this.MAP_TYPE = MAP_TYPE;
    }

    //URL
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL_BASE() {
        return URL_BASE;
    }

    public String getURL_END() {
        return URL_END;
    }

    public String getURL_TYPE() {
        return URL_TYPE;
    }

    public void setURL_TYPE(String URL_TYPE) {
        this.URL_TYPE = URL_TYPE;
    }

    public String getURL_PERIOD() {
        return URL_PERIOD;
    }

    public void setURL_PERIOD(String URL_PERIOD) {
        this.URL_PERIOD = URL_PERIOD;
    }
}
