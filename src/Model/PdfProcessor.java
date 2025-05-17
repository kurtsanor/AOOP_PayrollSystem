/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

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
    public static void createPayslipPdf(Payslip payslip) {
        try {

            String fileNameFormat = payslip.getEmployee().getLastName()+ "-Payslip-" + payslip.getPeriod().getMonth() + "-" + payslip.getPeriod().getYear() +".pdf";
            String filePath = "generated payslips" + File.separator + fileNameFormat;
            
            // Create PDF writer
            PdfWriter writer = new PdfWriter(filePath);

            // Create PDF document
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4);
            
            try (Document document = new Document(pdf)) {
                document.add(new Paragraph("MotorPH").setFontSize(20f).setTextAlignment(TextAlignment.CENTER).setBold());
                document.add(new Paragraph("EMPLOYEE PAYSLIP").setTextAlignment(TextAlignment.CENTER));
                
                Table table = create2ColumnTable();
                
                DeviceRgb blue = new DeviceRgb(56,60,76);
                DeviceRgb lightGray = new DeviceRgb(240,236,236);
                
                table.addCell(new Cell().add(new Paragraph("Employee Name: " + payslip.getEmployee().getFirstName() + " " + payslip.getEmployee().getLastName())));
                table.addCell("Period Month: " + payslip.getPeriod().getMonth());

                table.addCell("Employee ID: " + payslip.getEmployee().getID());
                table.addCell("Period Year: " + payslip.getPeriod().getYear());
                
                document.add(table);
                document.add(new Paragraph("\n\n"));
                
                Table earnings = create1ColumnTable();               
                earnings.addCell(new Cell().add(new Paragraph("EARNINGS")).setBorder(Border.NO_BORDER).setBold().setFontColor(WHITE).setBackgroundColor(blue));
                document.add(earnings);
                
                Table hourlyRate = create2ColumnTable();
                hourlyRate.addCell(new Cell().add(new Paragraph("Hourly Rate")).setBorder(Border.NO_BORDER));
                hourlyRate.addCell(new Cell().add(new Paragraph(amountToString(payslip.getEmployee().getHourlyRate()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(hourlyRate);
                
                Table hoursWorked = create2ColumnTable();
                hoursWorked.addCell(new Cell().add(new Paragraph("Hours Worked")).setBorder(Border.NO_BORDER));
                hoursWorked.addCell(new Cell().add(new Paragraph(amountToString(payslip.getWorkHours()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(hoursWorked);
                
                Table basicSalary = create2ColumnTable();
                basicSalary.addCell(new Cell().add(new Paragraph("Basic Salary")).setBorder(Border.NO_BORDER));
                basicSalary.addCell(new Cell().add(new Paragraph(amountToString(payslip.getBasicSalary()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(basicSalary);
                
                Table riceSubsidy = create2ColumnTable();
                riceSubsidy.addCell(new Cell().add(new Paragraph("Rice Subsidy")).setBorder(Border.NO_BORDER));
                riceSubsidy.addCell(new Cell().add(new Paragraph(amountToString(payslip.getRiceSubsidy()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(riceSubsidy);
                
                Table phoneAllowance = create2ColumnTable();
                phoneAllowance.addCell(new Cell().add(new Paragraph("Phone Allowance")).setBorder(Border.NO_BORDER));
                phoneAllowance.addCell(new Cell().add(new Paragraph(amountToString(payslip.getPhoneAllowance()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(phoneAllowance);
                
                Table clothingAllowance = create2ColumnTable();
                clothingAllowance.addCell(new Cell().add(new Paragraph("Clothing Allowance")).setBorder(Border.NO_BORDER));
                clothingAllowance.addCell(new Cell().add(new Paragraph(amountToString(payslip.getClothingAllowance()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(clothingAllowance);
                
                Table grossPay = create2ColumnTable();
                grossPay.addCell(new Cell().add(new Paragraph("Gross Pay")).setBorder(Border.NO_BORDER).setBold());
                grossPay.addCell(new Cell().add(new Paragraph(amountToString(payslip.getGrossPay()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold()).setBackgroundColor(lightGray);
                document.add(grossPay);
                
                document.add(new Paragraph("\n"));
                
                Table deductions = create1ColumnTable();             
                deductions.addCell(new Cell().add(new Paragraph("DEDUCTIONS")).setBorder(Border.NO_BORDER).setBold().setFontColor(WHITE).setBackgroundColor(blue));
                document.add(deductions);
                
                Table sssDeduction = create2ColumnTable();
                sssDeduction.addCell(new Cell().add(new Paragraph("Social Security System")).setBorder(Border.NO_BORDER));
                sssDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getSssDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(sssDeduction);
                
                Table philhealthDeduction = create2ColumnTable();
                philhealthDeduction.addCell(new Cell().add(new Paragraph("Philhealth")).setBorder(Border.NO_BORDER));
                philhealthDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getPhilhealthDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(philhealthDeduction);
                
                Table pagibigDeduction = create2ColumnTable();
                pagibigDeduction.addCell(new Cell().add(new Paragraph("Pag-Ibig")).setBorder(Border.NO_BORDER));
                pagibigDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getPagibigDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(pagibigDeduction);
                
                Table taxDeduction = create2ColumnTable();
                taxDeduction.addCell(new Cell().add(new Paragraph("Withholding Tax")).setBorder(Border.NO_BORDER));
                taxDeduction.addCell(new Cell().add(new Paragraph(amountToString(payslip.getTaxDeduction()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                document.add(taxDeduction);
                
                Table totalDeductions = create2ColumnTable();
                totalDeductions.addCell(new Cell().add(new Paragraph("Total Deductions")).setBorder(Border.NO_BORDER).setBold());
                totalDeductions.addCell(new Cell().add(new Paragraph(amountToString(payslip.getTotalDeductions()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold()).setBackgroundColor(lightGray);
                document.add(totalDeductions);
                
                document.add(new Paragraph(""));
                
                Table serparator = create1ColumnTable();
                serparator.addCell(new Cell().add(new Paragraph()).setBackgroundColor(LIGHT_GRAY).setBorder(Border.NO_BORDER));
                document.add(serparator);
                
                document.add(new Paragraph(""));
                
                Table takehomePay = create2ColumnTable();
                takehomePay.addCell(new Cell().add(new Paragraph("Take Home Pay")).setBorder(Border.NO_BORDER).setBold());
                takehomePay.addCell(new Cell().add(new Paragraph(amountToString(payslip.getNetPay()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold()).setBackgroundColor(blue).setFontColor(WHITE);
                document.add(takehomePay);
                
                document.add(new Paragraph("\n\nThis is a system generated payslip.").setTextAlignment(TextAlignment.CENTER));
            }
            
            // redirect to the folder containing the pdf
            File file = new File(filePath);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                File parentDirectory = file.getParentFile();
                desktop.open(parentDirectory);
            }
            System.out.println("PDF successfully created at: " + file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(PdfProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }           
    }
    
        private static String amountToString (double amount) {
            return String.format("%.2f", amount);
        }
        
        private static Table create2ColumnTable() {
            return new Table(UnitValue.createPercentArray(new float[]{50,50}))
                        .useAllAvailableWidth();
        }
        
        private static Table create1ColumnTable() {
            return new Table(UnitValue.createPercentArray(new float[]{100}))
                        .useAllAvailableWidth();
        }
        
    }

