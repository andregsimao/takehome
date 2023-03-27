package com.example.takehome.controller;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.model.CountriesResponse;
import com.example.takehome.model.CountryCodeAndName;
import com.example.takehome.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class CountryController {

    CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/countries/same-continent")
    public ResponseEntity<CountriesResponse> getCountriesInSameContinent(@RequestBody List<String> inputCountries) {
        try {
            log.info("[CountryController:getCountriesInSameContinent] Receiving request for input countries: " + inputCountries);
            List<CountryCodeAndName> countriesInSameContinent = countryService.getCountriesInSameContinent(inputCountries);
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
        } catch (Exception e) {
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
