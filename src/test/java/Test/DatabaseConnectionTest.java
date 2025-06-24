package Test;

import Util.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    public void testGetConnection_SuccessfulConnection() throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            assertNotNull(con);
            assertFalse(con.isClosed());
        }
    }
}
