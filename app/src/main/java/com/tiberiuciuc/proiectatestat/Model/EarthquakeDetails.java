package com.tiberiuciuc.proiectatestat.Model;

public class EarthquakeDetails {

    StringBuilder builder;
    String htmlText;

    public EarthquakeDetails(StringBuilder builder, String htmlText) {
        this.builder = builder;
        this.htmlText = htmlText;
    }

    public EarthquakeDetails() {
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }
}
