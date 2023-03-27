package com.example.takehome.controller;

import com.example.takehome.exception.ApplicationException;
import com.example.takehome.exception.ErrorType;
import com.example.takehome.model.CountriesRequest;
import com.example.takehome.model.Country;
import com.example.takehome.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @MockBean
    CountryService countryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void givenValidRequest_ThenReturnCountriesInSameContinent() throws Exception {
        CountriesRequest countriesRequest = new CountriesRequest();
        List<String> countryList = Arrays.asList("code1", "code2");
        countriesRequest.setCountries(countryList);

        List<Country> expectedCountries = new ArrayList<>();
        for(String contryCode: countryList) {
            Country country = new Country();
            country.setCode(contryCode);
            expectedCountries.add(country);
        }

        Mockito.when(countryService.getCountriesInSameContinent(countryList)).thenReturn(expectedCountries);

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/countries/same-continent")
                .content(asJsonString(countriesRequest))
                .header(HttpHeaders.AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.countriesOutput[0].code", Matchers.is("code1")))
            .andExpect(jsonPath("$.countriesOutput[1].code", Matchers.is("code2")));
    }

    @ParameterizedTest
    @EnumSource(
        value = ErrorType.class,
        names = {"INVALID_COUNTRY_INPUT", "COUNTRY_DATA_NOT_LOADED"}
    )
    public void givenInValidRequest_WhenExceptionIsApplicationException_ThenReturnsExpectedErrorStatusCode(
        ErrorType errorType
    ) throws Exception {
        CountriesRequest countriesRequest = new CountriesRequest();
        List<String> countryList = Arrays.asList("code1", "code2");
        countriesRequest.setCountries(countryList);

        String exceptionMessage = "exception message";
        ApplicationException exception = new ApplicationException(errorType, exceptionMessage);
        String inputConcatenated = String.join(", ", countryList);
        String expectedMessage = "Error to get the countries in the same continent as the input countries " +
            inputConcatenated + ": " + exceptionMessage;

        ResultMatcher resultMatcher = errorType == ErrorType.COUNTRY_DATA_NOT_LOADED ?
            status().isInternalServerError() : status().isBadRequest();

        Mockito.when(countryService.getCountriesInSameContinent(countryList)).thenThrow(exception);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/countries/same-continent")
                .content(asJsonString(countriesRequest))
                .header(HttpHeaders.AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(resultMatcher)
            .andExpect(jsonPath("$.message", Matchers.is(expectedMessage)));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
