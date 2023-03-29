package com.example.takehome.controller;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.model.CountriesRequest;
import com.example.takehome.model.CountriesResponse;
import com.example.takehome.model.Country;
import com.example.takehome.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${takehome.mapping.api.signature}")
public class CountryController {

    CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(
        value = "countries/same-continent",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CountriesResponse> getCountriesInSameContinent(@RequestBody CountriesRequest countriesRequest) {
        List<String> inputCountries = new ArrayList<>();
        try {
            inputCountries = countriesRequest.getCountries();
            log.info("[CountryController:getCountriesInSameContinent] Receiving request for input countries: " + inputCountries);
            List<Country> countriesInSameContinent = countryService.getCountriesInSameContinent(inputCountries);
            CountriesResponse countriesInSameContinentResponse = CountriesResponse
                .buildCountriesListResponse(inputCountries, countriesInSameContinent);
            log.info("[CountryController:getCountriesInSameContinent] Success in getting output countries: " + countriesInSameContinentResponse);
            return new ResponseEntity<>(countriesInSameContinentResponse, HttpStatus.OK);
        } catch (ApplicationException e) {
            CountriesResponse errorResponse = CountriesResponse
                .buildCountriesErrorResponse(inputCountries, e.getMessage());
            log.error("[CountryController:getCountriesInSameContinent] Bad request in getting output countries: " + errorResponse);
            HttpStatus httpStatus = getHttpStatusFromException(e.getErrorType());
            return new ResponseEntity<>(errorResponse, httpStatus);
        } catch (Throwable e) {
            CountriesResponse errorResponse = CountriesResponse
                    .buildCountriesErrorResponse(inputCountries, e.getMessage());
            log.error("[CountryController:getCountriesInSameContinent] Internal Server Error in getting output countries: " + errorResponse);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpStatus getHttpStatusFromException(ErrorType errorType) {
        if(errorType == ErrorType.COUNTRY_DATA_NOT_LOADED) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
