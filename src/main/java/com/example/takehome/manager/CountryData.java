package com.example.takehome.manager;

import com.example.takehome.enums.Continent;
import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.model.CountryCodeAndName;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CountryData {

    private Map<String, List<CountryCodeAndName>> mapCountriesFromContinent = new HashMap<>();
    private Map<String, String> mapContinentFromCountryCode = new HashMap<>();
    private Map<String, String> mapCodeFromCountryName = new HashMap<>();

    private boolean isUpdatingData = false;

    public Set<String> getContinentsInCountries(Set<String> countriesCode) {
        Set<String> continentsSet = new HashSet<>();
        for(String countryCode: countriesCode) {
            String continent = mapContinentFromCountryCode.get(countryCode);
            continentsSet.add(continent);
        }
        return continentsSet;
    }

    public List<CountryCodeAndName> getCountriesInSameContinents(
        Set<String> continentsCodes,
        Set<String> countriesCode
    ) {
        List<CountryCodeAndName> countriesInSameContinent = new ArrayList<>();
        for(String continentCode: continentsCodes) {
            List<CountryCodeAndName> countriesFromContinent = mapCountriesFromContinent.get(continentCode);
            addCountries(countriesInSameContinent, countriesCode, countriesFromContinent);
        }
        return countriesInSameContinent;
    }

    public void checkIfCountriesDataIsLoaded() throws ApplicationException {
        if(!isCountriesDataLoaded()) {
            String errorMessage = "The countries data is not loaded. Try again later.";
            throw new ApplicationException(ErrorType.COUNTRY_DATA_NOT_LOADED, errorMessage);
        }
    }

    public boolean containsCountryCode(String countryCode) {
        return mapCodeFromCountryName.containsValue(countryCode);
    }

    public String getCountryCodeFromCountryName(String countryName) {
        return mapCodeFromCountryName.get(countryName);
    }

    public void setCountriesData(Map<Continent, List<CountryCodeAndName>> countriesInContinent) {
        isUpdatingData = true;
        resetCountriesData();
        for(Map.Entry<Continent, List<CountryCodeAndName>> entrySet: countriesInContinent.entrySet()) {
            setCountriesInContinent(entrySet.getKey(), entrySet.getValue());
        }
        isUpdatingData = false;
    }

    boolean isCountriesDataLoaded() {
        return !mapCountriesFromContinent.isEmpty()
            && !mapContinentFromCountryCode.isEmpty()
            && !mapCodeFromCountryName.isEmpty()
            && !isUpdatingData;
    }

    private void resetCountriesData() {
        mapCountriesFromContinent.clear();
        mapContinentFromCountryCode.clear();
        mapCodeFromCountryName.clear();
    }

    private void setCountriesInContinent(Continent continent, List<CountryCodeAndName> countryCodeAndNameList) {
        mapCountriesFromContinent.put(continent.name(), countryCodeAndNameList);
        for(CountryCodeAndName contryCodeAndName: countryCodeAndNameList) {
            String countryCode = contryCodeAndName.getCode().toUpperCase();
            String countryName = contryCodeAndName.getName().toUpperCase();
            mapContinentFromCountryCode.put(countryCode, continent.name());
            mapCodeFromCountryName.put(countryName, countryCode);
        }
    }

    private void addCountries(List<CountryCodeAndName> countriesInSameContinent, Set<String> countriesFromInput, List<CountryCodeAndName> countriesToAdd) {
        for(CountryCodeAndName countryCodeAndName: countriesToAdd) {
            boolean isCountryFromInput = countriesFromInput.contains(countryCodeAndName.getCode());
            if(!isCountryFromInput) {
                countriesInSameContinent.add(countryCodeAndName);
            }
        }
    }
}
