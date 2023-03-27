package com.example.takehome.model;

public class Country {
    private String code;
    private String name;

    private Continent continent;

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public Continent getContinent() {
        return continent;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "Country(" +
            "code=" + code + ", " +
            "name=" + name + ", " +
            "continent=" + continent + ")";
    }
}
