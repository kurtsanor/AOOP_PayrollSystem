/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Dao.PositionDAO;
import Model.Position;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
/**
 *
 * @author admin
 */
public class PositionDAOTest {
    
    private static PositionDAO dao;
    
    @BeforeAll
    public static void setUp() {
        dao = new PositionDAO();
    }
    
    @Test
    public void testGetAllPositions() throws SQLException {
        List<Position> positions = dao.getAllPositions();
        assertTrue(!positions.isEmpty());
    }
}
