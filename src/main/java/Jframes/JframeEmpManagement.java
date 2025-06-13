/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import javax.swing.table.DefaultTableModel;
import CustomTable.TableActionCellEditor;
import CustomTable.TableActionCellRenderer;
import CustomTable.TableActionEvent;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import Model.Employee;
import Model.HR;

/**
 *
 * @author keith
 */
public class JframeEmpManagement extends javax.swing.JFrame {

    /**
     * Creates new form JframeEmpManagement
     */
    private DefaultTableModel employeeTbl;
    private Employee loggedEmployee;
    private HR hrEmployee;
    public static JWindow overlay;
    public JframeEmpManagement(Employee loggedEmployee) {
        overlay = new JWindow();
        this.loggedEmployee = loggedEmployee;
        initComponents();
        employeeTbl = (DefaultTableModel) jTableEmployeeList.getModel();
        setExtendedState(MAXIMIZED_BOTH);
        initHrUser(loggedEmployee);
        initTableEvents();
        loadEmployeeTable();            
    }
    
    // displays a dim overlay to darken the background
    private void showOverlay () {
        overlay.setSize(this.getSize());        
        overlay.setLocationRelativeTo(this);
        overlay.setBackground(new Color(0, 0, 0, 200));
        overlay.setVisible(true);
    }
    
    private void searchEmployee (int employeeID) {
        if (hrEmployee != null) {
            Employee employee = hrEmployee.viewEmployeeByID(employeeID);
            if (employee != null) {
                populateTableWithSearchedEmployee(employee);
                clearTableSelection();
            } else {
                JOptionPane.showMessageDialog(this, "Employee ID does not exist. Refreshing table", "No id found", JOptionPane.ERROR_MESSAGE);
                refreshTable();
            }
            
        }
    }
    
    private void populateTableWithSearchedEmployee (Employee employee) {
        clearTable();
        employeeTbl.addRow(createEmployeeData(employee));
    }
    
    private List<Employee> fetchEmployees () {
        return hrEmployee != null ? hrEmployee.loadEmployees() : Collections.emptyList();
    }
    
    private Object [] createEmployeeData (Employee employee) {
        return new Object [] {
                   employee.getID(),   
                   employee.getFirstName(),
                   employee.getLastName() 
        };
    }
    
    private void loadEmployeeTable () {
        List <Employee> employees = fetchEmployees();
        populateTableWithEmployees(employees);
    }
    
    private void populateTableWithEmployees (List <Employee> employees) {
        clearTable();
        for (Employee employee: employees) {
            employeeTbl.addRow(createEmployeeData(employee));
        }
    }
    
    private void clearTable () {
        employeeTbl.setRowCount(0);
    }
    
    private void clearTableSelection () {
        jTableEmployeeList.clearSelection();
        if (jTableEmployeeList.getCellEditor() != null) {
            jTableEmployeeList.getCellEditor().stopCellEditing();
        }
    }
      
    // creates an HR object if employee is instance of HR class
    private void initHrUser (Employee loggedEmployee) {
        if (loggedEmployee instanceof HR) {
            // pass the info of the current employee to the hr object
            this.hrEmployee = new HR(loggedEmployee);
        }
    }
    
    private void initTableEvents () {
        TableActionEvent events = createTableActionEvents();
        setupActionColumn(events);
    }
    
    private TableActionEvent createTableActionEvents () {
        return new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                int employeeID = getEmployeeIdFromRow(row);
                openEmployeeForm(employeeID);
            }

            @Override
            public void onDelete(int row) {
               String fullName = getFullNameFromRow(row);
               int employeeID = getEmployeeIdFromRow(row);
                handleEmployeeDeletion(employeeID, fullName);
            }
                
