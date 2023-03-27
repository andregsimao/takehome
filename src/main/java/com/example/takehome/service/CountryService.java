package com.example.takehome.service;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.manager.CountryData;
import com.example.takehome.model.CountryCodeAndName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CountryService {

    private CountryData countryData;

    @Autowired
    public CountryService(CountryData countryData) {
        this.countryData = countryData;
    }

    public List<CountryCodeAndName> getCountriesInSameContinent(List<String> inputCountries) throws ApplicationException {
        countryData.checkIfCountriesDataIsLoaded();
        Set<String> countriesCode = getCountriesCode(inputCountries);
        Set<String> continentsCodes = countryData.getContinentsInCountries(countriesCode);
        return countryData.getCountriesInSameContinents(continentsCodes, countriesCode);
    }

    private Set<String> getCountriesCode(List<String> inputCountries) throws ApplicationException {
        Set<String> countriesCode = new HashSet<>();
        for(String inputCountry: inputCountries) {
            String countryCode = getCountryCode(inputCountry);
            countriesCode.add(countryCode);
        }
        return countriesCode;
    }

    private String getCountryCode(String inputCountry) throws ApplicationException {
        String inputCountryUpperCase = inputCountry.toUpperCase();
        if(countryData.containsCountryCode(inputCountryUpperCase)) {
            return inputCountryUpperCase;
        }
        String countryCode = countryData.getCountryCodeFromCountryName(inputCountryUpperCase);
        if(countryCode == null) {
            String errorMessage = "Country " + inputCountry + " is not a valid input";
            throw new ApplicationException(ErrorType.INVALID_COUNTRY_INPUT, errorMessage);
        }
        return countryCode;
    }
}
