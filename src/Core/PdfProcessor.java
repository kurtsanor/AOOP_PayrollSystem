/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Domains.Payslip;
import static com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY;
import com.itextpdf.kernel.colors.DeviceRgb;
import static com.itextpdf.kernel.colors.DeviceRgb.WHITE;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author keith
 */
public class PdfProcessor {
    public static void createPayslipPdf(Employee employee, Payslip payslip) {
        try {
            // Get Downloads folder in a cross-platform way
            String userHome = System.getProperty("user.home");
            String fileNameFormat = employee.getLastName() + "-Payslip-" + payslip.getPeriod().getMonth() + "-" + payslip.getPeriod().getYear() +".pdf";
            String filePath = userHome + File.separator + "Downloads" + File.separator + fileNameFormat;

            // Create PDF writer
            PdfWriter writer = new PdfWriter(filePath);

            // Create PDF document
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4);
                     
            Document document = new Document(pdf);
            document.add(new Paragraph("MotorPH").setFontSize(20f).setTextAlignment(TextAlignment.CENTER).setBold());
            document.add(new Paragraph("EMPLOYEE PAYSLIP").setTextAlignment(TextAlignment.CENTER));
            
            // Create a table with 2 columns (each 50% width)
            Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();
            
            DeviceRgb blue = new DeviceRgb(56,60,76);
            DeviceRgb lightGray = new DeviceRgb(240,236,236);
            
            // First row
            table.addCell(new Cell().add(new Paragraph("Employee Name: " + employee.getFirstName() + " " + employee.getLastName())));
            table.addCell("Period Month: " + payslip.getPeriod().getMonth());

            // Second row
            table.addCell("Employee ID: " + employee.getID());
            table.addCell("Period Year: " + payslip.getPeriod().getYear());
            
            document.add(table);
            document.add(new Paragraph("\n\n"));
            
            Table earnings = new Table(UnitValue.createPercentArray(new float[]{100}))
                    .useAllAvailableWidth();
            
            earnings.addCell(new Cell().add(new Paragraph("EARNINGS")).setBorder(Border.NO_BORDER).setBold().setFontColor(WHITE).setBackgroundColor(blue));           
            document.add(earnings);
            
            Table hourlyRate = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            hourlyRate.addCell(new Cell().add(new Paragraph("Hourly Rate")).setBorder(Border.NO_BORDER));
            hourlyRate.addCell(new Cell().add(new Paragraph(amountToString(employee.getHourlyRate()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(hourlyRate);
            
            Table hoursWorked = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            hoursWorked.addCell(new Cell().add(new Paragraph("Hours Worked")).setBorder(Border.NO_BORDER));
            hoursWorked.addCell(new Cell().add(new Paragraph(amountToString(payslip.getWorkHours()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(hoursWorked);
            
            Table basicSalary = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            basicSalary.addCell(new Cell().add(new Paragraph("Basic Salary")).setBorder(Border.NO_BORDER));
            basicSalary.addCell(new Cell().add(new Paragraph(amountToString(payslip.getBasicSalary()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(basicSalary);
            
            Table riceSubsidy = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            riceSubsidy.addCell(new Cell().add(new Paragraph("Rice Subsidy")).setBorder(Border.NO_BORDER));
            riceSubsidy.addCell(new Cell().add(new Paragraph(amountToString(payslip.getRiceSubsidy()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(riceSubsidy);
            
            Table phoneAllowance = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            phoneAllowance.addCell(new Cell().add(new Paragraph("Phone Allowance")).setBorder(Border.NO_BORDER));
            phoneAllowance.addCell(new Cell().add(new Paragraph(amountToString(payslip.getPhoneAllowance()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(phoneAllowance);
            
            Table clothingAllowance = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            clothingAllowance.addCell(new Cell().add(new Paragraph("Clothing Allowance")).setBorder(Border.NO_BORDER));
            clothingAllowance.addCell(new Cell().add(new Paragraph(amountToString(payslip.getClothingAllowance()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(clothingAllowance);
            
            Table grossPay = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            grossPay.addCell(new Cell().add(new Paragraph("Gross Pay")).setBorder(Border.NO_BORDER).setBold());
            grossPay.addCell(new Cell().add(new Paragraph(amountToString(payslip.getGrossPay()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold()).setBackgroundColor(lightGray);
            document.add(grossPay);
            
            document.add(new Paragraph("\n"));
            
            Table deductions = new Table(UnitValue.createPercentArray(new float[]{100}))
                    .useAllAvailableWidth();
            
            deductions.addCell(new Cell().add(new Paragraph("DEDUCTIONS")).setBorder(Border.NO_BORDER).setBold().setFontColor(WHITE).setBackgroundColor(blue));
            document.add(deductions);
            
            Table sssDeduction = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            sssDeduction.addCell(new Cell().add(new Paragraph("Social Security System")).setBorder(Border.NO_BORDER));
            sssDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getSssDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(sssDeduction);
            
            Table philhealthDeduction = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            philhealthDeduction.addCell(new Cell().add(new Paragraph("Philhealth")).setBorder(Border.NO_BORDER));
            philhealthDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getPhilhealthDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(philhealthDeduction);
            
            Table pagibigDeduction = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            pagibigDeduction.addCell(new Cell().add(new Paragraph("Pag-Ibig")).setBorder(Border.NO_BORDER));
            pagibigDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getPagibigDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(pagibigDeduction);
            
            Table taxDeduction = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            taxDeduction.addCell(new Cell().add(new Paragraph("Withholding Tax")).setBorder(Border.NO_BORDER));
            taxDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getTaxDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
            document.add(taxDeduction);
            
            Table totalDeductions = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            totalDeductions.addCell(new Cell().add(new Paragraph("Total Deductions")).setBorder(Border.NO_BORDER).setBold());
            totalDeductions.addCell(new Cell().add(new Paragraph(amountToString(payslip.getTotalDeductions()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold()).setBackgroundColor(lightGray);
            document.add(totalDeductions);
            
            document.add(new Paragraph(""));
            
            Table serparator = new Table(UnitValue.createPercentArray(new float[]{100}))
                    .useAllAvailableWidth();
            serparator.addCell(new Cell().add(new Paragraph()).setBackgroundColor(LIGHT_GRAY).setBorder(Border.NO_BORDER));
            document.add(serparator);
            
            document.add(new Paragraph(""));
            
            Table takehomePay = new Table(UnitValue.createPercentArray(new float[]{50,50}))
                    .useAllAvailableWidth();
            takehomePay.addCell(new Cell().add(new Paragraph("Take Home Pay")).setBorder(Border.NO_BORDER).setBold());
            takehomePay.addCell(new Cell().add(new Paragraph(amountToString(payslip.getNetPay()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold()).setBackgroundColor(blue).setFontColor(WHITE);
            document.add(takehomePay);
            
            document.add(new Paragraph("\n\nThis is a system generated payslip.").setTextAlignment(TextAlignment.CENTER));
            
            document.close();
                      
            File file = new File(filePath);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                File parentDirectory = file.getParentFile();
                desktop.open(parentDirectory);  // Open the directory in the file explorer
            }
            System.out.println("PDF successfully created at: " + filePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(PdfProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }           
    }
    
        private static String amountToString (double amount) {
            return String.format("%.2f", amount);
        }
    }

