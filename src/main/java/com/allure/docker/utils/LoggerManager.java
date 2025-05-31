package com.allure.docker.utils;

import com.allure.docker.annotation.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for handling logging for classes annotated with @Logger.
 * This class provides static methods for logging at different levels.
 */
public class LoggerManager {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Log levels in order of severity
     */
    public enum LogLevel {
        TRACE(0),
        DEBUG(1),
        INFO(2),
        WARN(3),
        ERROR(4);

        private final int value;

        LogLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Get the calling class from the stack trace
     * 
     * @return The class that called the logging method
     */
    private static Class<?> getCallingClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Find the first class in the stack trace that is not LoggerManager
        for (int i = 2; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (!className.equals(LoggerManager.class.getName())) {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    // If we can't find the class, just return null
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Log a message at the specified level
     * 
     * @param level The log level
     * @param message The message to log
     */
    public static void log(LogLevel level, String message) {
        Class<?> sourceClass = getCallingClass();
        if (sourceClass == null) {
            // If we can't determine the calling class, just use System.out
            System.out.println(message);
            return;
        }

        if (!sourceClass.isAnnotationPresent(Logger.class)) {
            // If the class doesn't have the Logger annotation, just use System.out
            System.out.println(message);
            return;
        }

        Logger loggerAnnotation = sourceClass.getAnnotation(Logger.class);
        String loggerName = loggerAnnotation.name().isEmpty() ? 
                sourceClass.getSimpleName() : loggerAnnotation.name();

        LogLevel configuredLevel = LogLevel.valueOf(loggerAnnotation.level());

        // Only log if the message level is >= the configured level
        if (level.getValue() >= configuredLevel.getValue()) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            System.out.println(String.format("[%s] [%s] [%s] %s", 
                    timestamp, level, loggerName, message));
        }
    }

    /**
     * Log a message at the specified level
     * 
     * @param source The object that is the source of the log message
     * @param level The log level
     * @param message The message to log
     */
    public static void log(Object source, LogLevel level, String message) {
        if (source == null) {
            log(level, message);
            return;
        }

        Class<?> sourceClass = source.getClass();
        if (!sourceClass.isAnnotationPresent(Logger.class)) {
            // If the class doesn't have the Logger annotation, just use System.out
            System.out.println(message);
            return;
        }

        Logger loggerAnnotation = sourceClass.getAnnotation(Logger.class);
        String loggerName = loggerAnnotation.name().isEmpty() ? 
                sourceClass.getSimpleName() : loggerAnnotation.name();

        LogLevel configuredLevel = LogLevel.valueOf(loggerAnnotation.level());

        // Only log if the message level is >= the configured level
        if (level.getValue() >= configuredLevel.getValue()) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            System.out.println(String.format("[%s] [%s] [%s] %s", 
                    timestamp, level, loggerName, message));
        }
    }

    /**
     * Log a message at TRACE level
     */
    public static void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    /**
     * Log a message at TRACE level
     */
    public static void trace(Object source, String message) {
        log(source, LogLevel.TRACE, message);
    }

    /**
     * Log a message at DEBUG level
     */
    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Log a message at DEBUG level
     */
    public static void debug(Object source, String message) {
        log(source, LogLevel.DEBUG, message);
    }

    /**
     * Log a message at INFO level
     */
    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Log a message at INFO level
     */
    public static void info(Object source, String message) {
        log(source, LogLevel.INFO, message);
    }

    /**
     * Log a message at WARN level
     */
    public static void warn(String message) {
        log(LogLevel.WARN, message);
    }

    /**
     * Log a message at WARN level
     */
    public static void warn(Object source, String message) {
        log(source, LogLevel.WARN, message);
    }

    /**
     * Log a message at ERROR level
     */
    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Log a message at ERROR level
     */
    public static void error(Object source, String message) {
        log(source, LogLevel.ERROR, message);
    }

    /**
     * Log a message at ERROR level with an exception
     */
    public static void error(String message, Throwable throwable) {
        error(message + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * Log a message at ERROR level with an exception
     */
    public static void error(Object source, String message, Throwable throwable) {
        error(source, message + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }
}
