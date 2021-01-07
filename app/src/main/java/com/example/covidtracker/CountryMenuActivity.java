package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryMenuActivity extends AppCompatActivity {

    private PlaceholderAPI placeholderAPI;
    List<CountryName> countrySpecifics = null;
    List<CountryShort> countryShortList;

    TextView country, cases, deaths, recovered, total_cases, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_menu);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        String countryName = getIntent().getStringExtra("country");
        String ISO2 = getIntent().getStringExtra("countryCode");
        country = (TextView) findViewById(R.id.country);
        cases = (TextView) findViewById(R.id.cases);
        deaths = (TextView) findViewById(R.id.deaths);
        recovered = (TextView) findViewById(R.id.recovered);
        total_cases = (TextView) findViewById(R.id.total_cases);
        date = (TextView) findViewById(R.id.date);
        country.setText(countryName);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        placeholderAPI = retrofit2.create(PlaceholderAPI.class);

        Log.i("CountryNameCheck:",countryName);
        Log.i("CountryCodeCheck:",ISO2);
        Call<List<CountryShort>> callShort = placeholderAPI.getListShortName("/countries");
        callShort.enqueue(new Callback<List<CountryShort>>() {
            @Override
            public void onResponse(Call<List<CountryShort>> callShort, Response<List<CountryShort>> response) {
                Log.i("onResponse", "Successful short countries");
                countryShortList = response.body();

                String countryName = getCountryShortNameWhereISO2(ISO2);

                Log.i("Sprawdzonko", "ISO2: " + ISO2 + " , CountryName: " + getCountryShortSlugFromCountryName(countryName));

                Call<List<CountryName>> call = placeholderAPI.getListCountryName("/country/" + getCountryShortSlugFromCountryName(countryName));


                call.enqueue(new Callback<List<CountryName>>() {
                    @Override
                    public void onResponse(Call<List<CountryName>> call, Response<List<CountryName>> response) {
                        if (response.isSuccessful()) {
                            countrySpecifics = response.body();
                            if(countrySpecifics.size() != 0) {
                                cases.setText("Active cases: " + countrySpecifics.get(countrySpecifics.size() - 1).getActive());
                                deaths.setText("Deaths: " + countrySpecifics.get(countrySpecifics.size() - 1).getDeaths());
                                recovered.setText("Recovered: " + countrySpecifics.get(countrySpecifics.size() - 1).getRecovered());
                                total_cases.setText("Total cases: " + countrySpecifics.get(countrySpecifics.size() - 1).getConfirmed());
                                date.setText("Last update date: " + countrySpecifics.get(countrySpecifics.size()-1).getDate().substring(0,10));
                                Log.i("covid:", "It works");
                            } else {
                                cases.setText("Active cases: No data");
                                deaths.setText("Deaths: No data");
                                recovered.setText("Recovered: No data");
                                total_cases.setText("Total cases: No data");
                                date.setText("Last update date: No data");;
                            }

                        } else {
                            Log.e("86 Check", "/country/" + countryName);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryName>> call, Throwable t) {
                        Log.e("Yo", "Errror!");
                    }

                });
            }

            @Override
            public void onFailure(Call<List<CountryShort>> callShort, Throwable t) {
                Log.e("onFailure", "Errror!");
            }
        });

    }

    //zwraca ISO2 po podaniu nazwy
    public String getCountryShortNameWhereISO2(String ISO2){
        for(CountryShort cs : countryShortList){
            if(cs != null && cs.getISO2().equals(ISO2)){
                return cs.getCountry();
            }
        }
        return null;
    }

    public String getCountryShortSlugFromCountryName(String countryName){
        for(CountryShort cs : countryShortList){
            if(cs != null && cs.getCountry().equals(countryName)){
                return cs.getSlug();
            }
        }
        return null;
    }

    /*public String countryNameByISO2(List<CountryName> countries, String ISO2){
        for(CountryName cs : countries){
            if(cs != null && cs.ge().equals(countryName)){
                return cs.getISO2();
            }
        }
    }*/
}