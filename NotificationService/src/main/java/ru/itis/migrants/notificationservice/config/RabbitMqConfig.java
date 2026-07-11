package ru.itis.migrants.notificationservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import ru.itis.migrants.notificationservice.property.RabbitProperty;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {
    private final RabbitProperty property;

    @Bean
    public Queue pushQueue() {
        return new Queue(property.getQueue(), true);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(property.getExchange());
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(property.getPushKey())
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        RetryTemplate retryTemplate = RetryTemplate.builder()
                .maxAttempts(property.getMaxRetries())
                .fixedBackoff(property.getRetryTimeout())
                .build();

        template.setRetryTemplate(retryTemplate);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("MessageBroker accept message");
            } else {
                log.warn("MessageBroker don't accept message");
            }
        }));
        template.setReturnsCallback((message) -> {
            log.warn("Message returned: {}, {}: {}",
                    message.getExchange(), message.getRoutingKey(), message.getReplyText());
        });
        return template;
    }
}
