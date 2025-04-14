/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Jframes;

import java.time.LocalDate;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import oopClasses.Employee;
import oopClasses.EmployeeValidator;
import oopClasses.HR;
import oopClasses.RegularEmployee;

/**
 *
 * @author keith
 */
public class JframeEmployeeForm extends javax.swing.JFrame {

    /**
     * Creates new form JframeEmployeeForm
     */
    private Employee loggedEmployee;
    private int employeeIdToEdit;
    private boolean isAdding;
    private HR hrEmployee;
    private SimpleDateFormat dateFormat;
    // constructor for adding employee
    public JframeEmployeeForm(Employee loggedEmployee) {
        this.isAdding = true;
        this.loggedEmployee = loggedEmployee;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        showErrorLabels(false);
        initHrUser(loggedEmployee);
    }
    // constructor for editing employee
    public JframeEmployeeForm (Employee loggedEmployee, int employeeIdToEdit) {
        this.isAdding = false;
        this.employeeIdToEdit = employeeIdToEdit;
        this.loggedEmployee = loggedEmployee;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        showErrorLabels(false);
        jLabelHeader.setText("Edit an employee");
        initHrUser(loggedEmployee);
        populateTextFields();
        
    }
    
    // creates an HR object if employee is instance of HR class
    private void initHrUser (Employee loggedEmployee) {
        if (loggedEmployee instanceof HR) {
            // pass the info of the current employee to the hr object
            this.hrEmployee = new HR(loggedEmployee);
        }
    }
    
    // populates text fields with info of the "employee to edit"
    private void populateTextFields () {
        if (hrEmployee != null) {
            Employee employee = hrEmployee.viewEmployeeByID(employeeIdToEdit);
            jTextFieldFirstName.setText(employee.getFirstName().trim());
            jTextFieldLastName.setText(employee.getLastName().trim());
            jDateChooserBirthday.setDate(Date.valueOf(employee.getBirthday()));
            jTextFieldPhoneNumber.setText(employee.getPhoneNumber().trim());
            jTextFieldAddress.setText(employee.getAddress().trim());
            jComboBoxStatus.setSelectedItem(employee.getStatus().trim());
            jTextFieldPosition.setText(employee.getPosition().trim());
            jComboBoxRole.setSelectedItem(employee.getRole().trim());
            jTextFieldSupervisor.setText(employee.getSupervisor().trim());
            jTextFieldBasicSalary.setText(convertToString(employee.getBasicSalary()));
            jTextFieldGrossSemiMonthly.setText(convertToString(employee.getGrossSemiMonthlyRate()));
            jTextFieldHourlyRate.setText(convertToString(employee.getHourlyRate()));
            jTextFieldRiceSubsidy.setText(convertToString(employee.getRiceSubsidy()));
            jTextFieldPhoneAllowance.setText(convertToString(employee.getPhoneAllowance()));
            jTextFieldClothingAllowance.setText(convertToString(employee.getClothingAllowance()));
            jTextFieldSssNumber.setText(employee.getSSSNumber().trim());
            jTextFieldPhilhealthNumber.setText(employee.getPhilhealthNumber().trim());
            jTextFieldPagibigNumber.setText(employee.getPagibigNumber().trim());
            jTextFieldTinNumber.setText(employee.getTinNumber().trim());
        }
    }
    
    // helper method (double to String)
    private String convertToString (double amount) {
        return String.valueOf(amount);
    }
    
    // saves updated info from the textfields to the employee
    private Employee textFieldsInfoToEmployee () {
        return new RegularEmployee(
        0,
        jTextFieldFirstName.getText(),
        jTextFieldLastName.getText(),
        jTextFieldPosition.getText(),
        jComboBoxStatus.getSelectedItem().toString(),
        LocalDate.parse(dateFormat.format(jDateChooserBirthday.getDate())),
        jTextFieldAddress.getText(),
        jTextFieldPhoneNumber.getText(),
        jTextFieldSssNumber.getText(),
        jTextFieldPagibigNumber.getText(),
        jTextFieldTinNumber.getText(),
        Double.parseDouble(jTextFieldHourlyRate.getText()),
        jTextFieldPhilhealthNumber.getText(),
        jComboBoxRole.getSelectedItem().toString(),
        jTextFieldSupervisor.getText(),
        Double.parseDouble(jTextFieldBasicSalary.getText()),
        Double.parseDouble(jTextFieldRiceSubsidy.getText()),        
        Double.parseDouble(jTextFieldPhoneAllowance.getText()),
        Double.parseDouble(jTextFieldClothingAllowance.getText()),
        Double.parseDouble(jTextFieldGrossSemiMonthly.getText()        
        ));
    }
    
