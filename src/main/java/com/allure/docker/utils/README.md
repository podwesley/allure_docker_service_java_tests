# Database Utility Classes

This package contains utility classes for database operations in the project.

## DatabaseManager

The `DatabaseManager` class provides a comprehensive set of methods for database operations, including:

- Connection management with connection pooling
- Executing SQL queries and updates
- Batch operations
- Table management

### Key Features

- Connection pooling using HikariCP for efficient connection management
- Support for different database types (H2, MySQL, etc.)
- Configuration through annotations
- Thread-safe implementation
- Automatic resource management

### Usage

#### Basic Configuration

Use the `@Database` annotation to configure database connection properties:

```java
@Database(
    url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    username = "sa",
    password = "",
    maxPoolSize = 10,
    connectionTimeout = 30000
)
public class MyClass {
    // Your code here
}
```

#### Executing Queries

```java
// Query returning multiple rows
List<Map<String, Object>> users = DatabaseManager.executeQuery("SELECT * FROM USERS WHERE ACTIVE = ?", true);

// Query returning a single value
Object count = DatabaseManager.executeScalar("SELECT COUNT(*) FROM USERS");
```

#### Executing Updates

```java
// Insert a row
int rowsAffected = DatabaseManager.executeUpdate(
    "INSERT INTO USERS (ID, NAME, EMAIL) VALUES (?, ?, ?)",
    1, "John Doe", "john@example.com"
);

// Update a row
rowsAffected = DatabaseManager.executeUpdate(
    "UPDATE USERS SET EMAIL = ? WHERE ID = ?",
    "new.email@example.com", 1
);

// Delete a row
rowsAffected = DatabaseManager.executeUpdate(
    "DELETE FROM USERS WHERE ID = ?",
    1
);
```

#### Batch Operations

```java
List<Object[]> paramsList = new ArrayList<>();
paramsList.add(new Object[] {1, "John Doe", "john@example.com"});
paramsList.add(new Object[] {2, "Jane Smith", "jane@example.com"});

int[] results = DatabaseManager.executeBatch(
    "INSERT INTO USERS (ID, NAME, EMAIL) VALUES (?, ?, ?)",
    paramsList
);
```

#### Table Management

```java
// Check if a table exists
boolean exists = DatabaseManager.tableExists("USERS");

// Create a table if it doesn't exist
String columns = "ID INT PRIMARY KEY, NAME VARCHAR(100), EMAIL VARCHAR(100)";
DatabaseManager.createTableIfNotExists("USERS", columns);
```

#### Direct Connection Access

```java
try (Connection conn = DatabaseManager.getConnection()) {
    // Use the connection directly
    // ...
}
```

#### Cleanup

```java
// Close all data sources when your application is shutting down
DatabaseManager.closeAllDataSources();
```

### Example

See the `DatabaseExample` class in the `com.allure.docker.examples` package for a complete example of how to use the `DatabaseManager` class.

## LoggerManager

The `LoggerManager` class provides logging functionality for the project. See the class documentation for more details.