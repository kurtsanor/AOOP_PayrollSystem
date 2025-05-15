/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import DatabaseConnection.DatabaseConnection;
import Domains.Position;
import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
/**
 *
 * @author keith
 */
public class PositionDAO {
    
    public PositionDAO () {} 
    
    public List<Position> getAllPositions () throws SQLException {
        List<Position> positions = new ArrayList<>();
        String query = "SELECT positionID, positionName FROM position";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                positions.add(new Position(rs.getInt("positionID"), rs.getString("positionName")));
            }
            
            return positions;
            
        } catch (SQLException e) {
            throw new SQLException("Failed to get all positions", e);
        }
    }
    
}
