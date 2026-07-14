package ru.itis.migrants.bot.property;

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
}
