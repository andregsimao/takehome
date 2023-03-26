package com.example.takehome.config;

import com.example.takehome.manager.CountryData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
@Configuration
public class ScopesConfig {
    @Bean
    @Scope("singleton")
    public CountryData countryDataSingleton() {
        return new CountryData();
    }
}
