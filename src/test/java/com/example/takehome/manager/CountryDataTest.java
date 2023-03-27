package com.example.takehome.manager;

import com.example.takehome.enums.ContinentEnum;
import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.model.Continent;
import com.example.takehome.model.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CountryDataTest {

    CountryData countryData;

    @BeforeEach
    public void setUp() {
        countryData = new CountryData();
        Map<ContinentEnum, List<Country>> countriesInContinents = getCountryDataMapStub();
        countryData.setCountriesData(countriesInContinents);
    }

    @Test
    public void givenCountriesCode_ThenGetContinents() {
        String country1Code = "COUNT1";
        String country4Code = "COUNT4";
        Set<String> countriesCode = new HashSet<>(Arrays.asList(country1Code, country4Code));
        Set<String> continentsCode = countryData.getContinentsInCountries(countriesCode);
        assertEquals(2, continentsCode.size());
        assertTrue(continentsCode.contains(ContinentEnum.OC.name()));
        assertTrue(continentsCode.contains(ContinentEnum.AF.name()));
    }

    @Test
    public void givenContinentsAndCountriesCode_ThenGetCountriesInSameContinent() {
        String country1Code = "COUNT1";
        String country4Code = "COUNT4";
        Set<String> countriesCode = new HashSet<>(Arrays.asList(country1Code, country4Code));
        Set<String> continentsCode = new HashSet<>(Arrays.asList(ContinentEnum.OC.name(), ContinentEnum.AF.name()));

        List<Country> countriesInSameContinent = countryData
            .getCountriesInSameContinents(continentsCode, countriesCode);

        assertEquals(2, countriesInSameContinent.size());

        assertTrue(countriesInSameContinent.stream().anyMatch(c -> c.getCode().equals("COUNT2")));
        assertTrue(countriesInSameContinent.stream().anyMatch(c -> c.getCode().equals("COUNT3")));
    }

    @Test
    public void givenDataIsEmpty_WhenCheckingDataIsLoaded_ThenThrowException() {
        String expectedMessage = "The countries data is not loaded. Try again later.";
        countryData = new CountryData();
        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> countryData.checkIfCountriesDataIsLoaded());

        Assertions.assertEquals(expectedMessage, exception.getMessage());
        Assertions.assertEquals(ErrorType.COUNTRY_DATA_NOT_LOADED, exception.getErrorType());
    }

    @Test
    public void givenValidCountryCode_WhenCheckingCode_ThenReturnTrue() {
        assertTrue(countryData.containsCountryCode("COUNT1"));
    }

    @Test
    public void givenInValidCountryCode_WhenCheckingCode_ThenReturnFalse() {
        assertFalse(countryData.containsCountryCode("InvalidCountryCode"));
    }

    @Test
    public void givenCountryName_ThenReturnCountryCode() {
        assertEquals("COUNT2", countryData.getCountryCodeFromCountryName("COUNTRY 2"));
    }

    private Map<ContinentEnum, List<Country>> getCountryDataMapStub() {
        ContinentEnum continentEnum1 = ContinentEnum.OC;
        Continent continent1 = new Continent();
        continent1.setCode(continentEnum1.name());
        continent1.setName(continentEnum1.getFullName());

        List<Country> countries1 = new ArrayList<>();

        Country country1 = new Country();
        country1.setContinent(continent1);
        country1.setCode("COUNT1");
        country1.setName("COUNTRY 1");
        countries1.add(country1);

        Country country2 = new Country();
        country2.setContinent(continent1);
        country2.setCode("COUNT2");
        country2.setName("COUNTRY 2");
        countries1.add(country2);

        ContinentEnum continentEnum2 = ContinentEnum.AF;
        Continent continent2 = new Continent();
        continent2.setCode(continentEnum2.name());
        continent2.setName(continentEnum2.getFullName());

        List<Country> countries2 = new ArrayList<>();

        Country country3 = new Country();
        country3.setContinent(continent2);
        country3.setCode("COUNT3");
        country3.setName("COUNTRY 3");
        countries2.add(country3);

        Country country4 = new Country();
        country4.setContinent(continent2);
        country4.setCode("COUNT4");
        country4.setName("COUNTRY 4");
        countries2.add(country4);

        ContinentEnum continentEnum3 = ContinentEnum.AN;
        Continent continent3 = new Continent();
        continent3.setCode(continentEnum3.name());
        continent3.setName(continentEnum3.getFullName());

        List<Country> countries3 = new ArrayList<>();

        Country country5 = new Country();
        country5.setContinent(continent3);
        country5.setCode("COUNT5");
        country5.setName("COUNTRY 5");
        countries3.add(country5);

        Map<ContinentEnum, List<Country>> mapCountriesInContinents = new HashMap<>();
        mapCountriesInContinents.put(continentEnum1, countries1);
        mapCountriesInContinents.put(continentEnum2, countries2);
        mapCountriesInContinents.put(continentEnum3, countries3);
        return mapCountriesInContinents;
    }
}
