/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Ui;

import Model.YearPeriod;
import Model.Employee;
import Dao.EmployeeDAO;
import java.time.LocalDate;
import Dao.AttendanceDAO;
import Model.Finance;
import Model.HR;
import Service.HoursCalculator;
import Model.IT;
import Model.RegularEmployee;
import Model.AttendanceRecord;
import Model.LeaveBalance;
import Dao.CredentialsDAO;
import Dao.LeaveCreditsDAO;
import Dao.LeaveDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


/**
 *
 * @author keith
 */
public class JframeDashboard extends javax.swing.JFrame {

    /**
     * Creates new form JframeDashboard
     */
    private Employee loggedEmployee;
    private DefaultTableModel attendancetbl;
    public JframeDashboard(int employeeID) {       
        ToolTipManager.sharedInstance().setInitialDelay(0); // set delay of tooltips to appear to 0ms
        initComponents();
        attendancetbl = (DefaultTableModel) jTableAttendanceLogs.getModel();
        setExtendedState(MAXIMIZED_BOTH);
        loadEmployeeInformation(employeeID);
        configureRoleBasedButtons(loggedEmployee);
        populateLabelsWithInfo(loggedEmployee);        ;
        setUpWorkHoursChart();
        setUpLeaveStatusOverviewChart();
        setUpLeaveCredtisPieChart();
        populateAttendanceLogsTable();
    }
    
