package com.example.takehome.service;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.manager.CountryData;
import com.example.takehome.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CountryService {

    private CountryData countryData;

    @Autowired
    public CountryService(CountryData countryData) {
        this.countryData = countryData;
    }

    public List<Country> getCountriesInSameContinent(List<String> inputCountries) throws ApplicationException {
        checkForEmptyInput(inputCountries);
        countryData.checkIfCountriesDataIsLoaded();
        Set<String> countriesCode = getCountriesCode(inputCountries);
        Set<String> continentsCodes = countryData.getContinentsInCountries(countriesCode);
        return countryData.getCountriesInSameContinents(continentsCodes, countriesCode);
    }

    private void checkForEmptyInput(List<String> countries) throws ApplicationException {
        if(countries.isEmpty()) {
            String exceptionMessage = "Invalid input data";
            throw new ApplicationException(ErrorType.INVALID_COUNTRY_INPUT, exceptionMessage);
        }
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
        log.info(
            "[CountryService:getCountryCode] Country input " + inputCountry +
                " is not the country code, trying to find country by name: "
        );
        String countryCode = countryData.getCountryCodeFromCountryName(inputCountryUpperCase);
        if(countryCode == null) {
            String errorMessage = "Country " + inputCountry + " is not a valid input";
            throw new ApplicationException(ErrorType.INVALID_COUNTRY_INPUT, errorMessage);
        }
        log.info(
            "[CountryService:getCountryCode] Country code " + countryCode + " found from input " + inputCountry +
                " is not the country code, trying to find country by name: "
        );
        return countryCode;
    }
}
