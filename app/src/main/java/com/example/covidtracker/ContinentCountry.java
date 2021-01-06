package com.example.covidtracker;

import com.example.covidtracker.continentCountry.Language;
import com.example.covidtracker.continentCountry.Translation;
import com.example.covidtracker.continentCountry.currency;

import java.util.List;

public class ContinentCountry {
    private String name;
    private List<String> topLevelDomain;
    private String alpha2Code;
    private String alpha3Code;
    private List<String> callingCodes;
    private String capital;
    private List<String> altSpellings;
    private String region;
    private String subregion;
    private String population;
    private List<Double> latlng;
    private String demonym;
    private Double area;
    private Double gini;
    private List<String> timezones;
    private List<String> borders;
    private String nativeName;
    private String numericCode;
    private List<currency> currencies;
    private List<Language> languages;
    private Translation translations;
    private String flag;
    private String cioc;

    public String getName() {
        return name;
    }


    public String getAlpha2Code() {
        return alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }


    public String getCapital() {
        return capital;
    }

    public List<String> getTopLevelDomain() {
        return topLevelDomain;
    }

    public List<String> getCallingCodes() {
        return callingCodes;
    }

    public List<String> getAltSpellings() {
        return altSpellings;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getPopulation() {
        return population;
    }

    public List<Double> getLatlng() {
        return latlng;
    }

    public String getDemonym() {
        return demonym;
    }

    public Double getArea() {
        return area;
    }

    public Double getGini() {
        return gini;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public List<String> getBorders() {
        return borders;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public List<currency> getCurrencies() {
        return currencies;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Translation getTranslations() {
        return translations;
    }

    public String getFlag() {
        return flag;
    }

    public String getCioc() {
        return cioc;
    }
}
