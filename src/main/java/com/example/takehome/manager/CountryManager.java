package com.example.takehome.manager;

import com.example.takehome.enums.Continent;
import com.example.takehome.model.CountryCodeAndName;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import graphql.kickstart.spring.webclient.boot.GraphQLWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CountryManager {

    private final int numberOfDaysToRefreshData = 15;
    private final int numberOfMinutesToCheckData = 2;

    @Value("${graphql.client.url}")
    private String graphqlClientUrl;

    CountryData countryData;

    @Autowired
    public CountryManager(CountryData countryData) {
        this.countryData = countryData;
    }

    @Scheduled(fixedRate = numberOfDaysToRefreshData * 24 * 3600 * 1000)
    public void refreshCountriesData() {
        log.info("[CountryManager:refreshCountriesData] Refreshing countries data from Trevorblades API.");
        try {
            Map<Continent, List<CountryCodeAndName>> countriesInContinents = new HashMap<>();
            for(Continent continent: Continent.values()) {
                List<CountryCodeAndName> countriesInContinent = requestCountriesInContinent(continent);
                countriesInContinents.put(continent, countriesInContinent);
            }
            countryData.setCountriesData(countriesInContinents);
        } catch (Exception e) {
            log.error("[CountryManager:refreshCountriesData] Error while trying to refresh countries data: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = numberOfMinutesToCheckData * 60 * 1000, initialDelay = 60000)
    public void refreshCountriesDataIfMissing() {
        log.info("[CountryManager:refreshCountriesDataIfMissing] Checking if countries data is not loaded");
        if(!countryData.isCountriesDataLoaded()) {
            log.warn("[CountryManager:refreshCountriesDataIfMissing] Countries data is not loaded.");
            refreshCountriesData();
        } else {
            log.info("[CountryManager:refreshCountriesDataIfMissing] Countries data is already loaded");
        }
    }

    private List<CountryCodeAndName> requestCountriesInContinent(Continent continent) {
        ObjectMapper objectMapper = new ObjectMapper();
        WebClient webClient = WebClient.builder()
            .baseUrl(graphqlClientUrl).build();
        GraphQLWebClient graphqlClient = GraphQLWebClient.newInstance(webClient, objectMapper);
        String queryCountriesInContinent = getQueryCountriesInContinent(continent);
        var response = graphqlClient.post(GraphQLRequest.builder()
            .query(queryCountriesInContinent).build()).block();

        return response.getFirstList(CountryCodeAndName.class);
    }

    private String getQueryCountriesInContinent(Continent continent) {
        return "query { countries(filter: { continent: { eq: \"" + continent.name() + "\"} }) { code, name } }";
    }
}
