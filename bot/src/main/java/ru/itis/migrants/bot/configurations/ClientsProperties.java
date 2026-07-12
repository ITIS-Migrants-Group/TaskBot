package ru.itis.migrants.bot.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "clients")
public record ClientsProperties(ClientProperties gateway) {
    public record ClientProperties(String baseUrl, Duration connectTimeout, Duration readTimeout) {}
}