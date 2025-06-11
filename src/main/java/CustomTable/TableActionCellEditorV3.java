/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomTable;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author admin
 */
public class TableActionCellEditorV3 extends DefaultCellEditor {
    private TableActionEventV3 event;
    public TableActionCellEditorV3 (TableActionEventV3 event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelActionV3 action = new PanelActionV3();
        action.initEvent(event, row);
        action.setBackground(table.getSelectionBackground());
        return action;
        
    }
}