    private void saveEmployee () {
        Employee updatedEmployee = textFieldsInfoToEmployee();      
        boolean isSaved;
        String message;
        
        if (isAdding) {
            isSaved = hrEmployee.addEmployeeRecord(updatedEmployee);
            message = isSaved ? updatedEmployee.getFirstName() + " " + updatedEmployee.getLastName() + " was added succesfully." : "Failed to add record";
        } else {
            isSaved = hrEmployee.updateEmployeeRecordByID(employeeIdToEdit, updatedEmployee);
            message = isSaved ? updatedEmployee.getFirstName() + " " + updatedEmployee.getLastName() + " was updated succesfully." : "Failed to update record";
        }
        JOptionPane.showMessageDialog(this, message, "STATUS", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void closeFrameOpenPrevious () {
        this.dispose();
        new JframeEmpManagement(loggedEmployee).setVisible(true);
    }  
    
    private void validateThenSaveEmployee () {
        showErrorLabels(false);
        boolean validEmployee = true;
        String errorMessage;
        
        errorMessage = EmployeeValidator.validateFirstNameWithMessage(jTextFieldFirstName.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setFirstNameErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateLastNameWithMessage(jTextFieldLastName.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setLastNameErrorMessage(errorMessage);
        
        java.util.Date date = jDateChooserBirthday.getDate();
        LocalDate birthday = date != null ? LocalDate.parse(dateFormat.format(date)) : null;       
        errorMessage = EmployeeValidator.validateBirthdayWithMessage(birthday);
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setBirthdayErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validatePhoneNumberWithMessage(jTextFieldPhoneNumber.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setPhoneNumberErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAddressWithMessage(jTextFieldAddress.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setAddressErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validatePositionWithMessage(jTextFieldPosition.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setPositionErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateSupervisorWithMessage(jTextFieldSupervisor.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setSupervisorErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAmountWithMessage(jTextFieldBasicSalary.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setBasicSalaryErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAmountWithMessage(jTextFieldGrossSemiMonthly.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setGrossSemiMonthlyRateErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAmountWithMessage(jTextFieldHourlyRate.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setHourlyRateErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAmountWithMessage(jTextFieldRiceSubsidy.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setRiceSubsidyErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAmountWithMessage(jTextFieldClothingAllowance.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setClothingAllowanceErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateAmountWithMessage(jTextFieldPhoneAllowance.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setPhoneAllowanceErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateSssNumber(jTextFieldSssNumber.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setSssNumberErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validatePhilhealthWithMessage(jTextFieldPhilhealthNumber.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setPhilhealthErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validatePagibigWithMessage(jTextFieldPagibigNumber.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setPagibigErrorMessage(errorMessage);
        
        errorMessage = EmployeeValidator.validateTinNumber(jTextFieldTinNumber.getText());
        if (!errorMessage.isBlank()) { validEmployee = false;}
        setTinErrorMessage(errorMessage);
         
        if (validEmployee) {
            saveEmployee();
            closeFrameOpenPrevious();
        }
        
    }
    
    private void setTinErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelTinNumberError.setText(message);
            jLabelTinNumberError.setVisible(true);
        }
    }
    
    private void setPagibigErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelPagibigNumberError.setText(message);
            jLabelPagibigNumberError.setVisible(true);
        }
    }
    
    private void setPhilhealthErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelPhilhealthNumberError.setText(message);
            jLabelPhilhealthNumberError.setVisible(true);
        }
    }
    
    private void setSssNumberErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelSssNumberError.setText(message);
            jLabelSssNumberError.setVisible(true);
        }
    }
    
    private void setPhoneAllowanceErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelPhoneAllowanceError.setText(message);
            jLabelPhoneAllowanceError.setVisible(true);
        }
    }
    
    private void setClothingAllowanceErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelClothingAllowanceError.setText(message);
            jLabelClothingAllowanceError.setVisible(true);
        }
    }
    
    private void setRiceSubsidyErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelRiceSubisdyError.setText(message);
            jLabelRiceSubisdyError.setVisible(true);
        }
    }
    
    private void setHourlyRateErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelHourlyRateError.setText(message);
            jLabelHourlyRateError.setVisible(true);
        }
    }
    
    private void setGrossSemiMonthlyRateErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelGrossSemiMonthlyError.setText(message);
            jLabelGrossSemiMonthlyError.setVisible(true);
        }
    }
    
    private void setBasicSalaryErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelBasicSalaryError.setText(message);
            jLabelBasicSalaryError.setVisible(true);
        }
    }
    
    private void setSupervisorErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelSupervisorError.setText(message);
            jLabelSupervisorError.setVisible(true);
        }
    }
    
    private void setPositionErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelPositionError.setText(message);
            jLabelPositionError.setVisible(true);
        }
    }
    
    private void setAddressErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelAddressError.setText(message);
            jLabelAddressError.setVisible(true);
        }
    }
    
    private void setFirstNameErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelFirstNameError.setText(message);
            jLabelFirstNameError.setVisible(true);
        }
    }
    
    private void setLastNameErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelLastNameError.setText(message);
            jLabelLastNameError.setVisible(true);
        }
    }
    
    private void setBirthdayErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelBirthdayError.setText(message);
            jLabelBirthdayError.setVisible(true);
        }
    }
    
    private void setPhoneNumberErrorMessage (String message) {
        if (!message.isBlank()) {
            jLabelPhoneNumberError.setText(message);
            jLabelPhoneNumberError.setVisible(true);
        }
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
        jLabelHeader = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldFirstName = new javax.swing.JTextField();
        jTextFieldLastName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jDateChooserBirthday = new com.toedter.calendar.JDateChooser();
        jTextFieldPhoneNumber = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldAddress = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldPosition = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldSupervisor = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabelFirstNameError = new javax.swing.JLabel();
        jLabelLastNameError = new javax.swing.JLabel();
        jLabelBirthdayError = new javax.swing.JLabel();
        jLabelPhoneNumberError = new javax.swing.JLabel();
        jLabelAddressError = new javax.swing.JLabel();
        jLabelPositionError = new javax.swing.JLabel();
        jLabelSupervisorError = new javax.swing.JLabel();
        jComboBoxRole = new javax.swing.JComboBox<>();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldBasicSalary = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldGrossSemiMonthly = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldRiceSubsidy = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldPhoneAllowance = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldHourlyRate = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldClothingAllowance = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabelBasicSalaryError = new javax.swing.JLabel();
        jLabelGrossSemiMonthlyError = new javax.swing.JLabel();
        jLabelHourlyRateError = new javax.swing.JLabel();
        jLabelRiceSubisdyError = new javax.swing.JLabel();
        jLabelPhoneAllowanceError = new javax.swing.JLabel();
        jLabelClothingAllowanceError = new javax.swing.JLabel();
        jButtonDiscard = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldSssNumber = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldPhilhealthNumber = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldTinNumber = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldPagibigNumber = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabelSssNumberError = new javax.swing.JLabel();
        jLabelPhilhealthNumberError = new javax.swing.JLabel();
        jLabelPagibigNumberError = new javax.swing.JLabel();
        jLabelTinNumberError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(86, 98, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/man (1).png"))); // NOI18N
        jLabelHeader.setText("Add New Employee");
        jLabelHeader.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelHeader.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        jPanel1.add(jLabelHeader, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(86, 98, 106));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(86, 98, 106));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBackground(new java.awt.Color(86, 98, 106));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("First Name*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 12, 2, 12);
        jPanel4.add(jLabel1, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Last Name*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 12, 2, 12);
        jPanel4.add(jLabel3, gridBagConstraints);

        jTextFieldFirstName.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jTextFieldFirstName, gridBagConstraints);

        jTextFieldLastName.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jTextFieldLastName, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Birthday*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Phone Number*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel5, gridBagConstraints);

        jDateChooserBirthday.setDateFormatString("yyyy-MM-dd");
        jDateChooserBirthday.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jDateChooserBirthday, gridBagConstraints);

        jTextFieldPhoneNumber.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jTextFieldPhoneNumber, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Address*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel6, gridBagConstraints);

        jTextFieldAddress.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jTextFieldAddress, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Status*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Position*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel8, gridBagConstraints);

        jTextFieldPosition.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jTextFieldPosition, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Role*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Supervisor*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jLabel10, gridBagConstraints);

        jTextFieldSupervisor.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jTextFieldSupervisor, gridBagConstraints);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel4.add(jPanel7, gridBagConstraints);

        jLabelFirstNameError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelFirstNameError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jLabelFirstNameError, gridBagConstraints);

        jLabelLastNameError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelLastNameError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel4.add(jLabelLastNameError, gridBagConstraints);

        jLabelBirthdayError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelBirthdayError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel4.add(jLabelBirthdayError, gridBagConstraints);

        jLabelPhoneNumberError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelPhoneNumberError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        jPanel4.add(jLabelPhoneNumberError, gridBagConstraints);

        jLabelAddressError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelAddressError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        jPanel4.add(jLabelAddressError, gridBagConstraints);

        jLabelPositionError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelPositionError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        jPanel4.add(jLabelPositionError, gridBagConstraints);

        jLabelSupervisorError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelSupervisorError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        jPanel4.add(jLabelSupervisorError, gridBagConstraints);

        jComboBoxRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HR", "Finance", "IT", "Employee" }));
        jComboBoxRole.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jComboBoxRole, gridBagConstraints);

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Regular", "Probationary" }));
        jComboBoxStatus.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel4.add(jComboBoxStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel4, gridBagConstraints);

        jPanel5.setBackground(new java.awt.Color(86, 98, 106));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Basic Salary*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jLabel11, gridBagConstraints);

        jTextFieldBasicSalary.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jTextFieldBasicSalary, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Gross Semi Monthly*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jLabel12, gridBagConstraints);

        jTextFieldGrossSemiMonthly.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jTextFieldGrossSemiMonthly, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Hourly Rate*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jLabel13, gridBagConstraints);

        jTextFieldRiceSubsidy.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jTextFieldRiceSubsidy, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Rice Subsidy*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jLabel14, gridBagConstraints);

        jTextFieldPhoneAllowance.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jTextFieldPhoneAllowance, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Phone Allowance*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jLabel15, gridBagConstraints);

        jTextFieldHourlyRate.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jTextFieldHourlyRate, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Clothing Allowance*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jLabel21, gridBagConstraints);

        jTextFieldClothingAllowance.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jTextFieldClothingAllowance, gridBagConstraints);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(8, 12, 2, 12);
        jPanel5.add(jPanel8, gridBagConstraints);

        jLabelBasicSalaryError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelBasicSalaryError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel5.add(jLabelBasicSalaryError, gridBagConstraints);

        jLabelGrossSemiMonthlyError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelGrossSemiMonthlyError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel5.add(jLabelGrossSemiMonthlyError, gridBagConstraints);

        jLabelHourlyRateError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelHourlyRateError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        jPanel5.add(jLabelHourlyRateError, gridBagConstraints);

        jLabelRiceSubisdyError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelRiceSubisdyError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        jPanel5.add(jLabelRiceSubisdyError, gridBagConstraints);

        jLabelPhoneAllowanceError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelPhoneAllowanceError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        jPanel5.add(jLabelPhoneAllowanceError, gridBagConstraints);

        jLabelClothingAllowanceError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelClothingAllowanceError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        jPanel5.add(jLabelClothingAllowanceError, gridBagConstraints);

        jButtonDiscard.setBackground(new java.awt.Color(153, 0, 0));
        jButtonDiscard.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDiscard.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDiscard.setText("DISCARD");
        jButtonDiscard.setPreferredSize(new java.awt.Dimension(200, 40));
        jButtonDiscard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDiscardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        jPanel5.add(jButtonDiscard, gridBagConstraints);

        jButtonSave.setBackground(new java.awt.Color(0, 183, 229));
        jButtonSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSave.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSave.setText("SAVE");
        jButtonSave.setPreferredSize(new java.awt.Dimension(200, 40));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        jPanel5.add(jButtonSave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel5, gridBagConstraints);

        jPanel6.setBackground(new java.awt.Color(86, 98, 106));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("SSS Number*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 10);
        jPanel6.add(jLabel16, gridBagConstraints);

        jTextFieldSssNumber.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        jPanel6.add(jTextFieldSssNumber, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Philhealth Number*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 10);
        jPanel6.add(jLabel17, gridBagConstraints);

        jTextFieldPhilhealthNumber.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        jPanel6.add(jTextFieldPhilhealthNumber, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Pagibig Number*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 10);
        jPanel6.add(jLabel18, gridBagConstraints);

        jTextFieldTinNumber.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        jPanel6.add(jTextFieldTinNumber, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("TIN Number*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 10);
        jPanel6.add(jLabel19, gridBagConstraints);

        jTextFieldPagibigNumber.setPreferredSize(new java.awt.Dimension(200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        jPanel6.add(jTextFieldPagibigNumber, gridBagConstraints);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel6.add(jPanel9, gridBagConstraints);

        jLabelSssNumberError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelSssNumberError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel6.add(jLabelSssNumberError, gridBagConstraints);

        jLabelPhilhealthNumberError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelPhilhealthNumberError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel6.add(jLabelPhilhealthNumberError, gridBagConstraints);

        jLabelPagibigNumberError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelPagibigNumberError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel6.add(jLabelPagibigNumberError, gridBagConstraints);

        jLabelTinNumberError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelTinNumberError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        jPanel6.add(jLabelTinNumberError, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 15, 15);
        jPanel2.add(jPanel3, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDiscardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDiscardActionPerformed
        this.dispose();
        new JframeEmpManagement(loggedEmployee).setVisible(true);
    }//GEN-LAST:event_jButtonDiscardActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        validateThenSaveEmployee();
        
    }//GEN-LAST:event_jButtonSaveActionPerformed
    
    private void showErrorLabels (boolean visible) {
        jLabelFirstNameError.setVisible(visible);
        jLabelLastNameError.setVisible(visible);
        jLabelBirthdayError.setVisible(visible);
        jLabelPhoneNumberError.setVisible(visible);
        jLabelAddressError.setVisible(visible);     
        jLabelPositionError.setVisible(visible);
        jLabelSupervisorError.setVisible(visible);
        jLabelBasicSalaryError.setVisible(visible);
        jLabelHourlyRateError.setVisible(visible);
        jLabelPhoneAllowanceError.setVisible(visible);
        jLabelGrossSemiMonthlyError.setVisible(visible);
        jLabelRiceSubisdyError.setVisible(visible);
        jLabelClothingAllowanceError.setVisible(visible);
        jLabelSssNumberError.setVisible(visible);
        jLabelPagibigNumberError.setVisible(visible);
        jLabelPhilhealthNumberError.setVisible(visible);
        jLabelTinNumberError.setVisible(visible); 
    }
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDiscard;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox<String> jComboBoxRole;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private com.toedter.calendar.JDateChooser jDateChooserBirthday;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAddressError;
    private javax.swing.JLabel jLabelBasicSalaryError;
    private javax.swing.JLabel jLabelBirthdayError;
    private javax.swing.JLabel jLabelClothingAllowanceError;
    private javax.swing.JLabel jLabelFirstNameError;
    private javax.swing.JLabel jLabelGrossSemiMonthlyError;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelHourlyRateError;
    private javax.swing.JLabel jLabelLastNameError;
    private javax.swing.JLabel jLabelPagibigNumberError;
    private javax.swing.JLabel jLabelPhilhealthNumberError;
    private javax.swing.JLabel jLabelPhoneAllowanceError;
    private javax.swing.JLabel jLabelPhoneNumberError;
    private javax.swing.JLabel jLabelPositionError;
    private javax.swing.JLabel jLabelRiceSubisdyError;
    private javax.swing.JLabel jLabelSssNumberError;
    private javax.swing.JLabel jLabelSupervisorError;
    private javax.swing.JLabel jLabelTinNumberError;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldBasicSalary;
    private javax.swing.JTextField jTextFieldClothingAllowance;
    private javax.swing.JTextField jTextFieldFirstName;
    private javax.swing.JTextField jTextFieldGrossSemiMonthly;
    private javax.swing.JTextField jTextFieldHourlyRate;
    private javax.swing.JTextField jTextFieldLastName;
    private javax.swing.JTextField jTextFieldPagibigNumber;
    private javax.swing.JTextField jTextFieldPhilhealthNumber;
    private javax.swing.JTextField jTextFieldPhoneAllowance;
    private javax.swing.JTextField jTextFieldPhoneNumber;
    private javax.swing.JTextField jTextFieldPosition;
    private javax.swing.JTextField jTextFieldRiceSubsidy;
    private javax.swing.JTextField jTextFieldSssNumber;
    private javax.swing.JTextField jTextFieldSupervisor;
    private javax.swing.JTextField jTextFieldTinNumber;
    // End of variables declaration//GEN-END:variables
}
