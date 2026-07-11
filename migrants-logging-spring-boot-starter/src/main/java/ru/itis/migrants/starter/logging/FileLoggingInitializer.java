package ru.itis.migrants.starter.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import ru.itis.migrants.starter.logging.property.LoggingProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
public class FileLoggingInitializer {

    private static final String APPENDER_NAME = "EDUFLOW_FILE";

    private final LoggingProperties properties;

    @Value("${spring.application.name:application}")
    private String applicationName;

    @PostConstruct
    public void initialize() {
        LoggingProperties.File config = properties.getFile();
        if (!config.isEnabled()) {
            return;
        }
        Path directory = Paths.get(config.getDirectory());
        try {
            Files.createDirectories(directory);
        } catch (Exception e) {
            log.warn("Failed to create log directory {}: {}", directory, e.getMessage());
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(config.getTimestampPattern()));
        String fileName = "%s_%s.log".formatted(applicationName, timestamp);
        Path target = directory.resolve(fileName);

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(config.getPattern());
        encoder.start();

        FileAppender<ch.qos.logback.classic.spi.ILoggingEvent> appender = new FileAppender<>();
        appender.setName(APPENDER_NAME);
        appender.setContext(context);
        appender.setFile(target.toString());
        appender.setEncoder(encoder);
        appender.setAppend(true);
        appender.start();

        Logger root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        if (root instanceof ch.qos.logback.classic.Logger logbackRoot) {
            logbackRoot.addAppender(appender);
        }
        log.info("File logging enabled, writing to {}", target);
    }
}
