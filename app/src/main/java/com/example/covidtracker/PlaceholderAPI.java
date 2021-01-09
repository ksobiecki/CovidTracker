package com.example.covidtracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlaceholderAPI {

    @GET("/summary")
    Call<PlaceHolderSummary> getSummary();

    @GET("{fullUrl}")
    Call<List<CountryName>> getListCountryName(@Path(value = "fullUrl", encoded = true) String fullUrl);

    @GET("{full}")
    Call<List<CountryShort>> getListShortName(@Path(value = "full", encoded = true) String full);

}