package ru.itis.migrants.bot.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import ru.itis.migrants.bot.property.RabbitProperty;


@Configuration
@RequiredArgsConstructor
public class RabbitConfig {
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
}
