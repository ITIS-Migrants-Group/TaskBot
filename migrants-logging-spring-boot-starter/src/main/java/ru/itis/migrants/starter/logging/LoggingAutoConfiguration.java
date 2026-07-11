package ru.itis.migrants.starter.logging;

import ru.itis.migrants.starter.logging.property.LoggingProperties;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(prefix = "starter.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LoggingAspect loggingAspect(LoggingProperties properties) {
        return new LoggingAspect(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "starter.logging.file", name = "enabled", havingValue = "true")
    public FileLoggingInitializer fileLoggingInitializer(LoggingProperties properties) {
        return new FileLoggingInitializer(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "starter.logging", name = "pointcuts[0]")
    public DefaultPointcutAdvisor loggingPointcutAdvisor(LoggingAspect aspect,
                                                                LoggingProperties properties) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(String.join(" || ", properties.getPointcuts()));
        return new DefaultPointcutAdvisor(pointcut, aspect);
    }
}
