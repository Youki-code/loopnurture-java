package org.springframework.samples.loopnurture.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@ActiveProfiles("test")
public class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String dbName = conn.getMetaData().getDatabaseProductName();
            System.out.println("Database connection successful!");
            System.out.println("Database product name: " + dbName);
            System.out.println("Database product version: " + conn.getMetaData().getDatabaseProductVersion());
            
            assertTrue("PostgreSQL".equals(dbName) || "H2".equals(dbName),
                    "Test database should be PostgreSQL in integration profile or H2 in unit tests, actual: " + dbName);
            
            // 验证数据库连接是否正常工作
            try (var stmt = conn.createStatement()) {
                var rs = stmt.executeQuery("SELECT version()");
                assertTrue(rs.next(), "Should get PostgreSQL version");
                System.out.println("PostgreSQL version: " + rs.getString(1));
            }
        }
    }
} 