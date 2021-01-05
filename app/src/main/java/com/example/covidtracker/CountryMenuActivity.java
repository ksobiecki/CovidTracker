package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryMenuActivity extends AppCompatActivity {

    private PlaceholderAPI placeholderAPI;
    List<CountryName> countrySpecifics;

    TextView country, cases, deaths, recovered, total_cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_menu);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        String countryName = getIntent().getStringExtra("country");
        country = (TextView) findViewById(R.id.country);
        cases = (TextView) findViewById(R.id.cases);
        deaths = (TextView) findViewById(R.id.deaths);
        recovered = (TextView) findViewById(R.id.recovered);
        total_cases = (TextView) findViewById(R.id.total_cases);
        country.setText(countryName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        placeholderAPI = retrofit.create(PlaceholderAPI.class);



        Call<List<CountryName>> call = placeholderAPI.get("/country/" + countryName);


        call.enqueue(new Callback<List<CountryName>>() {

            @Override
            public void onResponse(Call<List<CountryName>> call, Response<List<CountryName>> response) {
                if (response.isSuccessful()) {
                    countrySpecifics = response.body();
                    cases.setText("Active cases: " + countrySpecifics.get(countrySpecifics.size()-1).getActive());
                    deaths.setText("Deaths: " + countrySpecifics.get(countrySpecifics.size()-1).getDeaths());
                    recovered.setText("Recovered: " + countrySpecifics.get(countrySpecifics.size()-1).getRecovered());
                    total_cases.setText("Total cases: " + countrySpecifics.get(countrySpecifics.size()-1).getConfirmed());

                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<CountryName>> call, Throwable t) {
                Log.e("Yo", "Errror!");
            }

        });

    }
}