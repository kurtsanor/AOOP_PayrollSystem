/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ui;

import Service.AttendanceProcessor;
import Model.Employee;
import Dao.EmployeeDAO;
import Model.Finance;
import Service.PayrollCalculator;
import Service.PayrollService;
import Model.EmployeeMonthlyHoursKey;
import Model.PayrollEntry;
import Model.PayrollSummary;
import Model.YearPeriod;
import Dao.AttendanceDAO;
import Util.PdfProcessor;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author keith
 */
public class JframePayroll extends javax.swing.JFrame {

    /**
     * Creates new form JframePayroll
     */
    private Employee loggedEmployee;
    private Finance financeEmployee;
    private Map<EmployeeMonthlyHoursKey, Double> workHoursMap;
    private List<Employee> employeeList;
    private DefaultTableModel tblModel;
    private YearPeriod latestGeneratedPeriod;
    public JframePayroll(Employee loggedEmployee) {      
        this.loggedEmployee = loggedEmployee;
        initComponents();
        this.tblModel = (DefaultTableModel) jTablePayroll.getModel();
        setExtendedState(MAXIMIZED_BOTH);
        initFinanceUser(loggedEmployee);
        populateEmployeeList();
        populateWorkHoursMap();
        jScrollPane2.setColumnHeaderView(null);
    }
      
    private void populateWorkHoursMap () {
        AttendanceDAO dao = new AttendanceDAO();
        AttendanceProcessor processor = new AttendanceProcessor(dao);
        workHoursMap = processor.mapMonthlyHoursOfEmployees();
    }
    
    private void initFinanceUser (Employee loggedEmployee) {
        if (loggedEmployee != null) {
            financeEmployee = new Finance(loggedEmployee);
        }
    }
    
    private YearPeriod getPeriod () {
        int year = jYearChooser.getYear();
        int month = jMonthChooser.getMonth()+1;
        return new YearPeriod(year, month);
    }
    
