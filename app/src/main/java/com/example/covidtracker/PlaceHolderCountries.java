package com.example.covidtracker;

import java.util.Date;

public class PlaceHolderPost {
    private String countryName;
    private String countryCode;
    private String slug;
    private Integer newConfirmed;
    private Integer totalConfirmed;
    private Integer newDeaths;

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getSlug() {
        return slug;
    }

    public Integer getNewConfirmed() {
        return newConfirmed;
    }

    public Integer getTotalConfirmed() {
        return totalConfirmed;
    }

    public Integer getNewDeaths() {
        return newDeaths;
    }

    public Integer getTotalDeaths() {
        return totalDeaths;
    }

    public Integer getNewRecovered() {
        return newRecovered;
    }

    public Integer getTotalRecovered() {
        return totalRecovered;
    }

    public String getDate() {
        return date;
    }

    private Integer totalDeaths;
    private Integer newRecovered;
    private Integer totalRecovered;
    private String date;

}

