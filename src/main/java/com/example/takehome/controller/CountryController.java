package com.example.takehome.controller;

import com.example.takehome.model.CountriesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CountryController {
    @PostMapping("/countries/same-continent")
    public ResponseEntity<CountriesResponse> getCountriesInSameContinent(@RequestBody List<String> inputCountries) {
        try {
            List<String> countriesInSameContinent = Arrays.asList("stub1", "stub2");
            CountriesResponse countriesInSameContinentResponse = CountriesResponse
                .buildCountriesListResponse(inputCountries, countriesInSameContinent);

            return new ResponseEntity<>(countriesInSameContinentResponse, HttpStatus.OK);
        } catch (Exception e) {
            CountriesResponse errorResponse = CountriesResponse
                .buildCountriesErrorResponse(inputCountries, e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
