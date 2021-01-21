package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.Serializable;
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
    private  List<String> favouriteCountries =new ArrayList<>();

    int width = 0;
    String currentContinent = null;
    List<ContinentCountry> continentCountries;
    LinearLayout buttonPanel;
    RelativeLayout header;
    TextView contentText;
    Boolean isCountriesMenu = false;
    Boolean isFavourite = false;
    SearchView search;
    Button favButton;
    private ContinentCountryApi continentCountryApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        //FavouriteCountriesIO.saveList(getApplicationContext(), "Poland");

        FavouriteCountriesIO.loadList(getApplicationContext());
        //favouriteCountries = FavouriteCountriesIO.getFavouriteCountries();
        Log.d("Pls ", FavouriteCountriesIO.getFavouriteCountries().toString());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel);
        contentText = (TextView) findViewById(R.id.textContent);
        search = (SearchView) findViewById(R.id.searchArea);
        header = (RelativeLayout) findViewById(R.id.relativeLayout);
        favButton = (Button) findViewById(R.id.favourite_btn);

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
                setFavButtonListeners();
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
                }
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
        for (String item : contentArray) {
            Button btn = new Button(this);
            btn.setText(item);
            btn.setTextColor(Color.parseColor("#c0c0c0"));
            btn.setBackground(getResources().getDrawable(R.drawable.customized_button_normal));

            if (contentArray == continentsArray) {
                addContinentButtonsEvents(btn, btn.getText().toString());
            } else {
                addCountryButtonEvents(btn, btn.getText().toString());

            }
            Log.d("Width", "generateButtons: Width" + width);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width-120, 160);
            params.setMargins(30, 10, 30, 10);
            buttonPanel.addView(btn, params);
        }
    }

    public void changeHeaderLayout(){
        ImageView logo = (ImageView) findViewById(R.id.imageView2);
        TextView appName = (TextView) findViewById(R.id.CovidTracker);
        TextView textContent = (TextView) findViewById(R.id.textContent);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) appName.getLayoutParams();
        if(isCountriesMenu){
            params.addRule(RelativeLayout.CENTER_VERTICAL,0);
            logo.setVisibility(View.GONE);
            textContent.setVisibility(View.VISIBLE);
            textContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
        } else {
            params.addRule(RelativeLayout.CENTER_VERTICAL,1);
            logo.setVisibility(View.VISIBLE);
            textContent.setVisibility(View.GONE);
            textContent.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
        }
    }
    public void filterButtons(){
        buttonPanel.removeAllViews();
        for(String item:filteredCountries){
            Button btn = new Button(this);
            btn.setText(item);
            btn.setBackground(getResources().getDrawable(R.drawable.customized_button_normal));
            btn.setTextColor(Color.parseColor("#c0c0c0"));
            addCountryButtonEvents(btn, btn.getText().toString());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width-120, 160);
            params.setMargins(30, 10, 30, 10);
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
        myIntent.putExtra("favouriteCountries", (Serializable) favouriteCountries);
        startActivity(myIntent);
    }

    public void onContinentClick(String continent) {
        isCountriesMenu = true;
        changeHeaderLayout();
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
        if(isFavourite)
            onFavouriteClick();
        else if (isCountriesMenu) {
            generateButtons(continentsArray);
            contentText.setText("");
            isCountriesMenu = false;
            changeHeaderLayout();
            FavouriteCountriesIO.saveList(getApplicationContext());
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

    public void setFavButtonListeners(){
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavouriteClick();
            }
        });
    }

    public void onFavouriteClick(){
        buttonPanel.removeAllViews();
        if(!isFavourite){
            favButton.setText("BACK");
            isFavourite=true;
            for(String item:FavouriteCountriesIO.getFavouriteCountries()){
                Button btn = new Button(this);
                btn.setText(item);
                btn.setBackground(getResources().getDrawable(R.drawable.customized_button_normal));
                btn.setTextColor(Color.parseColor("#c0c0c0"));
                addCountryButtonEvents(btn, btn.getText().toString());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width-120, 160);
                params.setMargins(30, 10, 30, 10);
                buttonPanel.addView(btn, params);
            }
        } else {
            favButton.setText("FAVOURITE");
            isFavourite=false;
            generateButtons(continentsArray);
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("OnDestroy: ",  FavouriteCountriesIO.getFavouriteCountries().toString());
        FavouriteCountriesIO.saveList(getApplicationContext());
    }
    public String ISO2WhereCountryName(String countryName){
        for(ContinentCountry cc : continentCountries){
            if(cc != null && cc.getName().equals(countryName))
                return cc.getAlpha2Code();
        }
        return null;
    }
}