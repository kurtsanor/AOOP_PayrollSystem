/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorphpayrollv2.CustomTable;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author keith
 */
public class TableActionCellEditorV2 extends DefaultCellEditor {
    private TableActionEventV2 event;
    public TableActionCellEditorV2 (TableActionEventV2 event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelActionV2 action = new PanelActionV2();
        action.initEvent(event, row);
        action.setBackground(table.getSelectionBackground());
        return action;
        
    }
}
