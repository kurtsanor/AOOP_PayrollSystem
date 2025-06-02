/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Domains.PayrollEntry;
import Domains.PayrollSummary;
import Domains.Payslip;
import Domains.YearPeriod;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import static com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY;
import com.itextpdf.kernel.colors.DeviceRgb;
import static com.itextpdf.kernel.colors.DeviceRgb.BLACK;
import static com.itextpdf.kernel.colors.DeviceRgb.WHITE;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
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
            String imagePath = "src/Images/motorphlogo.png";
            
            // Create PDF writer
            PdfWriter writer = new PdfWriter(filePath);

            // Create PDF document
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4);
            
            DeviceRgb blue = new DeviceRgb(56,60,76);
            DeviceRgb lightGray = new DeviceRgb(248,244,244);
            
            try (Document document = new Document(pdf)) {
                ImageData data = ImageDataFactory.create(imagePath);
                Image image = new Image(data);
                image.setWidth(120);
                image.setHeight(120);
                
                Table headerTable = create3ColumnTable();
                headerTable.addCell(new Cell().add(image.setHorizontalAlignment(HorizontalAlignment.LEFT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("MotorPH").setFontSize(30f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .add(new Paragraph("EMPLOYEE PAYSLIP")).setTextAlignment(TextAlignment.CENTER));
                headerTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                
                document.add(headerTable);
                
                Table info = create4ColumnTable();
                
                info.addCell(new Cell().add(new Paragraph("Employee ID")).setBackgroundColor(blue).setFontColor(WHITE).setBorder(Border.NO_BORDER).setBold());
                info.addCell(new Cell().add(new Paragraph(""+payslip.getEmployee().getID())));
                info.addCell(new Cell().add(new Paragraph("Period Month")).setBackgroundColor(blue).setFontColor(WHITE).setBorder(Border.NO_BORDER).setBold());
                info.addCell(new Cell().add(new Paragraph(""+payslip.getPeriod().getMonth())));
                
                info.addCell(new Cell().add(new Paragraph("Employee Name")).setBackgroundColor(blue).setFontColor(WHITE).setBorder(Border.NO_BORDER).setBold());
                info.addCell(new Cell().add(new Paragraph(payslip.getEmployee().getFirstName() + " " + payslip.getEmployee().getLastName())));
                info.addCell(new Cell().add(new Paragraph("Period Year")).setBackgroundColor(blue).setFontColor(WHITE).setBorder(Border.NO_BORDER).setBold());
                info.addCell(new Cell().add(new Paragraph(""+payslip.getPeriod().getYear())));
                
                document.add(info);
                               
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
                
                document.add(new Paragraph("\n"));
                
                Table separator = create1ColumnTable();
                separator.addCell(new Cell().add(new Paragraph()).setBackgroundColor(BLACK).setBorder(Border.NO_BORDER));
                document.add(separator);
                
                document.add(new Paragraph(""));
                
                Table takehomePay = create2ColumnTable();
                takehomePay.addCell(new Cell().add(new Paragraph("Take Home Pay")).setBorder(Border.NO_BORDER).setBold());
                takehomePay.addCell(new Cell().add(new Paragraph(amountToString(payslip.getNetPay()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold());
                document.add(takehomePay);
                
                document.add(new Paragraph("\n\nThis is a system generated payslip.").setTextAlignment(TextAlignment.CENTER));
            } catch (MalformedURLException ex) {
                Logger.getLogger(PdfProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // redirect to the folder containing the pdf
            File file = new File(filePath);
            redirectToDirectory(file);
            
            System.out.println("PDF successfully created at: " + file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }           
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
    
    private static Table create9ColumnTable () {
        return new Table(UnitValue.createPercentArray(new float[]{10,15,15,10,10,10,10,10,10}))
            .useAllAvailableWidth();
    }
    
    private static Table create3ColumnTable () {
        return new Table(UnitValue.createPercentArray(new float[]{33,33,33}))
            .useAllAvailableWidth();
    }
    
    private static Table create4ColumnTable () {
        return new Table(UnitValue.createPercentArray(new float[]{25,25,25,25}))
            .useAllAvailableWidth();
    }
    
    public static void createPayrollReportPdf (List<PayrollEntry> payrollEntries , YearPeriod period, PayrollSummary summary) {
        try {
            String [] colNames = {"Emp ID", "Name", "Position", "Gross Pay", "SSS", "Philhealth", "Pagibig", "Tax", "Net Pay"};
            
            String fileNameFormat = period.getMonth() + "-" + period.getYear() + "-Payroll-Summary-Report.pdf";
            String filePath = "generated reports" + File.separator + fileNameFormat;
            String imagePath = "src/Images/motorphlogo.png";
            
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(PageSize.A4.rotate());
            
            
            Document document = new Document(pdf);
            
            DeviceRgb blue = new DeviceRgb(56,60,76);
            
            ImageData data = ImageDataFactory.create(imagePath);
                Image image = new Image(data);
                image.setWidth(120);
                image.setHeight(120);
                
                Table headerTable = create3ColumnTable();
                headerTable.addCell(new Cell().add(image.setHorizontalAlignment(HorizontalAlignment.LEFT)).setBorder(Border.NO_BORDER));
                headerTable.addCell(new Cell().add(new Paragraph("MotorPH").setFontSize(30f).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .add(new Paragraph("MONTHLY PAYROLL SUMMARY REPORT")).setTextAlignment(TextAlignment.CENTER));
                headerTable.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
                
                document.add(headerTable);
            
            Table columnNames = create9ColumnTable();
            for (String colName: colNames) {
                columnNames.addCell(new Cell().add(new Paragraph(colName).setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(10f)).setBorder(Border.NO_BORDER).setBackgroundColor(blue).setFontColor(WHITE));
            }
            
            document.add(columnNames);
            
            DeviceRgb lightGray = new DeviceRgb(248,244,244);
            
            int rowNumber = 1;
            
                       
            Table newTable = create9ColumnTable();
            
            for (PayrollEntry entry: payrollEntries) {    
                boolean isEvenRow = rowNumber % 2 == 0;
                DeviceRgb rowBg = isEvenRow ? lightGray : new DeviceRgb(255,255,255);
                
                newTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getEmployeeID())).setFontSize(9f)).setBorder(Border.NO_BORDER).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(entry.getFullName()).setFontSize(9f)).setBorder(Border.NO_BORDER).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(entry.getPosition()).setFontSize(9f)).setBorder(Border.NO_BORDER).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(entry.getGrossIncome())).setFontSize(9f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(entry.getSss())).setFontSize(9f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(entry.getPhilhealth())).setFontSize(9f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(entry.getPagibig())).setFontSize(9f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(entry.getWithholdingTax())).setFontSize(9f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(rowBg));
                newTable.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(entry.getNetPay())).setFontSize(9f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(rowBg));
                
               
                rowNumber++;
            } 
            
            document.add(newTable);
            
            Table separator = create1ColumnTable();
            separator.addCell(new Cell().add(new Paragraph()).setBackgroundColor(BLACK).setBorder(Border.NO_BORDER));
            document.add(separator);
            
            // add summary total at the bottom
            Table summaryTable = create9ColumnTable();
            populateSummaryTable(summaryTable, summary);
            document.add(summaryTable);
                                  
            document.close();
            
            File file = new File(filePath);
            redirectToDirectory(file);
            
            System.out.println("PDF successfully created at: " + file.getAbsolutePath());
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException ex) {
            Logger.getLogger(PdfProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void populateSummaryTable (Table table, PayrollSummary summary) {
        table.addCell(new Cell().add(new Paragraph("TOTAL")).setBorder(Border.NO_BORDER).setFontSize(9f).setBold());
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER).setFontSize(9f).setBold());
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER).setFontSize(9f).setBold());
        table.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(summary.getTotalGrossIncome()))).setBorder(Border.NO_BORDER).setFontSize(9f).setBold().setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(summary.getTotalSSS()))).setBorder(Border.NO_BORDER).setFontSize(9f).setBold().setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(summary.getTotalPhilhealth()))).setBorder(Border.NO_BORDER).setFontSize(9f).setBold().setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(summary.getTotalPagibig()))).setBorder(Border.NO_BORDER).setFontSize(9f).setBold().setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(summary.getTotalTax()))).setBorder(Border.NO_BORDER).setFontSize(9f).setBold().setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(PayrollCalculator.formatAmount(summary.getTotalNetPay()))).setBorder(Border.NO_BORDER).setFontSize(9f).setBold().setTextAlignment(TextAlignment.RIGHT));
    }

        
}

