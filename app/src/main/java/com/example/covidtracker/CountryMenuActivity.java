package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryMenuActivity extends AppCompatActivity {

    static final String API_SOURCE = "https://api.covid19api.com";
    static final String CLASS_TAG = "CountryMenuActivity";
    private PlaceholderAPI placeholderAPI;
    private ToggleButton favouriteButton;
    List<CountryName> countrySpecifics = null;
    List<CountryShort> countryShortList;
    List<String> favouriteCountries;

    TextView country, cases, deaths, recovered, total_cases, date;

    ViewDialog alert;

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
        favouriteCountries = getIntent().getStringArrayListExtra("favouriteCountries");
        country = (TextView) findViewById(R.id.country);
        cases = (TextView) findViewById(R.id.casesNumber);
        deaths = (TextView) findViewById(R.id.deathsNumber);
        recovered = (TextView) findViewById(R.id.recoveredNumber);
        total_cases = (TextView) findViewById(R.id.totalCasesNumber);
        date = (TextView) findViewById(R.id.dateNumber);
        country.setText(countryName);
        favouriteButton = (ToggleButton) findViewById(R.id.toggleButton);

        alert = new ViewDialog();

        initialToggleButtonCheck(countryName);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(API_SOURCE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        placeholderAPI = retrofit2.create(PlaceholderAPI.class);

        Call<List<CountryShort>> callShort = placeholderAPI.getListShortName("/countries");
        callShort.enqueue(new Callback<List<CountryShort>>() {
            @Override
            public void onResponse(Call<List<CountryShort>> callShort, Response<List<CountryShort>> response) {
                Log.i(getString(R.string.CONN_TAG), "Successful connection: " + API_SOURCE + "/countries");
                countryShortList = response.body();

                String countryName = getCountryShortNameWhereISO2(ISO2);

                Call<List<CountryName>> call = placeholderAPI.getListCountryName("/country/" + getCountryShortSlugFromCountryName(countryName));

                call.enqueue(new Callback<List<CountryName>>() {
                    @Override
                    public void onResponse(Call<List<CountryName>> call, Response<List<CountryName>> response) {
                        if (response.isSuccessful()) {
                            countrySpecifics = response.body();
                            if(countrySpecifics.size() != 0) {
                                cases.setText(countrySpecifics.get(countrySpecifics.size() - 1).getActive());
                                deaths.setText(countrySpecifics.get(countrySpecifics.size() - 1).getDeaths());
                                recovered.setText(countrySpecifics.get(countrySpecifics.size() - 1).getRecovered());
                                total_cases.setText(countrySpecifics.get(countrySpecifics.size() - 1).getConfirmed());
                                date.setText(countrySpecifics.get(countrySpecifics.size()-1).getDate().substring(0,10));
                                Log.i(getString(R.string.CONN_TAG), "Successful response: " + API_SOURCE + "/country/" + countryName);
                            } else {
                                //to bedzie mozna wywalić
                                cases.setText("No data");
                                deaths.setText("No data");
                                recovered.setText("No data");
                                total_cases.setText(" No data");
                                date.setText("No data");
                                alert.showDialog(CountryMenuActivity.this, "There is no available data", "no_data");
                                Log.i(getString(R.string.CONN_TAG), "Response is empty: " + API_SOURCE + "/country/" + countryName);

                            }

                        } else {
                            alert.showDialog(CountryMenuActivity.this, "Couldn't load country details", "connection_error");
                            Log.e(getString(R.string.CONN_TAG), "Response is not successful: " + API_SOURCE + "/country/" + countryName);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryName>> call, Throwable t) {
                        alert.showDialog(CountryMenuActivity.this, "Connection failed", "connection_error");
                        Log.e(getString(R.string.CONN_TAG), "failed to connect: " + API_SOURCE + "/country/" + countryName);
                    }

                });
            }

            @Override
            public void onFailure(Call<List<CountryShort>> callShort, Throwable t) {
                alert.showDialog(CountryMenuActivity.this, "Connection failed", "connection_error");
                Log.e(getString(R.string.CONN_TAG), "failed to connect: " + API_SOURCE + "/countries");
            }
        });

        setFavouriteButtonEvents(countryName);
    }

    public void initialToggleButtonCheck(String countryName){
        for(String country: FavouriteCountriesIO.getFavouriteCountries()){
            Log.d("Initial Toggle: ", "country from list " + country + "countryName " + countryName);
            if(country.equals(countryName)){
                favouriteButton.setChecked(true);
            }
        }
    }

    public void setFavouriteButtonEvents(String countryName){
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favouriteButton.isChecked()){
                    FavouriteCountriesIO.addToList(countryName);
                    Log.d("Toggle: ", "ON - added" + FavouriteCountriesIO.getFavouriteCountries().toString());
                } else {
                    FavouriteCountriesIO.removeFromList(countryName);
                    Log.d("Toggle: ", "Off - removed" );
                }
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
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.e(CLASS_TAG, methodName + "returned null");
        return null;
    }

    public String getCountryShortSlugFromCountryName(String countryName){
        for(CountryShort cs : countryShortList){
            if(cs != null && cs.getCountry().equals(countryName)){
                return cs.getSlug();
            }
        }
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        Log.e(CLASS_TAG, methodName + "returned null");
        return null;
    }
}