package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private String [] continentsArray = new String[] {"Africa", "Asia", "Australia", "Europe", "North America", "South America"};
    private String [] africaCountries = new String [] {"Chad", "Egipt", "Sudan"};
    private String [] asiaCountries = new String [] {"China", "Vietnam", "Korea", "India"};
    private String [] australiaCountries = new String [] {"Australia", "Malysia"};
    private String [] europeCountries = new String [] {"Germany", "France", "Poland", "Spain", "Hungary", "Greece", "Belarus", "Bulgaria", "Slovakia", "Ukraine", "Belgium", "Switzerland", "Montenegro", "Czech Republic"};
    private String [] nAmericaCountries = new String [] {"USA", "Canada", "Mexico"};
    private String [] sAmericaCountries = new String [] {"Chile", "Brasil", "Argentina"};

    LinearLayout buttonPanel;
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


        //ScrollView scrollArea = (ScrollView) findViewById(R.id.scrollArea);
        buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel) ;

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

            buttonPanel.addView(btn);
        }
    }

    public void addContinentButtonsEvents (Button btn, String name) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinentClick(name);
                isCountriesMenu = true;
            }
        });
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
            isCountriesMenu = false;
        }
        else super.onBackPressed();
    }

}