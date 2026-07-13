package ru.itis.migrants.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.itis.migrants.apigateway.client.*;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public NotificationClient notificationClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl("http://notification-service:8093")
                        .build())).build();

        return factory.createClient(NotificationClient.class);
    }

    @Bean
    public TaskClient taskClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl("http://todo-service:8091")
                        .build())).build();

        return factory.createClient(TaskClient.class);
    }

    @Bean
    public UserClient userClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl("http://user-service:8090")
                        .build())).build();

        return factory.createClient(UserClient.class);
    }

    @Bean
    public DocumentClient documentClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl("http://document-service:8092")
                        .build())).build();

        return factory.createClient(DocumentClient.class);
    }

    @Bean
    public ContactClient contactClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(WebClient.builder()
                        .baseUrl("http://contact-service:8094")
                        .build())).build();

        return factory.createClient(ContactClient.class);

    }
}