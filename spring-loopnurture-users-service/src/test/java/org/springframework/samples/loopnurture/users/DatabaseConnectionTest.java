package org.springframework.samples.loopnurture.users;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() {
        String url = "jdbc:postgresql://db.cilxaqxdcffzpggwjphz.supabase.co:5432/postgres";
        String username = "postgres";
        String password = "loopnurture1a2b3c4d!";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection test successful!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Database connection test failed!");
            System.err.println("Error message: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}