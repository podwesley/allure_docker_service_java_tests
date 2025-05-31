package com.allure.docker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure database connection properties.
 * Classes or methods annotated with @Database will use these properties
 * for database operations through the DatabaseManager.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Database {
    /**
     * JDBC URL for the database connection.
     * Examples:
     * - H2 in-memory: "jdbc:h2:mem:testdb"
     * - MySQL: "jdbc:mysql://localhost:3306/dbname"
     */
    String url() default "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    
    /**
     * Database username.
     */
    String username() default "sa";
    
    /**
     * Database password.
     */
    String password() default "";
    
    /**
     * Maximum pool size for database connections.
     */
    int maxPoolSize() default 10;
    
    /**
     * Connection timeout in milliseconds.
     */
    long connectionTimeout() default 30000;
}