    private void loadEmployeeInformation (int employeeID) {
        
        try {
            EmployeeDAO dao = new EmployeeDAO();
            loggedEmployee = dao.getEmployeeByID(employeeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
                    
    }
    
    // enable or disable buttons based on user roles
    private void configureRoleBasedButtons (Employee loggedEmployee) {
        if (loggedEmployee instanceof HR) {
            jButtonAttendanceManagement.setVisible(true);
            jButtonEmployeeManagement.setVisible(true);
            jButtonLeaveManagement.setVisible(true);
            jButtonPayroll.setVisible(false);
            jButtonLeaveCreditsManagement.setVisible(true);
        }
        else if (loggedEmployee instanceof RegularEmployee) {
            jButtonAttendanceManagement.setVisible(false);
            jButtonEmployeeManagement.setVisible(false);
            jButtonLeaveManagement.setVisible(false);
            jButtonPayroll.setVisible(false);
            jButtonLeaveCreditsManagement.setVisible(false);
        }
        else if (loggedEmployee instanceof Finance) {
            jButtonAttendanceManagement.setVisible(false);
            jButtonEmployeeManagement.setVisible(false);
            jButtonLeaveManagement.setVisible(false);
            jButtonPayroll.setVisible(true);
            jButtonLeaveCreditsManagement.setVisible(false);
        }
        else if (loggedEmployee instanceof IT) {
            jButtonAttendanceManagement.setVisible(false);
            jButtonEmployeeManagement.setVisible(false);
            jButtonLeaveManagement.setVisible(false);
            jButtonPayroll.setVisible(false);
            jButtonLeaveCreditsManagement.setVisible(false);
        }
    }
      
    // set jLabels to employee information
    private void populateLabelsWithInfo (Employee loggedEmployee) {
        jLabelEmployeeID.setText("<html>Employee ID: <b>" + loggedEmployee.getID() + "</b></html>");
        jLabelFullName.setText("<html>Name: <b>" + loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName() + "</b></html>");
        jLabelBirthday.setText("<html>Birthday: <b>" + loggedEmployee.getBirthday() + "</b></html>");
        jLabelPhoneNumber.setText("<html>Phone: <b>" + loggedEmployee.getPhoneNumber() + "</b></html>");
        jLabelSssNumber.setText("<html>SSS: <b>" + loggedEmployee.getSSSNumber() + "</b></html>");
        jLabelPhilhealthNumber.setText("<html>Philhealth: <b>" + loggedEmployee.getPhilhealthNumber() + "</b></html>");
        jLabelTin.setText("<html>TIN: <b>" + loggedEmployee.getTinNumber() + "</b></html>");
        jLabelPagibig.setText("<html>Pagibig: <b>" + loggedEmployee.getPagibigNumber() + "</b></html>");
        jLabelStatus.setText("<html>Status: <b>" + loggedEmployee.getStatus() + "</b></html>");
        jLabelPosition.setText("<html>Position: <b>" + loggedEmployee.getPosition() + "</b></html>");
        jLabelRole.setText("<html>Role: <b>" + loggedEmployee.getRole() + "</b></html>");
        jLabelBasicSalary.setText("<html>Basic Salary: <b>" + formatAmount(loggedEmployee.getBasicSalary()) + "</b></html>");
        jLabelRiceSubsidy.setText("<html>Rice Subsidy: <b>" + formatAmount(loggedEmployee.getRiceSubsidy()) + "</b></html>");
        jLabelPhoneAllowance.setText("<html>Phone Allowance: <b>" + formatAmount(loggedEmployee.getPhoneAllowance()) + "</b></html>");
        jLabelClothingAllowance.setText("<html>Clothing Allowance: <b>" + formatAmount(loggedEmployee.getClothingAllowance()) + "</b></html>");
        jLabelHourlyRate.setText("<html>Hourly Rate: <b>" + formatAmount(loggedEmployee.getHourlyRate()) + "</b></html>");      
    }
    
    private String formatAmount (double amount) {
        return String.format("%.2f", amount);
    }
    
    private YearPeriod getMonthPeriod () {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return new YearPeriod(year, month);
    }
    
    private void setUpWorkHoursChart() {
        try {
            AttendanceDAO dao = new AttendanceDAO();
            YearPeriod period = getMonthPeriod();
            List<AttendanceRecord> attendance = dao.getAttendanceByIdAndPeriod(loggedEmployee.getID(), period);
            
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (AttendanceRecord record: attendance) {
                String rowKey = "Work Hours";
                String columnKey = record.getDate().toString();
                               
                double currentHours = HoursCalculator.calculateDailyHours(record.getTimeIn(), record.getTimeOut());
                Number existingValue = null;
                try {
                    existingValue = dataset.getValue(rowKey, columnKey);
                } catch (UnknownKeyException e) {
                }
                
                double totalHours = (existingValue != null ? existingValue.doubleValue() : 0.0) + currentHours;
                dataset.setValue(totalHours, rowKey, columnKey);
            }
            
            JFreeChart chart = ChartFactory.createBarChart(
                "",
                "Date",
                "Hours Worked",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
            );
            chart.getCategoryPlot().setBackgroundPaint(new Color(255,255,255));
            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setSeriesPaint(0, new Color(0,183,229));
            ChartPanel chartPanel = new ChartPanel(chart);
            jPanelWorkHoursChart.add(chartPanel, BorderLayout.CENTER);
                    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }   
    
    private void setUpLeaveStatusOverviewChart() {
        
        try {
            
            LeaveDAO dao = new LeaveDAO();
            int pendingCount = dao.getPendingLeaveCountByEmployeeID(loggedEmployee.getID());
            int approvedCount = dao.getApprovedLeaveCountByEmployeeID(loggedEmployee.getID());
            int deniedCount = dao.getDeniedLeaveCountByEmployeeID(loggedEmployee.getID());
            
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Approved", approvedCount);
            dataset.setValue("Pending", pendingCount);
            dataset.setValue("Denied", deniedCount);
            
            JFreeChart pieChart = ChartFactory.createPieChart(
                "",
                dataset,
                true,   // include legend
                true,   // tooltips
                false   // URLs
            );
            
            PiePlot plot = (PiePlot) pieChart.getPlot();
            plot.setSectionPaint("Approved", new Color(153, 221, 238));
            plot.setSectionPaint("Pending", new Color(0, 183, 229));
            plot.setSectionPaint("Denied", new Color(0, 102, 128));
            plot.setBackgroundPaint(new Color(255,255,255));
            
            ChartPanel pieChartPanel = new ChartPanel(pieChart);
            jPanelLeaveOverviewChart.add(pieChartPanel, BorderLayout.CENTER);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    private void setUpLeaveCredtisPieChart() {
        try {
            LeaveCreditsDAO dao = new LeaveCreditsDAO();
            LeaveBalance balance = dao.getLeaveCreditsByEmpID(loggedEmployee.getID());
            
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Vacation", balance.getVacationLeaveCredits());
            dataset.setValue("Medical", balance.getMedicalLeaveCredits());
            dataset.setValue("Personal", balance.getPersonalLeaveCredits());
            
            JFreeChart pieChart = ChartFactory.createPieChart(
                "",
                dataset,
                true,   // include legend
                true,   // tooltips
                false   // URLs
            );
            
            PiePlot plot = (PiePlot) pieChart.getPlot();
            plot.setSectionPaint("Vacation", new Color(153, 221, 238));
            plot.setSectionPaint("Medical", new Color(0, 183, 229));
            plot.setSectionPaint("Personal", new Color(0, 102, 128));
            plot.setBackgroundPaint(new Color(255,255,255));
            
            ChartPanel chartPanel = new ChartPanel(pieChart);
            jPanelLeaveCreditsBreakdown.add(chartPanel, BorderLayout.CENTER);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    
    public void timeIn () {
        
        try {
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            LocalDateTime now = LocalDateTime.now();
            boolean hasUnclosedEntry = attendanceDAO.hasUnclosedAttendanceEntry(loggedEmployee.getID(), now.toLocalDate());
            if (!hasUnclosedEntry) {
                boolean timeInSuccessful = loggedEmployee.timeIn(loggedEmployee.getID(), now);
                populateAttendanceLogsTable();
                showTimeInResult(timeInSuccessful);  
            } else {
                JOptionPane.showMessageDialog(this, "You have not yet timed out", "Invalid", JOptionPane.WARNING_MESSAGE);
            }    
        } catch (SQLException e) {
            e.printStackTrace();
        }
                          
    }
    
    public void timeOut () {        
        LocalDateTime now = LocalDateTime.now();
        boolean timeOutSuccessful = loggedEmployee.timeOut(loggedEmployee.getID(), now);       
        populateAttendanceLogsTable();
        showTimeOutResult(timeOutSuccessful);
    }
    
    public void showTimeInResult (boolean timeInSuccessful) {
        JOptionPane.showMessageDialog(this, timeInSuccessful ? "Time in has been recorded" : "Unable to time in" , timeInSuccessful ? "Success" : "Error" , timeInSuccessful? JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);
    }
    
    public void showTimeOutResult (boolean timeOutSuccessful) {
        JOptionPane.showMessageDialog(this, timeOutSuccessful ? "Time out has been recorded" : "Unable to time out" , timeOutSuccessful ? "Success" : "Error" , timeOutSuccessful? JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);
    }
    
    public void populateAttendanceLogsTable () { 
        try {
            LocalDateTime now = LocalDateTime.now();
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            List<AttendanceRecord> attendanceRecords = attendanceDAO.getAttendanceByCustomRange(loggedEmployee.getID(), now.toLocalDate(), now.toLocalDate());
            attendancetbl.setRowCount(0);
            for (AttendanceRecord record: attendanceRecords) {
                attendancetbl.addRow(createTableRowData(record));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Object [] createTableRowData (AttendanceRecord record) {
        return new Object [] {record.getEmployeeID(), 
                record.getDate(), 
                record.getTimeIn(), 
                record.getTimeOut()};
    }
    
    private void showRemove2FAResult(boolean isRemoved) {
        JOptionPane.showMessageDialog(this, isRemoved? "2FA has been disabled sucessfully": "Error disabling 2FA", isRemoved ? "Success" : "Error", isRemoved ? JOptionPane.INFORMATION_MESSAGE: JOptionPane.ERROR_MESSAGE );
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

        jPanelLeft = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButtonEnable2FA = new javax.swing.JButton();
        jButtonChangePassword = new javax.swing.JButton();
        jButtonAboutMotorPh = new javax.swing.JButton();
        jButtonAttendance = new javax.swing.JButton();
        jButtonPayslip = new javax.swing.JButton();
        jButtonLeave = new javax.swing.JButton();
        jButtonEmployeeManagement = new javax.swing.JButton();
        jButtonAttendanceManagement = new javax.swing.JButton();
        jButtonLeaveManagement = new javax.swing.JButton();
        jButtonPayroll = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanelLeaveOverviewChart = new javax.swing.JPanel();
        jButtonLeaveCreditsManagement = new javax.swing.JButton();
        jPanelRight = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButtonClockIn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButtonClockOut = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButtonLogout = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanelLeaveCreditsBreakdown = new javax.swing.JPanel();
        jPanelTop = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanelMid = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabelEmployeeID = new javax.swing.JLabel();
        jLabelSssNumber = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jLabelRiceSubsidy = new javax.swing.JLabel();
        jLabelFullName = new javax.swing.JLabel();
        jLabelPhilhealthNumber = new javax.swing.JLabel();
        jLabelPosition = new javax.swing.JLabel();
        jLabelPhoneAllowance = new javax.swing.JLabel();
        jLabelBirthday = new javax.swing.JLabel();
        jLabelTin = new javax.swing.JLabel();
        jLabelRole = new javax.swing.JLabel();
        jLabelClothingAllowance = new javax.swing.JLabel();
        jLabelPhoneNumber = new javax.swing.JLabel();
        jLabelPagibig = new javax.swing.JLabel();
        jLabelBasicSalary = new javax.swing.JLabel();
        jLabelHourlyRate = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAttendanceLogs = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jPanel15 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanelWorkHoursChart = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanelLeft.setBackground(new java.awt.Color(21, 36, 46));
        jPanelLeft.setPreferredSize(new java.awt.Dimension(200, 444));
        jPanelLeft.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(21, 36, 46));
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 183, 229));
        jLabel1.setText(" NAVIGATIONS");
        jLabel1.setPreferredSize(new java.awt.Dimension(89, 35));
        jPanel2.add(jLabel1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jPanel2, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(21, 36, 46));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 40));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 183, 229));
        jLabel3.setText(" ACCOUNT SETTINGS");
        jLabel3.setPreferredSize(new java.awt.Dimension(89, 35));
        jPanel1.add(jLabel3, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jPanel1, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(21, 36, 46));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButtonEnable2FA.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEnable2FA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shield.png"))); // NOI18N
        jButtonEnable2FA.setText("Enable 2FA");
        jButtonEnable2FA.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonEnable2FA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnable2FAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jButtonEnable2FA, gridBagConstraints);

        jButtonChangePassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonChangePassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/key.png"))); // NOI18N
        jButtonChangePassword.setText("Change my password");
        jButtonChangePassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangePasswordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jButtonChangePassword, gridBagConstraints);

        jButtonAboutMotorPh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAboutMotorPh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/info.png"))); // NOI18N
        jButtonAboutMotorPh.setText("About MotorPH");
        jButtonAboutMotorPh.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAboutMotorPh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAboutMotorPhActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jButtonAboutMotorPh, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jPanel3, gridBagConstraints);

        jButtonAttendance.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAttendance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/calendar.png"))); // NOI18N
        jButtonAttendance.setText("Attendance");
        jButtonAttendance.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAttendanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonAttendance, gridBagConstraints);

