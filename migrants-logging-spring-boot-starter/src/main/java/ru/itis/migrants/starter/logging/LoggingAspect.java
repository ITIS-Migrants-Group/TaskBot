package ru.itis.migrants.starter.logging;

import ru.itis.migrants.starter.logging.annotation.Sensitive;
import ru.itis.migrants.starter.logging.property.LoggingProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Aspect
public class LoggingAspect implements MethodInterceptor {

    private static final String SENSITIVE_PLACEHOLDER = "****";

    private static final String BEFORE_METHOD_MESSAGE_PATTERN =
            "method {} in {}: called with args {}";

    private static final String AFTER_METHOD_MESSAGE_PATTERN =
            "method {} in {}: result {}";

    private static final String AFTER_THROWING_MESSAGE_PATTERN =
            "method {} in {}: after throw {}, message: {}";

    private final Set<String> sensitiveTypes;
    private final Set<String> silentExceptions;

    public LoggingAspect(LoggingProperties properties) {
        this.sensitiveTypes = new HashSet<>(properties.getSensitiveTypes());
        this.silentExceptions = new HashSet<>(properties.getSilentExceptions());
    }

    @Pointcut("@annotation(ru.itis.migrants.starter.logging.annotation.LogAround) "
            + "|| @within(ru.itis.migrants.starter.logging.annotation.LogAround)")
    public void logAroundAnnotation() {}

    @Around("logAroundAnnotation()")
    public Object logAnnotated(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return logExecution(signature.getMethod(), joinPoint.getArgs(), joinPoint::proceed);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return logExecution(invocation.getMethod(), invocation.getArguments(), invocation::proceed);
    }

    private Object logExecution(Method method, Object[] args, ThrowingSupplier proceed) throws Throwable {
        String methodName = method.getName();
        String methodClass = method.getDeclaringClass().getSimpleName();
        log.debug(BEFORE_METHOD_MESSAGE_PATTERN,
                methodName, methodClass,
                maskArgs(args, method.getParameterAnnotations()));
        try {
            Object result = proceed.get();
            log.debug(AFTER_METHOD_MESSAGE_PATTERN, methodName, methodClass,
                    maskResult(result, method));
            return result;
        } catch (Exception e) {
            logFailure(methodName, methodClass, e);
            throw e;
        }
    }

    private void logFailure(String methodName, String methodClass, Throwable e) {
        if (isSilent(e)) {
            log.debug(AFTER_THROWING_MESSAGE_PATTERN,
                    methodName, methodClass,
                    e.getClass().getSimpleName(), e.getMessage());
        } else {
            log.warn(AFTER_THROWING_MESSAGE_PATTERN,
                    methodName, methodClass,
                    e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    private boolean isSilent(Throwable e) {
        if (silentExceptions.isEmpty()) {
            return false;
        }
        for (Class<?> c = e.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
            if (silentExceptions.contains(c.getName())) {
                return true;
            }
        }
        return false;
    }

    private Object maskResult(Object result, Method method) {
        if (result == null) {
            return null;
        }
        if (method.isAnnotationPresent(Sensitive.class) || isSensitiveType(result)) {
            return SENSITIVE_PLACEHOLDER;
        }
        return result;
    }

    private String maskArgs(Object[] args, Annotation[][] paramAnnotations) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        Object[] masked = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            masked[i] = isSensitiveParam(args[i], paramAnnotations[i])
                    ? SENSITIVE_PLACEHOLDER
                    : args[i];
        }
        return Arrays.toString(masked);
    }

    private boolean isSensitiveParam(Object arg, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Sensitive.class) {
                return true;
            }
        }
        return isSensitiveType(arg);
    }

    private boolean isSensitiveType(Object value) {
        if (value == null) {
            return false;
        }
        return sensitiveTypes.contains(value.getClass().getName());
    }

    @FunctionalInterface
    private interface ThrowingSupplier {
        Object get() throws Throwable;
    }
}
