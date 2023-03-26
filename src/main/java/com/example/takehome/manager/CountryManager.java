package com.example.takehome.manager;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CountryManager {

    private final int numberOfDaysToRefreshData = 15;
    private final int numberOfMinutesToCheckData = 2;

    CountryData countryData;

    @Autowired
    public CountryManager(CountryData countryData) {
        this.countryData = countryData;
    }

    @PostConstruct
    public void initializeCountriesData() {
        refreshCountriesData();
    }

    @Scheduled(fixedRate = numberOfDaysToRefreshData * 24 * 3600 * 1000)
    public void refreshCountriesData() {
        log.info("[CountryManager:refreshCountriesData] Refreshing countries data from Trevorblades API.");
        try {
            // TODO
        } catch (Exception e) {
            log.error("[CountryManager:refreshCountriesData] Error while trying to refresh countries data");
        }
    }

    @Scheduled(fixedRate = numberOfMinutesToCheckData * 60 * 1000)
    public void refreshCountriesDataIfMissing() {
        log.info("[CountryManager:refreshCountriesDataIfMissing] Checking if countries data is not loaded");
        if(!countryData.isCountriesDataLoaded()) {
            log.warn("[CountryManager:refreshCountriesDataIfMissing] Countries data is not loaded.");
            refreshCountriesData();
        } else {
            log.info("[CountryManager:refreshCountriesDataIfMissing] Countries data is already loaded");
        }
    }
}
