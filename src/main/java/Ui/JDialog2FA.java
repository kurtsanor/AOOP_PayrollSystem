/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Ui;

import Dao.CredentialsDAO;
import Service.TwoFactorAuthService;
import Util.QRCodeGenerator;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author admin
 */
public class JDialog2FA extends javax.swing.JDialog {

    /**
     * Creates new form JDialog2FA
     */
    private final int employeeID;
    private String secretKey;
    public JDialog2FA(java.awt.Frame parent, boolean modal, int employeeID) {
        super(parent, modal);
        initComponents();
        this.employeeID = employeeID;
        jLabelCodeError.setVisible(false);
        try {
            generateQR();
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
    }
    
    private void showQRCode(BufferedImage image) {        
        jLabelQRCode.setIcon(new ImageIcon(image));
    }
    
    private void generateQR() throws WriterException {
        secretKey = TwoFactorAuthService.generateSecretKey();
        String QRUrl = TwoFactorAuthService.getQRUrl("MotorPH", Integer.toString(employeeID), secretKey);
        BufferedImage image = QRCodeGenerator.generateQRCodeImage(QRUrl, 300, 300);
        showQRCode(image);
    }
    
    private void showUpdateResult(boolean isSuccessful) {
        JOptionPane.showMessageDialog(this, isSuccessful? "2FA has been enabled successfully": "Failed to enable 2FA", isSuccessful? "Success": "Error", isSuccessful? JOptionPane.INFORMATION_MESSAGE: JOptionPane.ERROR_MESSAGE);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelQRCode = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField6DigitCode = new javax.swing.JTextField();
        jButtonVerify = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabelCodeError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(86, 98, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Set up Two-Factor Authentication");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(40, 35, 5, 35);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Download an authenticator app");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Android or iOS - Google Authenticator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(86, 98, 106));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabelQRCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabelQRCode, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 40, 35);
        jPanel1.add(jPanel2, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter the 6-digit code generated by the app");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        jTextField6DigitCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6DigitCode.setPreferredSize(new java.awt.Dimension(64, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 35);
        jPanel1.add(jTextField6DigitCode, gridBagConstraints);

        jButtonVerify.setBackground(new java.awt.Color(0, 183, 229));
        jButtonVerify.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonVerify.setForeground(new java.awt.Color(255, 255, 255));
        jButtonVerify.setText("Verify");
        jButtonVerify.setPreferredSize(new java.awt.Dimension(72, 35));
        jButtonVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 35, 40, 35);
        jPanel1.add(jButtonVerify, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Scan this code with the app");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabelCodeError.setForeground(new java.awt.Color(255, 102, 102));
        jLabelCodeError.setText("This is required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        jPanel1.add(jLabelCodeError, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerifyActionPerformed
        // TODO add your handling code here:
        jLabelCodeError.setVisible(false);
        int code = 0;
        try {
            code = Integer.parseInt(jTextField6DigitCode.getText());
            
            try {                 
            boolean isAuthenticated = TwoFactorAuthService.verifyCode(secretKey, code);
            
            if (!isAuthenticated) {
                jLabelCodeError.setText("Incorrect 6-digit code");
                jLabelCodeError.setVisible(true);
                return;
            }
            
            CredentialsDAO dao = new CredentialsDAO();
            boolean isUpdated = dao.updateTotpSecret(employeeID, secretKey);
            showUpdateResult(isUpdated);
            this.dispose();
            
            
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            jLabelCodeError.setText("Code should be a 6-digit integer");
            jLabelCodeError.setVisible(true);
        }
        
        
    }//GEN-LAST:event_jButtonVerifyActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonVerify;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelCodeError;
    private javax.swing.JLabel jLabelQRCode;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField6DigitCode;
    // End of variables declaration//GEN-END:variables
}
