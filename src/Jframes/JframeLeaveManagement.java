/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import javax.swing.table.TableCellEditor;
import CustomTable.TableActionCellEditorV2;
import CustomTable.TableActionCellRendererV2;
import CustomTable.TableActionEventV2;
import Domains.LeaveBalance;
import Domains.LeaveRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import oopClasses.DatabaseConnection;
import oopClasses.Employee;
import oopClasses.HR;
import oopClasses.LeaveCreditsDatabase;

/**
 *
 * @author keith
 */
public class JframeLeaveManagement extends javax.swing.JFrame {

    /**
     * Creates new form JframeLeaveManagement
     */
    private Employee loggedEmployee;
    private LeaveCreditsDatabase leaveCreditsDB;
    private HR hrEmployee;
    private DefaultTableModel leaveTbl;
    public JframeLeaveManagement(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
        this.leaveCreditsDB = new LeaveCreditsDatabase(DatabaseConnection.Connect());
        initComponents();
        this.leaveTbl = (DefaultTableModel) jTableLeaveTable.getModel();
        setExtendedState(MAXIMIZED_BOTH);
        initTable();
        initHrUser(loggedEmployee);
        loadLeaveTable();
    }
    
    private void initHrUser (Employee loggedEmployee) {
        if (loggedEmployee instanceof HR) {
            this.hrEmployee = new HR(loggedEmployee);
        }
    }
    
    private void deductDurationToCredits (int employeeID, int leaveDuration, String leaveType) {
        LeaveBalance leaveBalance = leaveCreditsDB.getLeaveCreditsByEmpID(employeeID);
        switch (leaveType) {
            case "Vacation":
                leaveBalance = leaveBalance.deductVacationCredits(leaveDuration);
                break;
            case "Medical":
                leaveBalance = leaveBalance.deductMedicalCredits(leaveDuration);
                break;
            case "Personal":
                leaveBalance = leaveBalance.deductPersonalCredits(leaveDuration);
                break;
        }
        boolean updated = leaveCreditsDB.updateLeaveCreditsByEmpID(employeeID, leaveBalance);
        System.out.println("updated = " + updated);
    }
    
    private void updateLeaveRequest (int leaveID, String status) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        boolean updated = hrEmployee.updateLeaveStatus(leaveID, status, dateTimeNow);
        if (updated) {
            JOptionPane.showMessageDialog(this, "Leave status successfully updated to " + status, "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "There was a problem updating the request", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean hasEnoughCredits (int employeeID, int leaveDuration, String leaveType) {
        LeaveBalance leaveBalance = leaveCreditsDB.getLeaveCreditsByEmpID(employeeID);
        
        switch (leaveType) {
            case "Vacation":
                return leaveBalance.getVacationLeaveCredits() >= leaveDuration;
            case "Medical":
                return leaveBalance.getMedicalLeaveCredits() >= leaveDuration;
            case "Personal":
                return leaveBalance.getPersonalLeaveCredits() >= leaveDuration;
        }      
        return false; // should be unreachable
    }
    
    
    private void initTable () {
        TableActionEventV2 event = new TableActionEventV2() {
            @Override
            public void onApprove(int row) {
                String status = jTableLeaveTable.getValueAt(row, 6).toString();
                if (!"Pending".equals(status)) {
                    JOptionPane.showMessageDialog(JframeLeaveManagement.this, "This request has already been processed", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int choice = JOptionPane.showConfirmDialog(JframeLeaveManagement.this, "Do you want to approve this leave request?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    int leaveID = (int) jTableLeaveTable.getValueAt(row, 0);
                    int employeeID = (int) jTableLeaveTable.getValueAt(row, 1);
                    LocalDate startDate = (LocalDate) jTableLeaveTable.getValueAt(row, 3);
                    LocalDate endDate = (LocalDate) jTableLeaveTable.getValueAt(row, 4);
                    String leaveType = jTableLeaveTable.getValueAt(row, 2).toString();
                    int leaveDuration = startDate.until(endDate).getDays();
                                                     
                    if (hasEnoughCredits(employeeID, leaveDuration, leaveType)) {
                        updateLeaveRequest(leaveID, "Approved");
                        deductDurationToCredits(employeeID, leaveDuration, leaveType);
                        clearLeaveTable();
                        loadLeaveTable();
                        jTableLeaveTable.setRowSelectionInterval(row, row);

                    } else {
                        JOptionPane.showMessageDialog(JframeLeaveManagement.this, "Employee has insufficient leave credits", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }   
            }
                

            @Override
            public void onDeny(int row) {
                String status = jTableLeaveTable.getValueAt(row, 6).toString();
                if (!"Pending".equals(status)) {
                    JOptionPane.showMessageDialog(JframeLeaveManagement.this, "This request has already been processed", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                 int choice = JOptionPane.showConfirmDialog(JframeLeaveManagement.this, "Do you want to deny this leave request?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (choice == JOptionPane.YES_OPTION) {
                     int leaveID = (int) jTableLeaveTable.getValueAt(row, 0);
                    updateLeaveRequest(leaveID, "Denied");
                    clearLeaveTable();
                    loadLeaveTable();
                    jTableLeaveTable.setRowSelectionInterval(row, row);
                 }             
            }
        };
            jTableLeaveTable.getColumnModel().getColumn(9).setCellRenderer(new TableActionCellRendererV2());
            jTableLeaveTable.getColumnModel().getColumn(9).setCellEditor(new TableActionCellEditorV2(event));
    }
    
    
    private void loadLeaveTable () {
        List <LeaveRequest> leaveRequests = hrEmployee.loadEmployeeLeaves();
        
        for (LeaveRequest request: leaveRequests) {
            leaveTbl.addRow(new Object [] {
            request.getLeaveID(),
            request.getEmployeeID(),
            request.getLeaveType(),
            request.getStartDate(),
            request.getEndDate(),
            request.getRemarks(),
            request.getStatus(),
            request.getSubmittedDate(),
            request.getProcessedDate()});
        }
    }
    
    private void clearLeaveTable () {       
        leaveTbl.setRowCount(0);
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

        jPanel1 = new javax.swing.JPanel();
        jButtonBackToDashboard = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLeaveTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(86, 98, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonBackToDashboard.setBackground(new java.awt.Color(0, 183, 229));
        jButtonBackToDashboard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonBackToDashboard.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBackToDashboard.setText("Back To Dashboard");
        jButtonBackToDashboard.setPreferredSize(new java.awt.Dimension(75, 35));
        jButtonBackToDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToDashboardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonBackToDashboard, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(86, 98, 106));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(86, 98, 106));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 183, 229));
        jLabel1.setText("LEAVE REQUESTS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        jPanel3.add(jPanel2, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jTableLeaveTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Leave ID", "Employee ID", "Leave Type", "Start Date", "End Date", "Remarks", "Status", "Submitted Date", "Processed Date", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLeaveTable.setRowHeight(30);
        jTableLeaveTable.setSelectionBackground(new java.awt.Color(0, 183, 229));
        jScrollPane1.setViewportView(jTableLeaveTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanel4.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(38, 38, 38, 38);
        jPanel1.add(jPanel3, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBackToDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToDashboardActionPerformed
        this.dispose();
        new JframeDashboard(loggedEmployee.getID()).setVisible(true);
    }//GEN-LAST:event_jButtonBackToDashboardActionPerformed
        

    
    
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBackToDashboard;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLeaveTable;
    // End of variables declaration//GEN-END:variables
}
