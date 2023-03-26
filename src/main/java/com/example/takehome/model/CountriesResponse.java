package com.example.takehome.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountriesResponse {
    private String message;

    private List<String> countriesInput = new ArrayList<>();

    private Set<String> countriesOutput = new HashSet<>();

    private CountriesResponse() { }

    public static CountriesResponse buildCountriesListResponse(List<String> countriesInput, Set<String> countriesOutput) {
        String inputConcatenated = String.join(",", countriesInput);
        CountriesResponse response = new CountriesResponse();
        response.countriesInput = countriesInput;
        response.countriesOutput = countriesOutput;
        response.message = "There are " + countriesOutput.size() +
            " countries in the same continents as the input countries " + inputConcatenated;
        return response;
    }

    public static CountriesResponse buildCountriesErrorResponse(List<String> countriesInput, String errorMessage) {
        String inputConcatenated = String.join(",", countriesInput);
        CountriesResponse response = new CountriesResponse();
        response.countriesInput = countriesInput;
        response.message = "Error to get the countries in the same continent as the input countries " +
            inputConcatenated + ": " + errorMessage;
        return response;
    }

    public List<String> getCountriesInput() {
        return countriesInput;
    }

    public Set<String> getCountriesOutput() {
        return countriesOutput;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        String inputConcatenated = String.join(",", countriesInput);
        String outputConcatenated = String.join(",", countriesOutput);

        return "CountriesResponse(" +
            "message=" + message + ", " +
            "countriesInput=" + inputConcatenated + ", " +
            "countriesOutput=" + outputConcatenated + " " +
            ")";
    }
}
