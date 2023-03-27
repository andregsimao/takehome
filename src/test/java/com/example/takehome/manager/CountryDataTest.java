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
        Map<ContinentEnum, List<Country>> mapCountriesInContinents = new HashMap<>();
        String[] countryCodes1 = new String[]{"COUNT1", "COUNT2"};
        String[] countryCodes2 = new String[]{"COUNT3", "COUNT4"};
        String[] countryCodes3 = new String[]{"COUNT5"};

        String[] countryNames1 = new String[]{"COUNTRY 1", "COUNTRY 2"};
        String[] countryNames2 = new String[]{"COUNTRY 3", "COUNTRY 4"};
        String[] countryNames3 = new String[]{"COUNTRY 5"};

        addCountriesToContinent(mapCountriesInContinents, ContinentEnum.OC, countryCodes1, countryNames1);
        addCountriesToContinent(mapCountriesInContinents, ContinentEnum.AF, countryCodes2, countryNames2);
        addCountriesToContinent(mapCountriesInContinents, ContinentEnum.AN, countryCodes3, countryNames3);

        return mapCountriesInContinents;
    }

    private void addCountriesToContinent(
        Map<ContinentEnum, List<Country>> mapCountriesInContinents,
        ContinentEnum continentEnum,
        String[] countryCodes,
        String[] countryNames
    ) {
        Continent continent = new Continent();
        continent.setCode(continentEnum.name());
        continent.setName(continentEnum.getFullName());

        List<Country> countries = new ArrayList<>();
        for(int i = 0; i < countryCodes.length ; i ++) {
            Country country = new Country();
            country.setContinent(continent);
            country.setCode(countryCodes[i]);
            country.setName(countryNames[i]);
            countries.add(country);
        }
        mapCountriesInContinents.put(continentEnum, countries);
    }
}
