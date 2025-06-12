/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;
import Domains.PagibigMatrix;
import Domains.PhilhealthMatrix;
import Domains.SSSMatrix;
import Domains.TaxMatrix;
import Model.DeductionDAO;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
/**
 *
 * @author admin
 */
public class DeductionDAOTest {
    private static DeductionDAO dao;
    
    @BeforeAll
    public static void setUp() {
        dao = new DeductionDAO();
    }
    
    @Test
    public void testGetSSSMatrix() throws SQLException {
        List<SSSMatrix> matrix = dao.getSSSMatrix();
        assertNotNull(matrix);
    }
    
    @Test
    public void testGetPhilhealthMatrix() throws SQLException {
        List<PhilhealthMatrix> matrix = dao.getPhilhealthMatrix();
        assertNotNull(matrix);
    }
    
    @Test
    public void testGetPagibigMatrix() throws SQLException {
        List<PagibigMatrix> matrix = dao.getPagibigMatrix();
        assertNotNull(matrix);
    }
    
    @Test
    public void testGetTaxMatrix() throws SQLException {
        List<TaxMatrix> matrix = dao.getTaxMatrix();
        assertNotNull(matrix);
    }
}
