package com.example.takehome.manager;

import com.example.takehome.enums.ContinentEnum;
import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.model.Country;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CountryData {

    private Map<String, List<Country>> mapCountriesFromContinent = new HashMap<>();
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

    public List<Country> getCountriesInSameContinents(
        Set<String> continentsCodes,
        Set<String> countriesCode
    ) {
        List<Country> countriesInSameContinent = new ArrayList<>();
        for(String continentCode: continentsCodes) {
            List<Country> countriesFromContinent = mapCountriesFromContinent.get(continentCode);
            addCountries(countriesInSameContinent, countriesCode, countriesFromContinent);
        }
        return countriesInSameContinent;
    }

    public void checkIfCountriesDataIsLoaded() throws ApplicationException {
        if(isCountriesDataNotLoaded()) {
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

    public void setCountriesData(Map<ContinentEnum, List<Country>> countriesInContinents) {
        isUpdatingData = true;
        resetCountriesData();
        for(Map.Entry<ContinentEnum, List<Country>> entrySet: countriesInContinents.entrySet()) {
            setCountriesInContinent(entrySet.getKey(), entrySet.getValue());
        }
        isUpdatingData = false;
    }

    boolean isCountriesDataNotLoaded() {
        return (
            mapCountriesFromContinent.isEmpty() ||
            mapContinentFromCountryCode.isEmpty() ||
            mapCodeFromCountryName.isEmpty() ||
            isUpdatingData
        );
    }

    private void resetCountriesData() {
        mapCountriesFromContinent.clear();
        mapContinentFromCountryCode.clear();
        mapCodeFromCountryName.clear();
    }

    private void setCountriesInContinent(ContinentEnum continentEnum, List<Country> countryList) {
        mapCountriesFromContinent.put(continentEnum.name(), countryList);
        for(Country contryCodeAndName: countryList) {
            String countryCode = contryCodeAndName.getCode().toUpperCase();
            String countryName = contryCodeAndName.getName().toUpperCase();
            mapContinentFromCountryCode.put(countryCode, continentEnum.name());
            mapCodeFromCountryName.put(countryName, countryCode);
        }
    }

    private void addCountries(List<Country> countriesInSameContinent, Set<String> countriesFromInput, List<Country> countriesToAdd) {
        for(Country country : countriesToAdd) {
            boolean isCountryFromInput = countriesFromInput.contains(country.getCode());
            if(!isCountryFromInput) {
                countriesInSameContinent.add(country);
            }
        }
    }
}
