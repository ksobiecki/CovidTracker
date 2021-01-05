package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryMenuActivity extends AppCompatActivity {

    private PlaceholderAPI placeholderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_menu);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        String countryName = getIntent().getStringExtra("country");
        TextView country = (TextView) findViewById(R.id.country);
        country.setText(countryName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        placeholderAPI = retrofit.create(PlaceholderAPI.class);

        Call<PlaceHolderSummary> call = placeholderAPI.getCountry();


        call.enqueue(new Callback<PlaceHolderSummary>() {

            @Override
            public void onResponse(Call<PlaceHolderSummary> call, Response<PlaceHolderSummary> response) {
                if (response.isSuccessful()) {
                    PlaceHolderSummary posts = response.body();
                } else {

                    return;
                }
            }

            @Override
            public void onFailure(Call<PlaceHolderSummary> call, Throwable t) {
                Log.e("Yo", "Errror!");
            }

        });

    }
}