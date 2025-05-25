/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import CustomTable.TableActionCellEditorV2;
import CustomTable.TableActionCellRendererV2;
import CustomTable.TableActionEventV2;
import Domains.LeaveRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Model.Employee;
import Model.HR;
import Model.LeaveService;

/**
 *
 * @author keith
 */
public class JframeLeaveManagement extends javax.swing.JFrame {

    /**
     * Creates new form JframeLeaveManagement
     */
    private Employee loggedEmployee;
    private HR hrEmployee;
    private DefaultTableModel leaveTbl;
    private SimpleDateFormat simpleFormat;
    private SimpleDateFormat sqlDateFormat;
    public JframeLeaveManagement(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
        this.simpleFormat = new SimpleDateFormat("MMM dd, yyyy");
        this.sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initComponents();
        this.leaveTbl = (DefaultTableModel) jTableLeaveTable.getModel();
        setExtendedState(MAXIMIZED_BOTH);
        initTableEvents();
        initHrUser(loggedEmployee);
        loadLeaveTable();
    }
    
    private void initHrUser (Employee loggedEmployee) {
        if (loggedEmployee instanceof HR) {
            this.hrEmployee = new HR(loggedEmployee);
        }
    }
    
    private void updateLeaveCredits (int employeeID, String leaveType, int leaveDuration) {
        LeaveService service = new LeaveService();
        service.updateLeaveCredits(employeeID, leaveType, leaveDuration);
    }
    
    private void updateLeaveStatus (int leaveID, String status) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        boolean updated = hrEmployee.updateLeaveStatus(leaveID, status, dateTimeNow);
        showUpdatedLeaveStatusResult(updated, status);                          
    }
    
    private void showUpdatedLeaveStatusResult (boolean updated, String status) {
        String message = updated ? "Leave status successfully updated to " + status : " Failed to update leave status";
        int messageType = updated ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(this, message, updated ? "Success" : "Error", messageType);
    }
    
    private boolean confirmAction (String action) {
        String message = "Do you want to update the status to " + action +"?";
        return JOptionPane.showConfirmDialog(this, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
       
    private boolean hasEnoughCredits (int employeeID, int leaveDuration, String leaveType) {
        LeaveService service = new LeaveService();
        return service.hasEnoughCredits(employeeID, leaveDuration, leaveType);
    }
    
    private void showInsufficientLeaveCreditsWarning (String leaveType) {
        JOptionPane.showMessageDialog(JframeLeaveManagement.this, "Employee has insufficient " + leaveType + " credits", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private TableActionEventV2 createTableActionEvents () {
        return new TableActionEventV2() {
            @Override
            public void onApprove(int row) {
                String status = getStatusFromRow(row);
                if (alreadyProcessed(status)) return;
                                                          
                LocalDate startDate = getStartDateFromRow(row);
                LocalDate endDate = getEndDateFromRow(row);
                String leaveType = getLeaveTypeFromRow(row);
                int leaveDuration = getLeaveDuration(startDate, endDate);
                int leaveID = getLeaveIdFromRow(row);
                int employeeID = getEmployeeIdFromRow(row);
                
                if (!hasEnoughCredits(employeeID, leaveDuration, leaveType)) {
                    showInsufficientLeaveCreditsWarning(leaveType);
                    return;
                }
                
                String action = "Approved";
                if (confirmAction(action)) {
                    updateLeaveStatus(leaveID, action);
                    updateLeaveCredits(employeeID, leaveType, leaveDuration);
                    refreshLeaveTable();
                    jTableLeaveTable.setRowSelectionInterval(row, row);
                }              
            }

            @Override
            public void onDeny(int row) {
                String status = getStatusFromRow(row);
                if (alreadyProcessed(status)) return;
                
                int leaveID = getLeaveIdFromRow(row);
                String action = "Denied";
                if (confirmAction(action)) {
                    updateLeaveStatus(leaveID, action);
                    refreshLeaveTable();
                    jTableLeaveTable.setRowSelectionInterval(row, row);
                }              
            }
        };
    }
    
    private void setUpActionColumn (TableActionEventV2 event) {
        jTableLeaveTable.getColumnModel().getColumn(9).setCellRenderer(new TableActionCellRendererV2());
        jTableLeaveTable.getColumnModel().getColumn(9).setCellEditor(new TableActionCellEditorV2(event));
    }
    
    private void initTableEvents () {
        TableActionEventV2 events = createTableActionEvents();
        setUpActionColumn(events);
    }
    
    private void showAlreadyProcessedMessage() {
        JOptionPane.showMessageDialog(JframeLeaveManagement.this, "This request has already been processed", "Invalid", JOptionPane.WARNING_MESSAGE);
    }
    
    private boolean alreadyProcessed (String status) {
        if (!"Pending".equals(status)) {
            showAlreadyProcessedMessage();
            return true;
        }
        return false;
    }
    
    private int getEmployeeIdFromRow (int row) {
        return (int) jTableLeaveTable.getValueAt(row, 1);
    }
    
    private String getStatusFromRow (int row) {
        return jTableLeaveTable.getValueAt(row, 6).toString();
    }
    
    private int getLeaveIdFromRow (int row) {
        return (int) jTableLeaveTable.getValueAt(row, 0);
    }
    
    private LocalDate getStartDateFromRow (int row) {
        try {
            return LocalDate.parse(sqlDateFormat.format(simpleFormat.parse(jTableLeaveTable.getValueAt(row, 3).toString())));
        } catch (ParseException ex) {
            Logger.getLogger(JframeLeaveManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private LocalDate getEndDateFromRow (int row) {
        try {
            return LocalDate.parse(sqlDateFormat.format(simpleFormat.parse(jTableLeaveTable.getValueAt(row, 4).toString())));
        } catch (ParseException ex) {
            Logger.getLogger(JframeLeaveManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private int getLeaveDuration (LocalDate startDate, LocalDate endDate) {
        return LeaveService.getLeaveDuration(startDate, endDate);
    }
    
    private String getLeaveTypeFromRow (int row) {
        return jTableLeaveTable.getValueAt(row, 2).toString();
    }
    
    private void refreshLeaveTable () {
        clearLeaveTable();
        loadLeaveTable();
    }
     
    private void loadLeaveTable () {
        List <LeaveRequest> leaveRequests = hrEmployee.loadEmployeeLeaves();
        populateTableWithLeaveRequests(leaveRequests);
    }
    
    private Object [] createLeaveRowData(LeaveRequest request) {
        return new Object [] {
            request.getLeaveID(),
            request.getEmployeeID(),
            request.getLeaveType(),
            simpleFormat.format(Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant())),
            simpleFormat.format(Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())),
            request.getRemarks(),
            request.getStatus(),
            request.getSubmittedDate(),
            request.getProcessedDate()
        };
    }
    
    private void populateTableWithLeaveRequests(List<LeaveRequest> leaveRequests) {
        clearLeaveTable();      
        for (LeaveRequest request: leaveRequests) {
            leaveTbl.addRow(createLeaveRowData(request));
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
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
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
