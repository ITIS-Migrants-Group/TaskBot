package ru.itis.migrants.starter.logging.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "starter.logging")
public class LoggingProperties {

    private boolean enabled = true;

    private List<String> pointcuts = Collections.emptyList();

    private List<String> sensitiveTypes = Collections.emptyList();

    private List<String> silentExceptions = Collections.emptyList();

    private File file = new File();

    @Getter
    @Setter
    public static class File {
        private boolean enabled = false;
        private String directory = "logs";
        private String pattern =
                "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n";
        private String timestampPattern = "yyyy-MM-dd_HH-mm-ss";
    }
}