        jButtonPayslip.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPayslip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/salary.png"))); // NOI18N
        jButtonPayslip.setText("Payslip");
        jButtonPayslip.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonPayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPayslipActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonPayslip, gridBagConstraints);

        jButtonLeave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonLeave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/leave.png"))); // NOI18N
        jButtonLeave.setText("Leave");
        jButtonLeave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLeaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonLeave, gridBagConstraints);

        jButtonEmployeeManagement.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonEmployeeManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/user.png"))); // NOI18N
        jButtonEmployeeManagement.setText("Employee Management");
        jButtonEmployeeManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonEmployeeManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEmployeeManagementActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonEmployeeManagement, gridBagConstraints);

        jButtonAttendanceManagement.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAttendanceManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/calendar.png"))); // NOI18N
        jButtonAttendanceManagement.setText("Attendance Management");
        jButtonAttendanceManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAttendanceManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAttendanceManagementActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonAttendanceManagement, gridBagConstraints);

        jButtonLeaveManagement.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonLeaveManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/personWcalendar.png"))); // NOI18N
        jButtonLeaveManagement.setText("Leave Management");
        jButtonLeaveManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonLeaveManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLeaveManagementActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonLeaveManagement, gridBagConstraints);

        jButtonPayroll.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/salary.png"))); // NOI18N
        jButtonPayroll.setText("Payroll");
        jButtonPayroll.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPayrollActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonPayroll, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(21, 36, 46));
        jPanel4.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 183, 229));
        jLabel2.setText(" LEAVE STATUS OVERVIEW");
        jLabel2.setPreferredSize(new java.awt.Dimension(89, 35));
        jPanel4.add(jLabel2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jPanel4, gridBagConstraints);

        jPanelLeaveOverviewChart.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLeaveOverviewChart.setPreferredSize(new java.awt.Dimension(0, 90));
        jPanelLeaveOverviewChart.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        jPanelLeft.add(jPanelLeaveOverviewChart, gridBagConstraints);

        jButtonLeaveCreditsManagement.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonLeaveCreditsManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/statistics.png"))); // NOI18N
        jButtonLeaveCreditsManagement.setText("Adjust Leave Credits");
        jButtonLeaveCreditsManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonLeaveCreditsManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLeaveCreditsManagementActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelLeft.add(jButtonLeaveCreditsManagement, gridBagConstraints);

        getContentPane().add(jPanelLeft, java.awt.BorderLayout.LINE_START);

        jPanelRight.setBackground(new java.awt.Color(21, 36, 46));
        jPanelRight.setPreferredSize(new java.awt.Dimension(200, 444));
        jPanelRight.setLayout(new java.awt.GridBagLayout());

        jPanel10.setBackground(new java.awt.Color(21, 36, 46));
        jPanel10.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 183, 229));
        jLabel5.setText(" NOTIFICATIONS");
        jPanel10.add(jLabel5, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanelRight.add(jPanel10, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(100, 150));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(100, 150));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setMaximumSize(new java.awt.Dimension(45, 90));
        jList1.setMinimumSize(new java.awt.Dimension(45, 50));
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        jPanelRight.add(jScrollPane1, gridBagConstraints);

        jPanel6.setBackground(new java.awt.Color(21, 36, 46));
        jPanel6.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 183, 229));
        jLabel6.setText(" ACTION PANEL");
        jPanel6.add(jLabel6, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanelRight.add(jPanel6, gridBagConstraints);

        jPanel7.setBackground(new java.awt.Color(21, 36, 46));
        jPanel7.setLayout(new java.awt.GridLayout(3, 2));

        jButtonClockIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/time (1).png"))); // NOI18N
        jButtonClockIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClockInActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonClockIn);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 183, 229));
        jLabel9.setText(" Clock In");
        jPanel7.add(jLabel9);

        jButtonClockOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clock (2).png"))); // NOI18N
        jButtonClockOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClockOutActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonClockOut);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 183, 229));
        jLabel10.setText(" Clock Out");
        jPanel7.add(jLabel10);

        jButtonLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout (3).png"))); // NOI18N
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonLogout);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 183, 229));
        jLabel11.setText(" Logout");
        jPanel7.add(jLabel11);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanelRight.add(jPanel7, gridBagConstraints);

        jPanel8.setBackground(new java.awt.Color(21, 36, 46));
        jPanel8.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 183, 229));
        jLabel7.setText(" LEAVE CREDITS BREAKDOWN");
        jPanel8.add(jLabel7, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelRight.add(jPanel8, gridBagConstraints);

        jPanelLeaveCreditsBreakdown.setBackground(new java.awt.Color(255, 255, 255));
        jPanelLeaveCreditsBreakdown.setPreferredSize(new java.awt.Dimension(0, 90));
        jPanelLeaveCreditsBreakdown.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelRight.add(jPanelLeaveCreditsBreakdown, gridBagConstraints);

        getContentPane().add(jPanelRight, java.awt.BorderLayout.LINE_END);

        jPanelTop.setBackground(new java.awt.Color(204, 204, 204));
        jPanelTop.setPreferredSize(new java.awt.Dimension(653, 20));
        jPanelTop.setLayout(new java.awt.BorderLayout());

        jLabel15.setText(" @MotorPH");
        jPanelTop.add(jLabel15, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelTop, java.awt.BorderLayout.PAGE_START);

        jPanelMid.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMid.setLayout(new java.awt.GridBagLayout());

        jPanel11.setBackground(new java.awt.Color(0, 183, 229));
        jPanel11.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText(" EMPLOYEE INFORMATION");
        jPanel11.add(jLabel12, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelMid.add(jPanel11, gridBagConstraints);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new java.awt.GridLayout(4, 4));

        jLabelEmployeeID.setText("Employee ID: 10001");
        jPanel12.add(jLabelEmployeeID);

        jLabelSssNumber.setText("SSS: 44-4506057-3");
        jPanel12.add(jLabelSssNumber);

        jLabelStatus.setText("Status:");
        jPanel12.add(jLabelStatus);

        jLabelRiceSubsidy.setText("RiceSubsidy:");
        jPanel12.add(jLabelRiceSubsidy);

        jLabelFullName.setText("Name: Manuel Garcia");
        jPanel12.add(jLabelFullName);

        jLabelPhilhealthNumber.setText("Philhealth: ");
        jPanel12.add(jLabelPhilhealthNumber);

        jLabelPosition.setText("Position:");
        jPanel12.add(jLabelPosition);

        jLabelPhoneAllowance.setText("PhoneAllowance:");
        jPanel12.add(jLabelPhoneAllowance);

        jLabelBirthday.setText("Birthday: 1983-10-11");
        jPanel12.add(jLabelBirthday);

        jLabelTin.setText("TIN:");
        jPanel12.add(jLabelTin);

        jLabelRole.setText("Role:");
        jPanel12.add(jLabelRole);

        jLabelClothingAllowance.setText("Clothing allowance:");
        jPanel12.add(jLabelClothingAllowance);

        jLabelPhoneNumber.setText("Phone: 966-860-270");
        jPanel12.add(jLabelPhoneNumber);

        jLabelPagibig.setText("Pagibig:");
        jPanel12.add(jLabelPagibig);

        jLabelBasicSalary.setText("BasicSalary:");
        jPanel12.add(jLabelBasicSalary);

        jLabelHourlyRate.setText("Hourly Rate:");
        jPanel12.add(jLabelHourlyRate);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelMid.add(jPanel12, gridBagConstraints);

        jPanel13.setBackground(new java.awt.Color(51, 51, 51));
        jPanel13.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText(" TODAY'S ATTENDANCE LOG");
        jPanel13.add(jLabel13, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelMid.add(jPanel13, gridBagConstraints);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setPreferredSize(new java.awt.Dimension(469, 110));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jTableAttendanceLogs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee ID", "Date", "Time in", "Time out"
            }
        ));
        jTableAttendanceLogs.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jTableAttendanceLogs.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jTableAttendanceLogs.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableAttendanceLogs);

        jPanel17.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel14.add(jPanel17, gridBagConstraints);

        jPanel18.setBackground(new java.awt.Color(0, 51, 102));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jCalendar1.setBackground(new java.awt.Color(255, 255, 255));
        jCalendar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jCalendar1.setDecorationBackgroundColor(new java.awt.Color(255, 255, 255));
        jCalendar1.setTodayButtonText("");
        jPanel18.add(jCalendar1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel14.add(jPanel18, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelMid.add(jPanel14, gridBagConstraints);

        jPanel15.setBackground(new java.awt.Color(51, 51, 51));
        jPanel15.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText(" DAILY WORK HOURS THIS MONTH");
        jPanel15.add(jLabel14, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelMid.add(jPanel15, gridBagConstraints);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setPreferredSize(new java.awt.Dimension(469, 130));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jPanelWorkHoursChart.setBackground(new java.awt.Color(204, 204, 204));
        jPanelWorkHoursChart.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 2, 6);
        jPanel16.add(jPanelWorkHoursChart, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        jPanelMid.add(jPanel16, gridBagConstraints);

        getContentPane().add(jPanelMid, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEmployeeManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEmployeeManagementActionPerformed
        this.dispose();
        new JframeEmpManagement(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonEmployeeManagementActionPerformed

    private void jButtonLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLeaveActionPerformed
        this.dispose();
        new JframeLeave(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonLeaveActionPerformed

    private void jButtonLeaveManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLeaveManagementActionPerformed
        this.dispose();
        new JframeLeaveManagement(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonLeaveManagementActionPerformed

    private void jButtonPayslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayslipActionPerformed
        this.dispose();
        new JframePayslip(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonPayslipActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        this.dispose();
        new JframeLogin().setVisible(true);
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jButtonAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAttendanceActionPerformed
        this.dispose();
        new JframeAttendance(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonAttendanceActionPerformed

    private void jButtonAttendanceManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAttendanceManagementActionPerformed
        this.dispose();
        new JframeAttendanceManagement(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonAttendanceManagementActionPerformed

    private void jButtonPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayrollActionPerformed
        this.dispose();
        new JframePayroll(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonPayrollActionPerformed

    private void jButtonClockInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClockInActionPerformed
       timeIn();
    }//GEN-LAST:event_jButtonClockInActionPerformed

    private void jButtonClockOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClockOutActionPerformed
        timeOut();
    }//GEN-LAST:event_jButtonClockOutActionPerformed

    private void jButtonLeaveCreditsManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLeaveCreditsManagementActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new JframeLeaveCredits(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonLeaveCreditsManagementActionPerformed

    private void jButtonChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangePasswordActionPerformed
        new JDialogChangePassword(this, true, loggedEmployee.getID()).setVisible(true);
    }//GEN-LAST:event_jButtonChangePasswordActionPerformed

    private void jButtonEnable2FAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnable2FAActionPerformed
        // TODO add your handling code here:
        try {
            CredentialsDAO dao = new CredentialsDAO();
            boolean has2FA = dao.has2FAEnabled(loggedEmployee.getID());
            
            if (has2FA) {
                int option = JOptionPane.showConfirmDialog(this, "You already have 2FA enabled. Do you want to disable it?","Select an option", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    boolean isRemoved = dao.remove2FA(loggedEmployee.getID());
                    showRemove2FAResult(isRemoved);
                }
                return;
            }
            new JDialog2FA(this, true, loggedEmployee.getID()).setVisible(true);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_jButtonEnable2FAActionPerformed

    private void jButtonAboutMotorPhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAboutMotorPhActionPerformed
        // TODO add your handling code here:
        try {
        Desktop.getDesktop().browse(new URI("https://sites.google.com/mmdc.mcl.edu.ph/motorph/about-us?authuser=0"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButtonAboutMotorPhActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAboutMotorPh;
    private javax.swing.JButton jButtonAttendance;
    private javax.swing.JButton jButtonAttendanceManagement;
    private javax.swing.JButton jButtonChangePassword;
    private javax.swing.JButton jButtonClockIn;
    private javax.swing.JButton jButtonClockOut;
    private javax.swing.JButton jButtonEmployeeManagement;
    private javax.swing.JButton jButtonEnable2FA;
    private javax.swing.JButton jButtonLeave;
    private javax.swing.JButton jButtonLeaveCreditsManagement;
    private javax.swing.JButton jButtonLeaveManagement;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonPayroll;
    private javax.swing.JButton jButtonPayslip;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBasicSalary;
    private javax.swing.JLabel jLabelBirthday;
    private javax.swing.JLabel jLabelClothingAllowance;
    private javax.swing.JLabel jLabelEmployeeID;
    private javax.swing.JLabel jLabelFullName;
    private javax.swing.JLabel jLabelHourlyRate;
    private javax.swing.JLabel jLabelPagibig;
    private javax.swing.JLabel jLabelPhilhealthNumber;
    private javax.swing.JLabel jLabelPhoneAllowance;
    private javax.swing.JLabel jLabelPhoneNumber;
    private javax.swing.JLabel jLabelPosition;
    private javax.swing.JLabel jLabelRiceSubsidy;
    private javax.swing.JLabel jLabelRole;
    private javax.swing.JLabel jLabelSssNumber;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTin;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelLeaveCreditsBreakdown;
    private javax.swing.JPanel jPanelLeaveOverviewChart;
    private javax.swing.JPanel jPanelLeft;
    private javax.swing.JPanel jPanelMid;
    private javax.swing.JPanel jPanelRight;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JPanel jPanelWorkHoursChart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableAttendanceLogs;
    // End of variables declaration//GEN-END:variables
}
