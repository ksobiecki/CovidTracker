package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<String> continentsArray = Arrays.asList(new String[]{"Africa", "Asia", "Australia and Oceania", "Europe", "North America", "South America"});
    private List<String> africaCountries = new ArrayList<>();
    private List<String> asiaCountries = new ArrayList<>();
    private List<String> australiaCountries = new ArrayList<>();
    private List<String> europeCountries = new ArrayList<>();
    private List<String> nAmericaCountries = new ArrayList<>();
    private List<String> sAmericaCountries = new ArrayList<>();
    List<ContinentCountry> continentCountries;
    LinearLayout buttonPanel;
    TextView contentText;
    TextView header;
    Boolean isCountriesMenu = false;
    private ContinentCountryApi continentCountryApi = null;
    String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel);
        contentText = (TextView) findViewById(R.id.textContent);
        header = (TextView) findViewById(R.id.textView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.eu")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        continentCountryApi = retrofit.create(ContinentCountryApi.class);

        Call<List<ContinentCountry>> call = continentCountryApi.getContinentCountries();
        call.enqueue(new Callback<List<ContinentCountry>>() {
            @Override
            public void onResponse(Call<List<ContinentCountry>> call, Response<List<ContinentCountry>> response) {
                if (response.isSuccessful()) {
                    continentCountries = response.body();
                } else {
                    return;
                }
                fillContinentLists();
                generateButtons(continentsArray, buttonPanel);
            }

            @Override
            public void onFailure(Call<List<ContinentCountry>> call, Throwable t) {
                Log.e("Yo", "Errrorrrr!");
            }
        });




    }

    public void generateButtons(List<String> contentArray, LinearLayout buttonPanel) {
        buttonPanel.removeAllViews();

        for (String item : contentArray) {
            Button btn = new Button(this);
            btn.setText(item);
            btn.setBackground(getResources().getDrawable(R.drawable.customized_button));

            if (contentArray == continentsArray) {
                addContinentButtonsEvents(btn, btn.getText().toString());
            } else {
                addCountryButtonEvents(btn, btn.getText().toString());
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 160);
            params.setMargins(240, 10, 0, 10);
            buttonPanel.addView(btn, params);
        }
    }

    public void addContinentButtonsEvents(Button btn, String name) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinentClick(name);
                contentText.setText(name);
                isCountriesMenu = true;
            }
        });
    }

    public void addCountryButtonEvents(Button btn, String name) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCountryClick(name);
            }
        });
    }

    public void onCountryClick(String country) {
        Intent myIntent = new Intent(MainActivity.this, CountryMenuActivity.class);
        myIntent.putExtra("country", country);
        myIntent.putExtra("countryCode", ISO2WhereCountryName(country));
        startActivity(myIntent);
    }

    public void onContinentClick(String continent) {
        String continentName = continent.toLowerCase();
        switch (continentName) {
            case "africa":
                generateButtons(africaCountries, buttonPanel);
                break;
            case "asia":
                generateButtons(asiaCountries, buttonPanel);
                break;
            case "australia":
                generateButtons(australiaCountries, buttonPanel);
                break;
            case "europe":
                generateButtons(europeCountries, buttonPanel);
                break;
            case "north america":
                generateButtons(nAmericaCountries, buttonPanel);
                break;
            case "south america":
                generateButtons(sAmericaCountries, buttonPanel);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isCountriesMenu) {
            generateButtons(continentsArray, buttonPanel);
            contentText.setText("");
            isCountriesMenu = false;
        } else super.onBackPressed();
    }

    public void fillContinentLists(){
            for(ContinentCountry continentCountry:continentCountries){

                switch (continentCountry.getRegion()) {
                    case "Africa":
                        africaCountries.add(continentCountry.getName());
                        break;
                    case "Asia":
                        asiaCountries.add(continentCountry.getName());
                        break;
                    case "Oceania":
                        australiaCountries.add(continentCountry.getName());
                        break;
                    case "Europe":
                        europeCountries.add(continentCountry.getName());
                        break;
                    case "Americas":
                        if (continentCountry.getSubregion().equals("South America"))
                            sAmericaCountries.add(continentCountry.getName());
                        else nAmericaCountries.add(continentCountry.getName());
                        break;
                }
        }
    }

    public String ISO2WhereCountryName(String countryName){
        for(ContinentCountry cc : continentCountries){
            if(cc != null && cc.getName().equals(countryName))
                return cc.getAlpha2Code();
        }
        return null;
    }
}