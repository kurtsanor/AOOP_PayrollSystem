/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import Domains.LeaveBalance;
import Domains.LeaveRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import Model.Employee;
import Model.LeaveCreditsDAO;
import Model.LeaveRequestValidator;
import java.sql.SQLException;

/**
 *
 * @author keith
 */
public class JframeLeave extends javax.swing.JFrame {

    /**
     * Creates new form JframeLeave
     */
    private Employee loggedEmployee;
    private DefaultTableModel leaveTbl;
    private LeaveBalance personalLeaveCredits;
    private SimpleDateFormat sqlDateFormat;
    private SimpleDateFormat simpleFormat;
    public JframeLeave(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
        this.sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.simpleFormat = new SimpleDateFormat("MMM dd, yyyy");
        initComponents();
        this.leaveTbl = (DefaultTableModel) jTableLeaveHistory.getModel();
        jPanel3.setVisible(false);
        setExtendedState(MAXIMIZED_BOTH);
        loadPersonalLeaves();
        loadPersonalLeaveCredits();
        syncLeaveCreditsToLabels();
        hideErrorLables();
        configureDateChoosers();
    }
       
    private void configureDateChoosers () {
        ((JTextField) jDateChooserStartDate.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) jDateChooserEndDate.getDateEditor().getUiComponent()).setEditable(false);
    }
    
    private void syncLeaveCreditsToLabels () {
        jLabelVacationLeaveBalance.setText(String.valueOf(personalLeaveCredits.getVacationLeaveCredits()));
        jLabelMedicalLeaveBalance.setText(String.valueOf(personalLeaveCredits.getMedicalLeaveCredits()));
        jLabelPersonalLeaveBalance.setText(String.valueOf(personalLeaveCredits.getPersonalLeaveCredits()));     
    }
    
    private void loadPersonalLeaveCredits () {
        try {
            LeaveCreditsDAO dao = new LeaveCreditsDAO();
            this.personalLeaveCredits = dao.getLeaveCreditsByEmpID(loggedEmployee.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private List<LeaveRequest> fetchPersonalLeaves () {
        return loggedEmployee.viewPersonalLeaves(loggedEmployee.getID());
    }
    
    private void loadPersonalLeaves () {
        List <LeaveRequest> leaveRecords = fetchPersonalLeaves();
        populateLeaveTable(leaveRecords);        
    }
    
    private Object [] createLeaveRowData (LeaveRequest request) {
        return new Object [] {
            request.getLeaveID(),
            request.getLeaveType(),
            simpleFormat.format(Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant())),
            simpleFormat.format(Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())),
            request.getStatus(),
            request.getSubmittedDate(),
            request.getProcessedDate(),
            request.getRemarks()
        };
    }
    
    private void populateLeaveTable (List<LeaveRequest> requests) {
        clearTable();
        for (LeaveRequest request: requests) {
            leaveTbl.addRow(createLeaveRowData(request));
        }       
    }
    
    private void clearTable () {
        leaveTbl.setRowCount(0);
    }
    
    private void hideErrorLables () {
        jLabelLeaveTypeError.setVisible(false);
        jLabelStartDateError.setVisible(false);
        jLabelEndDateError.setVisible(false);
        jLabelRemarksError.setVisible(false);
    }
    
    private void clearInputFields () {
        jDateChooserStartDate.setDate(null);
        jDateChooserEndDate.setDate(null);
        jTextFieldRemarks.setText(null);
    }
    
    private LeaveRequest setupLeaveRequest () {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        Date start = jDateChooserStartDate.getDate();       
        LocalDate startDate = start != null ? LocalDate.parse(sqlDateFormat.format(start)) : null;
        Date end = jDateChooserEndDate.getDate();
        LocalDate endDate = end != null ? LocalDate.parse(sqlDateFormat.format(end)) : null;
        
        return new LeaveRequest(
            loggedEmployee.getID(),
            jComboBoxLeaveType.getSelectedItem().toString().trim(),
            startDate,
            endDate,
            "Pending",
            dateTimeNow,
            jTextFieldRemarks.getText());
    }
    
    private void submitLeave () {
        if (validLeaveRequest()) {
            LeaveRequest leaveRequest = setupLeaveRequest();       
            boolean requestSuccessful = loggedEmployee.requestForLeave(leaveRequest);
            showLeaveSubmissionResult(requestSuccessful);
            refreshLeaveTable();
            clearInputFields();
            jPanel3.setVisible(false);
        }           
    }
    
    private void refreshLeaveTable () {
        clearTable();
        loadPersonalLeaves();
    }
    
    private void showLeaveSubmissionResult (boolean submitted) {
        String message = submitted ? "Leave request has been submitted" : "Failed to submit leave request";
        int messageType = submitted ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(this, message, submitted? "Success": "Failed", messageType);
    }
    
    private boolean validLeaveRequest () {
        hideErrorLables();
        boolean validRequest = true;
        String errorMessage;
        
        Date unparsedStartDate = jDateChooserStartDate.getDate();       
        LocalDate startDate = unparsedStartDate != null ? LocalDate.parse(sqlDateFormat.format(unparsedStartDate)) : null;
        Date unparsedEndDate = jDateChooserEndDate.getDate();
        LocalDate endDate = unparsedEndDate != null ? LocalDate.parse(sqlDateFormat.format(unparsedEndDate)) : null;
        String leaveType = jComboBoxLeaveType.getSelectedItem().toString();
        
        errorMessage = LeaveRequestValidator.validateStartDateWithMessage(startDate, endDate);
        setStartDateErorrMessage(errorMessage);
        if (!errorMessage.isBlank()) validRequest = false;
        
        errorMessage = LeaveRequestValidator.validateEndDateWithMessage(startDate, endDate);
        setEndDateErorrMessage(errorMessage);
        if (!errorMessage.isBlank()) validRequest = false;
        
        errorMessage = LeaveRequestValidator.validateRemarksWithMessage(jTextFieldRemarks.getText());
        setRemarksErorrMessage(errorMessage);
        if (!errorMessage.isBlank()) validRequest = false;
        
        errorMessage = LeaveRequestValidator.validateLeaveTypeWithMessage(personalLeaveCredits, leaveType, startDate, endDate);
        setLeaveTypeErorrMessage(errorMessage);
        if (!errorMessage.isBlank()) validRequest = false;
        
        return validRequest;
    }
    
    private void setStartDateErorrMessage (String message) {
        jLabelStartDateError.setText(message);
        jLabelStartDateError.setVisible(true);
    }
    
    private void setEndDateErorrMessage (String message) {
        jLabelEndDateError.setText(message);
        jLabelEndDateError.setVisible(true);
    }
    
    private void setRemarksErorrMessage (String message) {
        jLabelRemarksError.setText(message);
        jLabelRemarksError.setVisible(true);
    }
    
    private void setLeaveTypeErorrMessage (String message) {
        jLabelLeaveTypeError.setText(message);
        jLabelLeaveTypeError.setVisible(true);
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
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabelVacationLeaveBalance = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabelMedicalLeaveBalance = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabelPersonalLeaveBalance = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jComboBoxLeaveType = new javax.swing.JComboBox<>();
        jTextFieldRemarks = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButtonDiscard = new javax.swing.JButton();
        jButtonSubmit = new javax.swing.JButton();
        jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
        jDateChooserEndDate = new com.toedter.calendar.JDateChooser();
        jLabelLeaveTypeError = new javax.swing.JLabel();
        jLabelStartDateError = new javax.swing.JLabel();
        jLabelEndDateError = new javax.swing.JLabel();
        jLabelRemarksError = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLeaveHistory = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jButtonAddLeave = new javax.swing.JButton();
        jButtonBackToDashboard = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(86, 98, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(552, 90));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBackground(new java.awt.Color(86, 98, 106));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabelVacationLeaveBalance.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelVacationLeaveBalance.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVacationLeaveBalance.setText("21");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel6.add(jLabelVacationLeaveBalance, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Available");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel6.add(jLabel2, gridBagConstraints);

        jLabel3.setBackground(new java.awt.Color(51, 153, 0));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Vacation Leave");
        jLabel3.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel6.add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel6, gridBagConstraints);

        jPanel7.setBackground(new java.awt.Color(86, 98, 106));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabelMedicalLeaveBalance.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelMedicalLeaveBalance.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMedicalLeaveBalance.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel7.add(jLabelMedicalLeaveBalance, gridBagConstraints);

        jLabel5.setBackground(new java.awt.Color(204, 0, 102));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Medical Leave");
        jLabel5.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel7.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Available");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel7.add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel7, gridBagConstraints);

        jPanel8.setBackground(new java.awt.Color(86, 98, 106));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabelPersonalLeaveBalance.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelPersonalLeaveBalance.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPersonalLeaveBalance.setText("11");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel8.add(jLabelPersonalLeaveBalance, gridBagConstraints);

        jLabel8.setBackground(new java.awt.Color(204, 153, 0));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Personal Leave");
        jLabel8.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel8.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Available");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel8.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(86, 98, 106));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jComboBoxLeaveType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vacation", "Medical", "Personal" }));
        jComboBoxLeaveType.setPreferredSize(new java.awt.Dimension(100, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jComboBoxLeaveType, gridBagConstraints);

        jTextFieldRemarks.setPreferredSize(new java.awt.Dimension(100, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jTextFieldRemarks, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Leave Type*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Start Date*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jLabel11, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("End Date*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Remarks*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jLabel13, gridBagConstraints);

        jButtonDiscard.setBackground(new java.awt.Color(153, 0, 0));
        jButtonDiscard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDiscard.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDiscard.setText("Discard");
        jButtonDiscard.setPreferredSize(new java.awt.Dimension(90, 35));
        jButtonDiscard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDiscardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(9, 12, 9, 12);
        jPanel3.add(jButtonDiscard, gridBagConstraints);

        jButtonSubmit.setBackground(new java.awt.Color(0, 183, 229));
        jButtonSubmit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSubmit.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSubmit.setText("Submit");
        jButtonSubmit.setPreferredSize(new java.awt.Dimension(90, 35));
        jButtonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 12, 9, 12);
        jPanel3.add(jButtonSubmit, gridBagConstraints);

        jDateChooserStartDate.setDateFormatString("MMM dd, yyyy");
        jDateChooserStartDate.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jDateChooserStartDate, gridBagConstraints);

        jDateChooserEndDate.setDateFormatString("MMM dd, yyyy");
        jDateChooserEndDate.setPreferredSize(new java.awt.Dimension(88, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 3, 12);
        jPanel3.add(jDateChooserEndDate, gridBagConstraints);

        jLabelLeaveTypeError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelLeaveTypeError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel3.add(jLabelLeaveTypeError, gridBagConstraints);

        jLabelStartDateError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelStartDateError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel3.add(jLabelStartDateError, gridBagConstraints);

        jLabelEndDateError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelEndDateError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        jPanel3.add(jLabelEndDateError, gridBagConstraints);

        jLabelRemarksError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelRemarksError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        jPanel3.add(jLabelRemarksError, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(86, 98, 106));
        jPanel4.setPreferredSize(new java.awt.Dimension(552, 150));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jTableLeaveHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Leave ID", "Leave Type", "Start Date", "End Date", "Status", "Submitted Date", "Processed Date", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLeaveHistory.setSelectionBackground(new java.awt.Color(0, 183, 229));
        jScrollPane1.setViewportView(jTableLeaveHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel5.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 13, 13, 13);
        jPanel4.add(jPanel5, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 183, 229));
        jLabel14.setText("My Leave History");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel4.add(jLabel14, gridBagConstraints);

        jButtonAddLeave.setBackground(new java.awt.Color(0, 183, 229));
        jButtonAddLeave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/add (3).png"))); // NOI18N
        jButtonAddLeave.setPreferredSize(new java.awt.Dimension(72, 25));
        jButtonAddLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLeaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel4.add(jButtonAddLeave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel4, gridBagConstraints);

        jButtonBackToDashboard.setBackground(new java.awt.Color(0, 183, 229));
        jButtonBackToDashboard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonBackToDashboard.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBackToDashboard.setText("Back To Dashboard");
        jButtonBackToDashboard.setPreferredSize(new java.awt.Dimension(131, 35));
        jButtonBackToDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackToDashboardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonBackToDashboard, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBackToDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackToDashboardActionPerformed
        this.dispose();
        new JframeDashboard(loggedEmployee.getID()).setVisible(true);
    }//GEN-LAST:event_jButtonBackToDashboardActionPerformed

    private void jButtonDiscardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDiscardActionPerformed
        jPanel3.setVisible(false);
        clearInputFields();
        hideErrorLables();
    }//GEN-LAST:event_jButtonDiscardActionPerformed

    private void jButtonAddLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLeaveActionPerformed
        jPanel3.setVisible(true);
    }//GEN-LAST:event_jButtonAddLeaveActionPerformed

    private void jButtonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitActionPerformed
         submitLeave();
    }//GEN-LAST:event_jButtonSubmitActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddLeave;
    private javax.swing.JButton jButtonBackToDashboard;
    private javax.swing.JButton jButtonDiscard;
    private javax.swing.JButton jButtonSubmit;
    private javax.swing.JComboBox<String> jComboBoxLeaveType;
    private com.toedter.calendar.JDateChooser jDateChooserEndDate;
    private com.toedter.calendar.JDateChooser jDateChooserStartDate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelEndDateError;
    private javax.swing.JLabel jLabelLeaveTypeError;
    private javax.swing.JLabel jLabelMedicalLeaveBalance;
    private javax.swing.JLabel jLabelPersonalLeaveBalance;
    private javax.swing.JLabel jLabelRemarksError;
    private javax.swing.JLabel jLabelStartDateError;
    private javax.swing.JLabel jLabelVacationLeaveBalance;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLeaveHistory;
    private javax.swing.JTextField jTextFieldRemarks;
    // End of variables declaration//GEN-END:variables
}
