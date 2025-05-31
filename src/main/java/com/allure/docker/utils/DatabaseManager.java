package com.allure.docker.utils;

import com.allure.docker.annotation.Database;
import com.allure.docker.annotation.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for database operations.
 * This class provides methods for executing SQL queries and managing database connections.
 */
@Logger(level = "INFO")
public class DatabaseManager {
    private static final Map<String, HikariDataSource> DATA_SOURCES = new ConcurrentHashMap<>();
    private static final String DEFAULT_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DEFAULT_USERNAME = "sa";
    private static final String DEFAULT_PASSWORD = "";
    private static final int DEFAULT_MAX_POOL_SIZE = 10;
    private static final long DEFAULT_CONNECTION_TIMEOUT = 30000;

    /**
     * Get a database connection based on the calling class's @Database annotation.
     * If the class doesn't have the annotation, default connection parameters are used.
     *
     * @return A database connection
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        Class<?> callingClass = getCallingClass();
        return getConnection(callingClass);
    }

    /**
     * Get a database connection based on the specified class's @Database annotation.
     * If the class doesn't have the annotation, default connection parameters are used.
     *
     * @param sourceClass The class to get the database configuration from
     * @return A database connection
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection(Class<?> sourceClass) throws SQLException {
        String url = DEFAULT_URL;
        String username = DEFAULT_USERNAME;
        String password = DEFAULT_PASSWORD;
        int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

        if (sourceClass != null && sourceClass.isAnnotationPresent(Database.class)) {
            Database dbAnnotation = sourceClass.getAnnotation(Database.class);
            url = dbAnnotation.url();
            username = dbAnnotation.username();
            password = dbAnnotation.password();
            maxPoolSize = dbAnnotation.maxPoolSize();
            connectionTimeout = dbAnnotation.connectionTimeout();
        }

        return getConnection(url, username, password, maxPoolSize, connectionTimeout);
    }

    /**
     * Get a database connection with the specified parameters.
     *
     * @param url JDBC URL for the database
     * @param username Database username
     * @param password Database password
     * @param maxPoolSize Maximum connection pool size
     * @param connectionTimeout Connection timeout in milliseconds
     * @return A database connection
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection(String url, String username, String password, 
                                          int maxPoolSize, long connectionTimeout) throws SQLException {
        String dataSourceKey = url + username;
        
        if (!DATA_SOURCES.containsKey(dataSourceKey)) {
            synchronized (DatabaseManager.class) {
                if (!DATA_SOURCES.containsKey(dataSourceKey)) {
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(url);
                    config.setUsername(username);
                    config.setPassword(password);
                    config.setMaximumPoolSize(maxPoolSize);
                    config.setConnectionTimeout(connectionTimeout);
                    config.setAutoCommit(true);
                    
                    HikariDataSource dataSource = new HikariDataSource(config);
                    DATA_SOURCES.put(dataSourceKey, dataSource);
                    LoggerManager.info("Created new database connection pool for " + url);
                }
            }
        }
        
        return DATA_SOURCES.get(dataSourceKey).getConnection();
    }

    /**
     * Execute a SQL query that returns a result set.
     *
     * @param sql The SQL query to execute
     * @param params Parameters for the prepared statement
     * @return A list of maps, where each map represents a row with column names as keys
     * @throws SQLException If a database access error occurs
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {
            
            return resultSetToList(rs);
        }
    }

    /**
     * Execute a SQL update statement (INSERT, UPDATE, DELETE).
     *
     * @param sql The SQL statement to execute
     * @param params Parameters for the prepared statement
     * @return The number of rows affected
     * @throws SQLException If a database access error occurs
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, sql, params)) {
            
            return stmt.executeUpdate();
        }
    }

    /**
     * Execute a SQL batch update with multiple sets of parameters.
     *
     * @param sql The SQL statement to execute
     * @param paramsList List of parameter arrays for batch execution
     * @return An array of update counts
     * @throws SQLException If a database access error occurs
     */
    public static int[] executeBatch(String sql, List<Object[]> paramsList) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Object[] params : paramsList) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                stmt.addBatch();
            }
            
            return stmt.executeBatch();
        }
    }

    /**
     * Execute a SQL query and return a single value from the first row and column.
     *
     * @param sql The SQL query to execute
     * @param params Parameters for the prepared statement
     * @return The value from the first row and column, or null if no results
     * @throws SQLException If a database access error occurs
     */
    public static Object executeScalar(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        }
    }

    /**
     * Close all data sources and release connections.
     */
    public static void closeAllDataSources() {
        for (HikariDataSource dataSource : DATA_SOURCES.values()) {
            if (!dataSource.isClosed()) {
                dataSource.close();
            }
        }
        DATA_SOURCES.clear();
        LoggerManager.info("Closed all database connection pools");
    }

    /**
     * Get the calling class from the stack trace.
     *
     * @return The class that called the DatabaseManager method
     */
    private static Class<?> getCallingClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Find the first class in the stack trace that is not DatabaseManager
        for (int i = 2; i < stackTrace.length; i++) {
            String className = stackTrace[i].getClassName();
            if (!className.equals(DatabaseManager.class.getName())) {
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
     * Prepare a statement with the given parameters.
     *
     * @param conn The database connection
     * @param sql The SQL statement
     * @param params The parameters for the statement
     * @return A prepared statement
     * @throws SQLException If a database access error occurs
     */
    private static PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        return stmt;
    }

    /**
     * Convert a ResultSet to a List of Maps.
     *
     * @param rs The ResultSet to convert
     * @return A list of maps, where each map represents a row with column names as keys
     * @throws SQLException If a database access error occurs
     */
    private static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            
            results.add(row);
        }
        
        return results;
    }

    /**
     * Execute a SQL script containing multiple statements separated by semicolons.
     *
     * @param script The SQL script to execute
     * @throws SQLException If a database access error occurs
     */
    public static void executeScript(String script) throws SQLException {
        try (Connection conn = getConnection()) {
            String[] statements = script.split(";");
            
            for (String statement : statements) {
                String trimmedStatement = statement.trim();
                if (!trimmedStatement.isEmpty()) {
                    try (PreparedStatement stmt = conn.prepareStatement(trimmedStatement)) {
                        stmt.execute();
                    }
                }
            }
        }
    }

    /**
     * Check if a table exists in the database.
     *
     * @param tableName The name of the table to check
     * @return true if the table exists, false otherwise
     */
    public static boolean tableExists(String tableName) {
        try (Connection conn = getConnection()) {
            ResultSet tables = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null);
            return tables.next();
        } catch (SQLException e) {
            LoggerManager.error("Error checking if table exists: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Create a table in the database if it doesn't exist.
     *
     * @param tableName The name of the table to create
     * @param columns The column definitions (e.g., "id INT PRIMARY KEY, name VARCHAR(255)")
     * @throws SQLException If a database access error occurs
     */
    public static void createTableIfNotExists(String tableName, String columns) throws SQLException {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", tableName, columns);
        executeUpdate(sql);
        LoggerManager.info("Created table if not exists: " + tableName);
    }
}