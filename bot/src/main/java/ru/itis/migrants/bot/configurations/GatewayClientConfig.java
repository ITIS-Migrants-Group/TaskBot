package ru.itis.migrants.bot.configurations;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.invoker.ApiClient;

@Configuration
public class GatewayClientConfig {

    @Bean
    public ApiClient gatewayApiClient(RestTemplateBuilder builder, ClientsProperties props) {
        RestTemplate restTemplate = builder.connectTimeout(props.gateway().connectTimeout())
                .readTimeout(props.gateway().readTimeout())
                .build();

        ApiClient client = new ApiClient(restTemplate);
        client.setBasePath(props.gateway().baseUrl());
        return client;
    }

    @Bean
    public DefaultApi defaultApi(ApiClient gatewayApiClient) {
        return new DefaultApi(gatewayApiClient);
    }
}
