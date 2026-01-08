package com.trading.market.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SseConfig implements WebMvcConfigurer {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // Set default timeout for SseEmitters to a longer duration (30 minutes)
        configurer.setDefaultTimeout(30 * 60 * 1000); // 30 minutes
    }
}