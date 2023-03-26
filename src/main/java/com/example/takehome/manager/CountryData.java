package com.example.takehome.manager;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;

import java.util.*;

public class CountryData {

    private Map<String, Set<String>> mapCountriesInContinent = new HashMap<>();
    private Map<String, String> mapContinentOfCountryCode = new HashMap<>();
    private Map<String, String> mapCodeFromCountryName = new HashMap<>();

    public Set<String> getContinentsInCountries(Set<String> countriesCode) {
        Set<String> continentsSet = new HashSet<>();
        for(String countryCode: countriesCode) {
            String continent = mapContinentOfCountryCode.get(countryCode);
            continentsSet.add(continent);
        }
        return continentsSet;
    }

    public Set<String> getCountriesInSameContinents(
        Set<String> continentsCodes,
        Set<String> countriesCode
    ) {
        Set<String> countriesInSameContinent = new HashSet<>();
        for(String continentCode: continentsCodes) {
            Set<String> countriesInContinent = mapCountriesInContinent.get(continentCode);
            countriesInSameContinent.addAll(countriesInContinent);
        }
        countriesInSameContinent.removeAll(countriesCode);
        return countriesInSameContinent;
    }

    public void checkIfCountriesDataIsLoaded() throws ApplicationException {
        if(!isCountriesDataLoaded()) {
            String errorMessage = "The countries data is not loaded. Try again later.";
            throw new ApplicationException(ErrorType.COUNTRY_DATA_NOT_LOADED, errorMessage);
        }
    }

    public boolean containsCountryCode(String countryCode) {
        return mapCodeFromCountryName.containsKey(countryCode);
    }

    public String getCountryCodeFromCountryName(String countryName) {
        return mapCodeFromCountryName.get(countryName);
    }

    boolean isCountriesDataLoaded() {
        return !mapCountriesInContinent.isEmpty()
            && !mapContinentOfCountryCode.isEmpty()
            && !mapCodeFromCountryName.isEmpty();
    }
}
