/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import Domains.AttendanceRecord;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import Model.AttendanceDAO;
import Model.Employee;
import Model.HR;
import Model.HoursCalculator;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author keith
 */
public class JframeAttendanceManagement extends javax.swing.JFrame {

    /**
     * Creates new form JframeAttendanceManagement
     */
    private Employee loggedEmployee;
    private HR hrEmployee;
    private SimpleDateFormat sqlDateFormat;
    private SimpleDateFormat simpleFormat;
    private DefaultTableModel attendanceTbl;
    public JframeAttendanceManagement(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
        this.sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.simpleFormat = new SimpleDateFormat("MMM dd, yyyy");
        initComponents();
        attendanceTbl = (DefaultTableModel) jTableAttendance.getModel();
        setExtendedState(MAXIMIZED_BOTH);
        initHrUser();
        populateComboBoxWithEmployees();
        hideErrorLabels();
    }
    
    private void initHrUser () {
        if (loggedEmployee instanceof HR) {
            hrEmployee = new HR(loggedEmployee);
        }
    }
    
    private List<Employee> fetchEmployees () {
        return hrEmployee.loadEmployees();
    }
    
    private void populateComboBoxWithEmployees () {
        List<Employee> employees = fetchEmployees();
        
        for (Employee employee: employees) {
            jComboBoxEmployeeList.addItem(employee.getID() + " - " + employee.getFirstName() + " " + employee.getLastName());
        }
    }
    
    private LocalDate getStartDate () {
        Date chosenDate = jDateChooserStartDate.getDate();
        return chosenDate != null ? LocalDate.parse(sqlDateFormat.format(chosenDate)) : null;
        
    }
    
    private LocalDate getEndDate () {
        Date chosenDate = jDateChooserEndDate.getDate();
        return chosenDate != null? LocalDate.parse(sqlDateFormat.format(chosenDate)): null;
    }
    
    private int getSelectedEmployee () {
        int employeeID = Integer.parseInt(jComboBoxEmployeeList.getSelectedItem().toString().split(" ") [0]);
        return employeeID;
    }
    
    private List<AttendanceRecord> fetchAttendanceRecords (int employeeID, LocalDate start, LocalDate end) {
        try {
            AttendanceDAO dao = new AttendanceDAO();
            return dao.getAttendanceByIdAndPeriod(employeeID, start, end);
        } catch (SQLException ex) {
            Logger.getLogger(JframeAttendanceManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private Object [] createTableRowData (AttendanceRecord record) {
        LocalTime in = record.getTimeIn();
        LocalTime out = record.getTimeOut();
        return new Object [] { 
            record.getEmployeeID(),
            simpleFormat.format(Date.from(record.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant())),
            in,
            out,
            HoursCalculator.calculateDailyHours(in, out)
        };
    }
    
    private void populateTable (List<AttendanceRecord> records) {            
        for (AttendanceRecord record: records) {
            attendanceTbl.addRow(createTableRowData(record));
        }
    }
    
    private void loadTable () {
        LocalDate start = getStartDate();
        LocalDate end = getEndDate();
        hideErrorLabels();
        attendanceTbl.setRowCount(0);
        
        if (!validDates(start, end)) {
            showInvalidDatesError(start, end);
            return;
        }
        int employeeID = getSelectedEmployee();
        List <AttendanceRecord> records = fetchAttendanceRecords(employeeID, start, end);  
        if (!records.isEmpty()) {
            populateTable(records);
        } else {
            jLabelNoResultError.setVisible(true);
        }   
    }
    
    public boolean validDates (LocalDate start, LocalDate end) {
        return start != null && 
                   end != null &&
                   (!start.isAfter(end)) &&
                   (!end.isBefore(start));
    }
    
    public void showInvalidDatesError (LocalDate start, LocalDate end) {
        if (start == null) {
            jLabelStartDateError.setText("Required");
            jLabelStartDateError.setVisible(true);
        }
        if (end == null) {
            jLabelEndDateError.setText("Required");
            jLabelEndDateError.setVisible(true);
        }
        if (start != null && end != null && start.isAfter(end)) {
            jLabelStartDateError.setText("Invalid Order");
            jLabelStartDateError.setVisible(true);
        }
        if (end != null && start != null && end.isBefore(start)) {
            jLabelEndDateError.setText("Invalid Order");
            jLabelEndDateError.setVisible(true);
        }
    }
    
    private void hideErrorLabels () {
        jLabelStartDateError.setVisible(false);
        jLabelEndDateError.setVisible(false);
        jLabelNoResultError.setVisible(false);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAttendance = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxEmployeeList = new javax.swing.JComboBox<>();
        jDateChooserEndDate = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButtonView = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabelStartDateError = new javax.swing.JLabel();
        jLabelEndDateError = new javax.swing.JLabel();
        jLabelNoResultError = new javax.swing.JLabel();

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
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonBackToDashboard, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jTableAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "Date", "Time In", "Time out", "Total Hours"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAttendance.setSelectionBackground(new java.awt.Color(0, 183, 229));
        jScrollPane1.setViewportView(jTableAttendance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        jPanel3.add(jScrollPane1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jDateChooserStartDate.setDateFormatString("MMM dd, yyyy");
        jDateChooserStartDate.setPreferredSize(new java.awt.Dimension(130, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 4, 0, 0);
        jPanel2.add(jDateChooserStartDate, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 183, 229));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TIMESHEET");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 37);
        jPanel2.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(14, 2, 0, 0);
        jPanel2.add(jComboBoxEmployeeList, gridBagConstraints);

        jDateChooserEndDate.setDateFormatString("MMM dd, yyyy");
        jDateChooserEndDate.setPreferredSize(new java.awt.Dimension(130, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 2, 0, 0);
        jPanel2.add(jDateChooserEndDate, gridBagConstraints);

        jLabel3.setText("Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 2, 0, 0);
        jPanel2.add(jLabel4, gridBagConstraints);

        jButtonView.setText("View");
        jButtonView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 2, 0, 7);
        jPanel2.add(jButtonView, gridBagConstraints);

        jLabel1.setText("Select Employee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(14, 10, 0, 0);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabelStartDateError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelStartDateError.setText("Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabelStartDateError, gridBagConstraints);

        jLabelEndDateError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelEndDateError.setText("Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabelEndDateError, gridBagConstraints);

        jLabelNoResultError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelNoResultError.setText("No results found");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 37);
        jPanel2.add(jLabelNoResultError, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanel3.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(27, 27, 27, 27);
        jPanel1.add(jPanel3, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBackToDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToDashboardActionPerformed
        this.dispose();
        new JframeDashboard(loggedEmployee.getID()).setVisible(true);
    }//GEN-LAST:event_jButtonBackToDashboardActionPerformed

    private void jButtonViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewActionPerformed
        loadTable();
    }//GEN-LAST:event_jButtonViewActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBackToDashboard;
    private javax.swing.JButton jButtonView;
    private javax.swing.JComboBox<String> jComboBoxEmployeeList;
    private com.toedter.calendar.JDateChooser jDateChooserEndDate;
    private com.toedter.calendar.JDateChooser jDateChooserStartDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelEndDateError;
    private javax.swing.JLabel jLabelNoResultError;
    private javax.swing.JLabel jLabelStartDateError;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableAttendance;
    // End of variables declaration//GEN-END:variables
}
