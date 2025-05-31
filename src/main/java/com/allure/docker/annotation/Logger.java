package com.allure.docker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes that should use the logging system.
 * Classes annotated with @Logger will have their System.out.println
 * statements processed by the LoggerManager.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Logger {
    /**
     * Optional name for the logger. If not specified, the class name will be used.
     */
    String name() default "";
    
    /**
     * Log level for this class.
     * Possible values: TRACE, DEBUG, INFO, WARN, ERROR
     */
    String level() default "INFO";
}