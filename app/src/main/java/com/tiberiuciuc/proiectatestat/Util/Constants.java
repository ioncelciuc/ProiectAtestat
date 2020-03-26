package com.tiberiuciuc.proiectatestat.Util;

public class Constants {
    public static final String URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson";
    public static final int LIMIT = 30; //maxim 30 markere pe harta

    //significant 30 days:
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_month.geojson
    //M4.5+ 30 days:
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_month.geojson
    //All earthquakes 30 days(o sa crape, sunt multe):
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson

    //significant past week:
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_week.geojson
    //M4.5+ past week:
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson
    //All earthquakes past week(posibil sa crape?):
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson

    //significant today (poate fi gol):
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.geojson
    //all today:
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson

    //All earthquakes past hour:
    //https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson
}
