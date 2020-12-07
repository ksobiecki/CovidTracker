package com.example.covidtracker;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface PlaceholderAPI {


    @GET("dayone/country/south-africa/status/confirmed")
    Call<List> getPosts();

}