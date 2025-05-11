/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

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
    
    private final Connection connection;
    
    public PositionDAO (Connection connection) {
        this.connection = connection;
    } 
    
    public List<Position> getAllPositions () {
        List<Position> positions = new ArrayList<>();
        String query = "SELECT positionID, positionName FROM position";
        
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                positions.add(new Position(rs.getInt("positionID"), rs.getString("positionName")));
            }
            
            return positions;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all positions", e);
        }
    }
    
}
