package com.example.takehome.model;

import java.util.ArrayList;
import java.util.List;

public class CountriesRequest {
    private List<String> countries = new ArrayList<>();

    public List<String> getCountries() {
        return countries;
    }
    public void setCountries(List<String> countries) {
        this.countries = countries;
    }
}
