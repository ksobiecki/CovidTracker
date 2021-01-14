package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

    static final String API_SOURCE = "https://restcountries.eu";

    private List<String> continentsArray = Arrays.asList(new String[]{"Africa", "Asia", "Australia and Oceania", "Europe", "North America", "South America", "Other"});
    private List<String> africaCountries = new ArrayList<>();
    private List<String> asiaCountries = new ArrayList<>();
    private List<String> australiaCountries = new ArrayList<>();
    private List<String> europeCountries = new ArrayList<>();
    private List<String> nAmericaCountries = new ArrayList<>();
    private List<String> sAmericaCountries = new ArrayList<>();
    private List<String> otherCountries = new ArrayList<>();
    private List<String> filteredCountries = new ArrayList<>();
    String currentContinent = null;
    List<ContinentCountry> continentCountries;
    LinearLayout buttonPanel;
    TextView contentText;
    TextView header;
    Boolean isCountriesMenu = false;
    SearchView search;
    private ContinentCountryApi continentCountryApi = null;

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
        search = (SearchView) findViewById(R.id.searchArea);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_SOURCE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        continentCountryApi = retrofit.create(ContinentCountryApi.class);

        Call<List<ContinentCountry>> call = continentCountryApi.getContinentCountries("/rest/v2/all");
        call.enqueue(new Callback<List<ContinentCountry>>() {
            @Override
            public void onResponse(Call<List<ContinentCountry>> call, Response<List<ContinentCountry>> response) {
                if (response.isSuccessful()) {
                    continentCountries = response.body();
                } else {
                    Log.e(getString(R.string.CONN_TAG), "Response is not successful: " + API_SOURCE + "/rest/v2/all");
                    return;
                }
                fillContinentLists();
                generateButtons(continentsArray);
            }

            @Override
            public void onFailure(Call<List<ContinentCountry>> call, Throwable t) {
                Log.e(getString(R.string.CONN_TAG), "Failed to connect: " + API_SOURCE + "/rest/v2/all");
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!(query.isEmpty()||query==""||query==null)) {
                    for (ContinentCountry country : continentCountries) {
                        if (country.getName().toLowerCase().contains(query) && country.getName().toLowerCase().indexOf(query) == 0) {
                            Log.d("Trąba", "onQueryTextSubmit: ");
                            filteredCountries.add(country.getName());
                        }
                    }
                    Log.d("Filter", "onQueryTextSubmit: "+ query);
                    filterButtons();
                }  /**/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    if(isCountriesMenu){
                        chooseContinentCountries(currentContinent);
                    } else generateButtons(continentsArray);
                }
                return true;
            }
        });
    }

    public void generateButtons(List<String> contentArray) {
        buttonPanel.removeAllViews();
        Log.d("Trąbla", "onQueryTextSubmit: ");
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

    public void filterButtons(){
        buttonPanel.removeAllViews();
        for(String item:filteredCountries){
            Button btn = new Button(this);
            btn.setText(item);
            btn.setBackground(getResources().getDrawable(R.drawable.customized_button));
            addCountryButtonEvents(btn, btn.getText().toString());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(600, 160);
            params.setMargins(240, 10, 0, 10);
            buttonPanel.addView(btn, params);
        }
        filteredCountries.clear();
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
        chooseContinentCountries(continent);
    }
    public void chooseContinentCountries(String continent){
        String continentName = continent.toLowerCase();
        currentContinent=continent;
        switch (continentName) {
            case "africa":
                generateButtons(africaCountries);
                break;
            case "asia":
                generateButtons(asiaCountries);
                break;
            case "australia and oceania":
                generateButtons(australiaCountries);
                break;
            case "europe":
                generateButtons(europeCountries);
                break;
            case "north america":
                generateButtons(nAmericaCountries);
                break;
            case "south america":
                generateButtons(sAmericaCountries);
                break;
            default:
                generateButtons(otherCountries);
        }
    }

    @Override
    public void onBackPressed() {
        if (isCountriesMenu) {
            generateButtons(continentsArray);
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
                        Log.d("Australia", "fillContinentLists: "+continentCountry.getName());
                        break;
                    case "Europe":
                        europeCountries.add(continentCountry.getName());
                        break;
                    case "Americas":
                        if (continentCountry.getSubregion().equals("South America"))
                            sAmericaCountries.add(continentCountry.getName());
                        else nAmericaCountries.add(continentCountry.getName());
                        break;
                    default:
                        otherCountries.add(continentCountry.getName());
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