package com.allure.docker.examples;

import com.allure.docker.annotation.Database;
import com.allure.docker.annotation.Logger;
import com.allure.docker.utils.DatabaseManager;
import com.allure.docker.utils.LoggerManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Example class demonstrating how to use the DatabaseManager.
 * This class shows various database operations using the H2 in-memory database.
 */
@Logger(level = "DEBUG")
@Database(url = "jdbc:h2:mem:exampledb;DB_CLOSE_DELAY=-1", username = "sa", password = "")
public class DatabaseExample {

    /**
     * Main method to run the database example.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Create a table
            createExampleTable();
            
            // Insert data
            insertExampleData();
            
            // Query data
            queryExampleData();
            
            // Update data
            updateExampleData();
            
            // Delete data
            deleteExampleData();
            
            // Execute a batch operation
            executeBatchExample();
            
            // Close all database connections
            DatabaseManager.closeAllDataSources();
            
            LoggerManager.info("Database example completed successfully");
        } catch (SQLException e) {
            LoggerManager.error("Error in database example: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create an example table.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void createExampleTable() throws SQLException {
        LoggerManager.info("Creating example table...");
        
        // Check if the table already exists
        if (DatabaseManager.tableExists("USERS")) {
            LoggerManager.info("Table USERS already exists");
            return;
        }
        
        // Create the table
        String columns = "ID INT PRIMARY KEY, NAME VARCHAR(100), EMAIL VARCHAR(100), ACTIVE BOOLEAN";
        DatabaseManager.createTableIfNotExists("USERS", columns);
        
        LoggerManager.info("Table USERS created successfully");
    }
    
    /**
     * Insert example data into the table.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void insertExampleData() throws SQLException {
        LoggerManager.info("Inserting example data...");
        
        // Insert a single row
        String insertSql = "INSERT INTO USERS (ID, NAME, EMAIL, ACTIVE) VALUES (?, ?, ?, ?)";
        int rowsAffected = DatabaseManager.executeUpdate(insertSql, 1, "John Doe", "john@example.com", true);
        
        LoggerManager.info("Inserted " + rowsAffected + " row(s)");
        
        // Insert multiple rows
        rowsAffected = DatabaseManager.executeUpdate(insertSql, 2, "Jane Smith", "jane@example.com", true);
        rowsAffected += DatabaseManager.executeUpdate(insertSql, 3, "Bob Johnson", "bob@example.com", false);
        
        LoggerManager.info("Inserted " + rowsAffected + " more row(s)");
    }
    
    /**
     * Query example data from the table.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void queryExampleData() throws SQLException {
        LoggerManager.info("Querying example data...");
        
        // Query all users
        String querySql = "SELECT * FROM USERS";
        List<Map<String, Object>> results = DatabaseManager.executeQuery(querySql);
        
        LoggerManager.info("Found " + results.size() + " user(s):");
        for (Map<String, Object> row : results) {
            LoggerManager.info("User: " + row.get("ID") + ", " + row.get("NAME") + ", " + row.get("EMAIL") + ", " + row.get("ACTIVE"));
        }
        
        // Query with a condition
        querySql = "SELECT * FROM USERS WHERE ACTIVE = ?";
        results = DatabaseManager.executeQuery(querySql, true);
        
        LoggerManager.info("Found " + results.size() + " active user(s)");
        
        // Query a scalar value
        querySql = "SELECT COUNT(*) FROM USERS";
        Object count = DatabaseManager.executeScalar(querySql);
        
        LoggerManager.info("Total user count: " + count);
    }
    
    /**
     * Update example data in the table.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void updateExampleData() throws SQLException {
        LoggerManager.info("Updating example data...");
        
        // Update a user
        String updateSql = "UPDATE USERS SET EMAIL = ? WHERE ID = ?";
        int rowsAffected = DatabaseManager.executeUpdate(updateSql, "john.doe@example.com", 1);
        
        LoggerManager.info("Updated " + rowsAffected + " row(s)");
        
        // Verify the update
        String querySql = "SELECT * FROM USERS WHERE ID = ?";
        List<Map<String, Object>> results = DatabaseManager.executeQuery(querySql, 1);
        
        if (!results.isEmpty()) {
            Map<String, Object> user = results.get(0);
            LoggerManager.info("Updated user: " + user.get("ID") + ", " + user.get("NAME") + ", " + user.get("EMAIL"));
        }
    }
    
    /**
     * Delete example data from the table.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void deleteExampleData() throws SQLException {
        LoggerManager.info("Deleting example data...");
        
        // Delete a user
        String deleteSql = "DELETE FROM USERS WHERE ID = ?";
        int rowsAffected = DatabaseManager.executeUpdate(deleteSql, 3);
        
        LoggerManager.info("Deleted " + rowsAffected + " row(s)");
        
        // Verify the delete
        String querySql = "SELECT COUNT(*) FROM USERS";
        Object count = DatabaseManager.executeScalar(querySql);
        
        LoggerManager.info("Remaining user count: " + count);
    }
    
    /**
     * Execute a batch operation.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void executeBatchExample() throws SQLException {
        LoggerManager.info("Executing batch operation...");
        
        // Prepare batch parameters
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(new Object[] {4, "Alice Brown", "alice@example.com", true});
        paramsList.add(new Object[] {5, "Charlie Davis", "charlie@example.com", true});
        paramsList.add(new Object[] {6, "Eve Franklin", "eve@example.com", false});
        
        // Execute batch insert
        String batchSql = "INSERT INTO USERS (ID, NAME, EMAIL, ACTIVE) VALUES (?, ?, ?, ?)";
        int[] results = DatabaseManager.executeBatch(batchSql, paramsList);
        
        int totalInserted = 0;
        for (int result : results) {
            if (result > 0) {
                totalInserted++;
            }
        }
        
        LoggerManager.info("Batch inserted " + totalInserted + " row(s)");
        
        // Verify the batch insert
        String querySql = "SELECT COUNT(*) FROM USERS";
        Object count = DatabaseManager.executeScalar(querySql);
        
        LoggerManager.info("Final user count: " + count);
    }
    
    /**
     * Example of using a direct connection.
     * This method demonstrates how to use a Connection directly.
     * 
     * @throws SQLException If a database access error occurs
     */
    private static void directConnectionExample() throws SQLException {
        LoggerManager.info("Using direct connection...");
        
        try (Connection conn = DatabaseManager.getConnection()) {
            // Use the connection directly
            boolean valid = conn.isValid(5);
            LoggerManager.info("Connection is valid: " + valid);
        }
    }
}