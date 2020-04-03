package com.tiberiuciuc.proiectatestat.Util;

import android.content.SharedPreferences;

import java.net.ContentHandler;

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


    private String pastMonth_ALL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
    private String pastMonth_SIGNIFICANT = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
    private String pastMonth_M4_5PLUS = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_month.geojson";
    private String pastWeek_ALL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    private String pastWeek_SIGNIFICANT = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson";
    private String pastWeek_M4_5PLUS = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";
    private String pastDay_ALL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    private String pastDay_SIGNIFICANT = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.geojson";
    private String pastHour_ALL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    private int MAP_TYPE = 4;
    private int MAP_TYPE_PREPARE_CHANGES;

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

    public void prepareMapTypeChanges(int MAP_TYPE_PREPARE_CHANGES){
        this.MAP_TYPE_PREPARE_CHANGES = MAP_TYPE_PREPARE_CHANGES;
    }

    public int getMAP_TYPE_PREPARE_CHANGES() {
        return MAP_TYPE_PREPARE_CHANGES;
    }

    //URL
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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
