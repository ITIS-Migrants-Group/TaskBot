package ru.itis.migrants.notificationservice.publisher;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.notificationservice.property.RabbitProperty;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RetriableProducer {
    private final RabbitProperty property;

    public void publishWithRetry(
            Channel channel, String exchange,
            String routingKey, byte[] body, AMQP.BasicProperties properties)
            throws Exception {
        int attempt = 0;
        boolean confirmed = false;
        Exception lastException = null;

        channel.confirmSelect();

        while (attempt < property.getMaxRetries() && !confirmed) {
            try {
                channel.basicPublish(exchange, routingKey, properties, body);
                confirmed = channel.waitForConfirms(property.getRetryTimeout());

                int currentAttempt = attempt + 1;
                if (confirmed) {
                    log.debug("Retry {}, message confirmed", currentAttempt);
                } else {
                    log.warn("Message don't confirmed, retry {} failed", currentAttempt);
                }
            } catch (IOException | InterruptedException | TimeoutException e) {
                lastException = e;
                Thread.currentThread().interrupt();
            }

            if (!confirmed) {
                attempt++;
                if (attempt < property.getMaxRetries()) {
                    log.debug("Next try after {}ms", property.getRetryTimeout());
                    Thread.sleep(property.getRetryTimeout());
                }
            }
        }

        if (!confirmed) {
            log.error("Message don't accepted after {} retries", property.getMaxRetries(), lastException);
        }
    }
}
