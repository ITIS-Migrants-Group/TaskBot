package ru.itis.migrants.bot.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.invoker.ApiClient;
import ru.itis.migrants.bot.invoker.RFC3339JavaTimeModule;

@Configuration
public class GatewayClientConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JsonNullableModule());
        return mapper;
    }

    @Bean
    public ApiClient gatewayApiClient(RestTemplateBuilder builder, ClientsProperties props, ObjectMapper objectMapper) {
        RestTemplate restTemplate = builder.connectTimeout(props.gateway().connectTimeout())
                .readTimeout(props.gateway().readTimeout())
                .build();

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        restTemplate.getMessageConverters()
                .removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);

        restTemplate.getMessageConverters()
                .add(converter);

        ApiClient client = new ApiClient(restTemplate);
        client.setBasePath(props.gateway().baseUrl());
        return client;
    }

    @Bean
    public DefaultApi defaultApi(ApiClient gatewayApiClient) {
        return new DefaultApi(gatewayApiClient);
    }
}
