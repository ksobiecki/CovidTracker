package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CountryMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_menu);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch(NullPointerException e){}
        String countryName = getIntent().getStringExtra("country");
        TextView country = (TextView) findViewById(R.id.country);
        country.setText(countryName);
    }
}