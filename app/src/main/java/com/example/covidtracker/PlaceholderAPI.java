package com.example.covidtracker;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlaceholderAPI {

    @GET("/summary")
    Call<PlaceHolderSummary> getCountry();

}