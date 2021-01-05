package com.example.covidtracker;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface PlaceholderAPI {

    //ten header to se mozna w cipsko wsadzic
    @Headers("X-Access-Token: 5cf9dfd5-3449-485e-b5ae-70a60e997864")
    @GET("/summary")
    Call<PlaceHolderSummary> getToZajebaneApi();

}