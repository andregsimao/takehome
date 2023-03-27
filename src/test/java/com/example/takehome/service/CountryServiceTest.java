package com.example.takehome.service;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.manager.CountryData;
import com.example.takehome.model.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @InjectMocks
    CountryService countryService;

    @Mock
    CountryData countryData;

    @Test
    public void givenInputCountries_WhenInputIsValid_ThenReturnCountryList() throws ApplicationException {
        List<String> inputCountries = Arrays.asList("COUNTRY1", "COUNTRY2");
        Set<String> otherCountries = new HashSet<>(Arrays.asList("COUNTRY3", "COUNTRY4"));
        List<Country> expectedCountries = new ArrayList<>();
        for (String countryInput : otherCountries) {
            Country country = new Country();
            country.setCode(countryInput);
            expectedCountries.add(country);
        }

        when(countryData.containsCountryCode(any())).thenReturn(true);
        when(countryData.getContinentsInCountries(any())).thenReturn(otherCountries);
        when(countryData.getCountriesInSameContinents(any(), any())).thenReturn(expectedCountries);

        List<Country> countries = countryService.getCountriesInSameContinent(inputCountries);
        for (Country country : countries) {
            assertTrue(expectedCountries.stream().anyMatch(c -> c.getCode().equals(country.getCode())));
        }
    }

    @Test
    public void givenInputCountries_WhenInputIsInvalid_ThenThrowsException() {
        List<String> inputCountries = new ArrayList<>();
        String expectedMessage = "Invalid input data";
        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> countryService.getCountriesInSameContinent(inputCountries));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
        Assertions.assertEquals(ErrorType.INVALID_COUNTRY_INPUT, exception.getErrorType());
    }

    @Test
    public void givenInputCountries_WhenInputIsCountryName_ThenRetrieveCountryCode() throws ApplicationException {
        List<String> inputCountries = Arrays.asList("country1", "country name 2");

        when(countryData.containsCountryCode("COUNTRY1")).thenReturn(true);
        when(countryData.containsCountryCode("COUNTRY NAME 2")).thenReturn(false);
        when(countryData.getCountryCodeFromCountryName("COUNTRY NAME 2")).thenReturn("COUNTRY2");

        List<Country> countries = countryService.getCountriesInSameContinent(inputCountries);

        verify(countryData, times(0)).containsCountryCode("country1");
        verify(countryData, times(1)).containsCountryCode("COUNTRY1");
        verify(countryData, times(1)).containsCountryCode("COUNTRY NAME 2");
        verify(countryData, times(1)).getCountryCodeFromCountryName("COUNTRY NAME 2");
    }

    @Test
    public void givenInputCountries_WhenInputIsNotInCountriesData_ThenThrowsException() {
        List<String> inputCountries = Arrays.asList("country1", "country name 2");

        when(countryData.containsCountryCode("COUNTRY1")).thenReturn(false);
        when(countryData.getCountryCodeFromCountryName("COUNTRY1")).thenReturn(null);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class, () -> countryService.getCountriesInSameContinent(inputCountries));

        String expectedMessage = "Country country1 is not a valid input";

        Assertions.assertEquals(expectedMessage, exception.getMessage());
        Assertions.assertEquals(ErrorType.INVALID_COUNTRY_INPUT, exception.getErrorType());
    }
}