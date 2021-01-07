package com.example.covidtracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ContinentCountryApi {

    //@GET("/rest/v2/all?fields=name;region")
    @GET("{fullUrl}")
    Call<List<ContinentCountry>> getContinentCountries(@Path(value = "fullUrl", encoded = true) String fullUrl);
}
