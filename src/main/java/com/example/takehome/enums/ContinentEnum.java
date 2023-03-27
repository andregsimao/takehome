package com.example.takehome.enums;

public enum ContinentEnum {

    AF("Africa"),
    AN("Antarctica"),
    AS("Asia"),
    EU("Europe"),
    NA("North America"),
    OC("Oceania"),
    SA("South America");

    final String fullName;

    ContinentEnum(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
