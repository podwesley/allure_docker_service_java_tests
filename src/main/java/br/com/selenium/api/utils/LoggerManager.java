package br.com.selenium.api.utils;

import br.com.selenium.api.annotation.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerManager {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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

    private static Class<?> getCallingClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (int i = 2; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (!className.equals(LoggerManager.class.getName())) {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {

                    return null;
                }
            }
        }

        return null;
    }


    public static void log(LogLevel level, String message) {
        Class<?> sourceClass = getCallingClass();
        if (sourceClass == null) {

            System.out.println(message);
            return;
        }

        if (!sourceClass.isAnnotationPresent(Logger.class)) {

            System.out.println(message);
            return;
        }

        Logger loggerAnnotation = sourceClass.getAnnotation(Logger.class);
        String loggerName = loggerAnnotation.name().isEmpty() ? 
                sourceClass.getSimpleName() : loggerAnnotation.name();

        LogLevel configuredLevel = LogLevel.valueOf(loggerAnnotation.level());


        if (level.getValue() >= configuredLevel.getValue()) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            System.out.println(String.format("[%s] [%s] [%s] %s", 
                    timestamp, level, loggerName, message));
        }
    }


    public static void log(Object source, LogLevel level, String message) {
        if (source == null) {
            log(level, message);
            return;
        }

        Class<?> sourceClass = source.getClass();
        if (!sourceClass.isAnnotationPresent(Logger.class)) {

            System.out.println(message);
            return;
        }

        Logger loggerAnnotation = sourceClass.getAnnotation(Logger.class);
        String loggerName = loggerAnnotation.name().isEmpty() ? 
                sourceClass.getSimpleName() : loggerAnnotation.name();

        LogLevel configuredLevel = LogLevel.valueOf(loggerAnnotation.level());


        if (level.getValue() >= configuredLevel.getValue()) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            System.out.println(String.format("[%s] [%s] [%s] %s", 
                    timestamp, level, loggerName, message));
        }
    }


    public static void trace(String message) {
        log(LogLevel.TRACE, message);
    }


    public static void trace(Object source, String message) {
        log(source, LogLevel.TRACE, message);
    }


    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }


    public static void debug(Object source, String message) {
        log(source, LogLevel.DEBUG, message);
    }


    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void info(Object source, String message) {
        log(source, LogLevel.INFO, message);
    }

    public static void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public static void warn(Object source, String message) {
        log(source, LogLevel.WARN, message);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public static void error(Object source, String message) {
        log(source, LogLevel.ERROR, message);
    }

    public static void error(String message, Throwable throwable) {
        error(message + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public static void error(Object source, String message, Throwable throwable) {
        error(source, message + ": " + throwable.getMessage());
        throwable.printStackTrace();
    }
}