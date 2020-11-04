package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String [] continentsArray = new String[] {"Africa", "Asia", "Australia", "Europe", "North America", "South America"};
    private String [] africaCountries = new String [] {"Chad", "Egypt", "Sudan"};
    private String [] asiaCountries = new String [] {"China", "Vietnam", "Korea", "India"};
    private String [] australiaCountries = new String [] {"Australia", "Malaysia"};
    private String [] europeCountries = new String [] {"Germany", "France", "Poland", "Spain", "Hungary", "Greece", "Belarus", "Bulgaria", "Slovakia", "Ukraine", "Belgium", "Switzerland", "Montenegro", "Czech Republic"};
    private String [] nAmericaCountries = new String [] {"USA", "Canada", "Mexico"};
    private String [] sAmericaCountries = new String [] {"Chile", "Brazil", "Argentina"};

    LinearLayout buttonPanel;
    TextView contentText;
    Boolean isCountriesMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch(NullPointerException e){}

        buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel) ;
        contentText = (TextView) findViewById(R.id.textContent);

        generateButtons(continentsArray, buttonPanel);
    }

    public void generateButtons (String[] contentArray, LinearLayout buttonPanel) {
        buttonPanel.removeAllViews();

        for (String item: contentArray) {
            Button btn = new Button(this);
            btn.setText(item);

            if (contentArray == continentsArray) {
                addContinentButtonsEvents(btn, btn.getText().toString());
            }
            else{
                addCountryButtonEvents(btn,btn.getText().toString());
            }


            buttonPanel.addView(btn);
        }
    }

    public void addContinentButtonsEvents (Button btn, String name) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinentClick(name);
                contentText.setText(name);
                isCountriesMenu = true;
            }
        });
    }

    public void addCountryButtonEvents (Button btn, String name){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCountryClick(name);
            }
        });
    }

    public void onCountryClick (String country){
        Intent myIntent = new Intent(getApplicationContext(), CountryMenuActivity.class);
        myIntent.putExtra("country",country);
        startActivity(myIntent);
    }

    public void onContinentClick (String continent) {
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
    public void onBackPressed()
    {
        if (isCountriesMenu) {
            generateButtons(continentsArray, buttonPanel);
            contentText.setText("");
            isCountriesMenu = false;
        }
        else super.onBackPressed();
    }

}