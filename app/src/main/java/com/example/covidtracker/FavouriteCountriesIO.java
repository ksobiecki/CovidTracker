package com.example.covidtracker;

import android.app.AppComponentFactory;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FavouriteCountriesIO {

    static public List<String> countriesList = new ArrayList<>();


    static public void addToList(String countryName){
        countriesList.add(countryName);
    }
    static public void removeFromList(String countryName){
        countriesList.remove(countryName);
    }

    static public void loadList(Context context){
        String string = "";
        countriesList.clear();
        try {
            FileInputStream fis = context.openFileInput("file.txt");
            Scanner scanner = new Scanner(fis);
            while(scanner.hasNext())
                countriesList.add(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void saveList(Context context){
        try {
            File file = new File("file.txt");
            FileOutputStream fos = context.openFileOutput("file.txt", Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(fos);
            for(String country: countriesList){
                pw.println(country);
            Log.d("YEAAAAAH ", file.getAbsolutePath() + country);}
            pw.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public List<String> getFavouriteCountries(){

        return countriesList;
    }

}
