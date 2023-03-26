package com.example.takehome.service;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.manager.CountryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    CountryData countryData;

    @Autowired
    public CountryService(CountryData countryData) {
        this.countryData = countryData;
    }

    public List<String> getCountriesInSameContinent(List<String> inputCountries) throws ApplicationException {
        countryData.checkIfCountriesDataIsLoaded();
        // TODO
    }
}