            @Override
            public void onView(int row) {
                int employeeID = getEmployeeIdFromRow(row);              
                openProfileView(employeeID);
            }
        };
    }
    
    private void openProfileView (int employeeID) {
        showOverlay();
        new JframeProfile(loggedEmployee, employeeID).setVisible(true);
    }
    
    private void setupActionColumn (TableActionEvent event) {
         jTableEmployeeList.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRenderer());
         jTableEmployeeList.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
    }
    
    private int getEmployeeIdFromRow (int row) {
        return (int)jTableEmployeeList.getValueAt(row, 0);
    }
    
    private String getFullNameFromRow (int row) {
        String firstName = jTableEmployeeList.getValueAt(row, 1).toString();
        String lastName = jTableEmployeeList.getValueAt(row, 2).toString();
        return firstName + " " + lastName;
    }
    
    private void openEmployeeForm (int employeeID) {
        this.dispose();
        new JframeEmployeeForm(loggedEmployee, employeeID).setVisible(true);
    }
    
    private boolean confirmDeletion (String fullName) {
        String message = "This operation will permanently delete " + fullName + "\n Do you wish to procceed?";
        return JOptionPane.showConfirmDialog(this, message, "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
    
    private void handleEmployeeDeletion (int employeeID, String fullName) {
        if (confirmDeletion(fullName)) {
            boolean deleted = hrEmployee.deleteEmployeeByID(employeeID);
            showDeletionResult(deleted);
            refreshTable();
        }            
    }
    
    private void showDeletionResult (boolean deleted) {
        String message = deleted ? "Employee record has been deleted" : "Failed to delete record";
        int messageType = deleted ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(this, message, deleted ? "Success" : "Error", messageType);
    }
    
    private void refreshTable () {
        clearTableSelection();
        clearTable();
        loadEmployeeTable();
    }
      
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelBg = new javax.swing.JPanel();
        jButtonDashboard = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelEmployeeList = new javax.swing.JLabel();
        jButtonAddNewEmployee = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableEmployeeList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelBg.setBackground(new java.awt.Color(86, 98, 106));
        jPanelBg.setLayout(new java.awt.GridBagLayout());

        jButtonDashboard.setBackground(new java.awt.Color(86, 98, 106));
        jButtonDashboard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDashboard.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back.png"))); // NOI18N
        jButtonDashboard.setText(" Back to Dashboard");
        jButtonDashboard.setBorder(null);
        jButtonDashboard.setFocusable(false);
        jButtonDashboard.setPreferredSize(new java.awt.Dimension(146, 35));
        jButtonDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDashboardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanelBg.add(jButtonDashboard, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(86, 98, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true));
        jPanel2.setPreferredSize(new java.awt.Dimension(586, 50));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelEmployeeList.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelEmployeeList.setForeground(new java.awt.Color(51, 51, 51));
        jLabelEmployeeList.setText("EMPLOYEE LIST");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jLabelEmployeeList, gridBagConstraints);

        jButtonAddNewEmployee.setBackground(new java.awt.Color(0, 183, 229));
        jButtonAddNewEmployee.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonAddNewEmployee.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAddNewEmployee.setText("Add New Employee");
        jButtonAddNewEmployee.setPreferredSize(new java.awt.Dimension(147, 35));
        jButtonAddNewEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddNewEmployeeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel2.add(jButtonAddNewEmployee, gridBagConstraints);

        jTextFieldSearch.setPreferredSize(new java.awt.Dimension(147, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel2.add(jTextFieldSearch, gridBagConstraints);

        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search-interface-symbol (1).png"))); // NOI18N
        jButtonSearch.setPreferredSize(new java.awt.Dimension(60, 35));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jButtonSearch, gridBagConstraints);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("jLabel1sd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jTableEmployeeList.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jTableEmployeeList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "First Name", "Last Name", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableEmployeeList.setRowHeight(30);
        jTableEmployeeList.setSelectionBackground(new java.awt.Color(0, 183, 229));
        jTableEmployeeList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTableEmployeeList);
        if (jTableEmployeeList.getColumnModel().getColumnCount() > 0) {
            jTableEmployeeList.getColumnModel().getColumn(3).setMinWidth(130);
            jTableEmployeeList.getColumnModel().getColumn(3).setMaxWidth(130);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanel3.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(38, 38, 38, 38);
        jPanelBg.add(jPanel1, gridBagConstraints);

        getContentPane().add(jPanelBg, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    
    private void jButtonDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDashboardActionPerformed
       this.dispose();
       new JframeDashboard(loggedEmployee.getID()).setVisible(true);
    }//GEN-LAST:event_jButtonDashboardActionPerformed

    private void jButtonAddNewEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddNewEmployeeActionPerformed
        dispose();
        new JframeEmployeeForm(loggedEmployee).setVisible(true);         
    }//GEN-LAST:event_jButtonAddNewEmployeeActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        
        try {
            clearTableSelection();
            int employeeID = Integer.parseInt(jTextFieldSearch.getText());
            searchEmployee(employeeID);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid id format. Refreshing table", "Invalid", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        }       
    }//GEN-LAST:event_jButtonSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddNewEmployee;
    private javax.swing.JButton jButtonDashboard;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelEmployeeList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelBg;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableEmployeeList;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