    private void populateEmployeeList () {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            employeeList = dao.getAllEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadPayrollTable () {
        if (financeEmployee == null) {
            JOptionPane.showMessageDialog(this, "Only finance employees are authorized to this feature", "Invalid Role", JOptionPane.WARNING_MESSAGE);
            return;
        }
        tblModel.setRowCount(0);
        YearPeriod period = getPeriod();
        latestGeneratedPeriod = period;
        List<PayrollEntry> payrollEntries = financeEmployee.generatePayroll(employeeList, workHoursMap, period);
        PayrollSummary summary = PayrollService.calculatePayrollSummary(payrollEntries);
        
        for (PayrollEntry payrollEntry: payrollEntries) {
            tblModel.addRow(createPayrollRowData(payrollEntry));
        }
        
        populateTotalTable(summary);
    }
    
    private Object [] createPayrollRowData (PayrollEntry payrollEntry) {
        return new Object [] {
                    payrollEntry.getEmployeeID(),
                    payrollEntry.getFullName(),
                    payrollEntry.getPosition(),
                    PayrollCalculator.formatAmount(payrollEntry.getGrossIncome()),
                    PayrollCalculator.formatAmount(payrollEntry.getSss()),
                    PayrollCalculator.formatAmount(payrollEntry.getPhilhealth()),
                    PayrollCalculator.formatAmount(payrollEntry.getPagibig()),
                    PayrollCalculator.formatAmount(payrollEntry.getWithholdingTax()),
                    PayrollCalculator.formatAmount(payrollEntry.getNetPay())
            };
    }
    
    private void populateTotalTable (PayrollSummary summary) {
        DefaultTableModel totalModel = (DefaultTableModel) jTableTotal.getModel();
        totalModel.setRowCount(0);
        totalModel.addRow(new Object []{"Total",
            "",
            "", 
            PayrollCalculator.formatAmount(summary.getTotalGrossIncome()),
            PayrollCalculator.formatAmount(summary.getTotalSSS()),
            PayrollCalculator.formatAmount(summary.getTotalPhilhealth()), 
            PayrollCalculator.formatAmount(summary.getTotalPagibig()), 
            PayrollCalculator.formatAmount(summary.getTotalTax()), 
            PayrollCalculator.formatAmount(summary.getTotalNetPay())
        });
    }
    
    private static void redirectToDirectory(File file) {
        try {
            if (Desktop.isDesktopSupported()) {         
            Desktop desktop = Desktop.getDesktop();
            File parentDirectory = file.getParentFile();
            desktop.open(parentDirectory);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private void showGenerateReportResult (File filePath) {
        JOptionPane.showMessageDialog(this, filePath != null? "Pdf successfully created at: " + filePath.getAbsolutePath() : "There was a problem generating the report", filePath != null ? "Success": "Error", filePath != null ? JOptionPane.INFORMATION_MESSAGE: JOptionPane.ERROR_MESSAGE);
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
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMonthChooser = new com.toedter.calendar.JMonthChooser();
        jYearChooser = new com.toedter.calendar.JYearChooser();
        jButtonGenerate = new javax.swing.JButton();
        jButtonSavePdf = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePayroll = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableTotal = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(86, 98, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton1.setBackground(new java.awt.Color(86, 98, 106));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back.png"))); // NOI18N
        jButton1.setText(" Back To Dashboard");
        jButton1.setBorder(null);
        jButton1.setFocusable(false);
        jButton1.setPreferredSize(new java.awt.Dimension(75, 35));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButton1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 2, true));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitle.setText("MONTHLY PAYROLL SUMMARY REPORT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 0);
        jPanel3.add(jLabelTitle, gridBagConstraints);

        jLabel2.setText("Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 8, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        jMonthChooser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jMonthChooser.setPreferredSize(new java.awt.Dimension(127, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 5);
        jPanel3.add(jMonthChooser, gridBagConstraints);

        jYearChooser.setValue(2022);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 5);
        jPanel3.add(jYearChooser, gridBagConstraints);

        jButtonGenerate.setBackground(new java.awt.Color(0, 183, 229));
        jButtonGenerate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonGenerate.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGenerate.setText("Generate");
        jButtonGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 8);
        jPanel3.add(jButtonGenerate, gridBagConstraints);

        jButtonSavePdf.setText("Save PDF");
        jButtonSavePdf.setEnabled(false);
        jButtonSavePdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSavePdfActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 8);
        jPanel3.add(jButtonSavePdf, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(jPanel3, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jTablePayroll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "Full Name", "Position", "Gross Income", "SSS", "Philhealth", "Pagibig", "Withholding Tax", "Net Pay"
            }
        ));
        jTablePayroll.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jTablePayroll.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jTablePayroll.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTablePayroll);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        jPanel4.add(jScrollPane1, gridBagConstraints);

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTableTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Total", "", "", "", "", "", "", "", ""
            }
        ));
        jTableTotal.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jTableTotal.setShowGrid(false);
        jTableTotal.getTableHeader().setResizingAllowed(false);
        jTableTotal.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableTotal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
        jPanel4.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(27, 27, 27, 27);
        jPanel1.add(jPanel2, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        new JframeDashboard(loggedEmployee.getID()).setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateActionPerformed
        if (!jButtonSavePdf.isEnabled()) {
            jButtonSavePdf.setEnabled(true);
        }
        loadPayrollTable();
    }//GEN-LAST:event_jButtonGenerateActionPerformed

    private void jButtonSavePdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSavePdfActionPerformed
        YearPeriod period = getPeriod();
        List<PayrollEntry> payrollEntries = PayrollService.computeBatchPayroll(employeeList, workHoursMap, period);
        PayrollSummary summary = PayrollService.calculatePayrollSummary(payrollEntries);
        File filePath = PdfProcessor.createPayrollReportPdf(payrollEntries, latestGeneratedPeriod, summary);
        showGenerateReportResult(filePath);
        redirectToDirectory(filePath);
    }//GEN-LAST:event_jButtonSavePdfActionPerformed

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonGenerate;
    private javax.swing.JButton jButtonSavePdf;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelTitle;
    private com.toedter.calendar.JMonthChooser jMonthChooser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePayroll;
    private javax.swing.JTable jTableTotal;
    private com.toedter.calendar.JYearChooser jYearChooser;
    // End of variables declaration//GEN-END:variables
}
