package com.example.covidtracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ContinentCountryApi {

    @GET("/rest/v2/all?fields=name;region")
    //@GET("/rest/v2/all")
    Call<List<ContinentCountry>> getContinentCountries();
}
