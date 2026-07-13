package ru.itis.migrants.notificationservice.property;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "property.rabbitmq")
@Validated
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class RabbitProperty {
    @NotBlank
    private String queue;

    @NotBlank
    private String exchange;

    @NotBlank
    private String pushKey;

    @Min(1)
    @Max(100)
    private Integer maxRetries;

    @Min(1)
    @Max(3_600_000)
    private Integer retryTimeout;
}
