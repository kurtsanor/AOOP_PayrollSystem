/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author keith
 */
public class Position {
    private int positionID;
    private String positionName;
    
    public Position (int positionID, String positionName) {
        this.positionID = positionID;
        this.positionName = positionName;
    }
    
    public int getPositionID () {
        return positionID;
    }
    
    public String getPositionName () {
        return positionName;
    }
}
