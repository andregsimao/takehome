package com.example.takehome.manager;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;

import java.util.*;

public class CountryData {

    private Map<String, List<String>> mapCountriesInContinent = new HashMap<>();
    private Map<String, String> mapContinentOfCountryCode = new HashMap<>();
    private Map<String, String> mapCodeFromCountryName = new HashMap<>();

    public Set<String> getContinentsInCountries(Set<String> countriesCode) throws ApplicationException {
        Set<String> continentsSet = new HashSet<>();
        for(String countryCode: countriesCode) {
            String continent = mapContinentOfCountryCode.get(countryCode);
            continentsSet.add(continent);
        }
        return continentsSet;
    }

    public List<String> getCountriesInSameContinents(
        Set<String> continentsCodes,
        Set<String> countriesCode
    ) {

    }

    public void checkIfCountriesDataIsLoaded() throws ApplicationException {
        if(!isCountriesDataLoaded()) {
            String errorMessage = "The countries data is not loaded. Try again later.";
            throw new ApplicationException(ErrorType.COUNTRY_DATA_NOT_LOADED, errorMessage);
        }
    }

    boolean isCountriesDataLoaded() {
        return !mapCountriesInContinent.isEmpty()
            && !mapContinentOfCountryCode.isEmpty()
            && !mapCodeFromCountryName.isEmpty();
    }

    private String getCountryCodeFromCountryName(String countryName) {
        return mapCodeFromCountryName.get(countryName);
    }
}
