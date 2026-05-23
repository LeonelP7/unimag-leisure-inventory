package com.unimag.unimagleisureinventory.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unimag.unimagleisureinventory.exceptions.BusinessException;
import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.repositories.CheckOutRepository;
import com.unimag.unimagleisureinventory.repositories.PenaltyRepository;
import com.unimag.unimagleisureinventory.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CheckOutRepository checkOutRepository;
    private final PenaltyRepository penaltyRepository;

    // PDF de préstamos
    public byte[] exportCheckOutsPdf(LocalDateTime from, LocalDateTime to) {
        validateDateRange(from, to);
        List<CheckOut> checkOuts = (List<CheckOut>) checkOutRepository.findAll();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Préstamos"));
            document.add(new Paragraph("Generado: " + LocalDateTime.now()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Headers
            Stream.of("ID", "Estudiante", "Artículo", "Fecha", "Estado")
                    .forEach(h -> {
                        PdfPCell cell = new PdfPCell(new Phrase(h));
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    });

            // Rows
            for (CheckOut c : checkOuts) {
                table.addCell(c.getCheckOutId().toString());
                table.addCell(c.getReservation() != null
                        ? c.getReservation().getStudent().getPerson().getFirstName()
                        : "Préstamo directo");
                table.addCell(c.getReservation() != null
                        ? c.getReservation().getItem().getName()
                        : "-");
                table.addCell(c.getCheckOutDate().toString());
                table.addCell(c.getStatus().name());
            }

            document.add(table);
            document.close();
            return out.toByteArray();

        } catch (DocumentException e) {
            throw new BusinessException("Error generating PDF", e);
        }
    }

    // Excel de préstamos
    public byte[] exportCheckOutsExcel(LocalDateTime from, LocalDateTime to) {
        validateDateRange(from, to);
        List<CheckOut> checkOuts = (List<CheckOut>) checkOutRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Préstamos");

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Estudiante", "Artículo", "Fecha CheckOut",
                    "Fecha Límite", "Fecha Devolución", "Estado"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Data rows
            int rowNum = 1;
            for (CheckOut c : checkOuts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(c.getCheckOutId().toString());
                row.createCell(1).setCellValue(c.getReservation() != null
                        ? c.getReservation().getStudent().getPerson().getFirstName()
                        + " " + c.getReservation().getStudent().getPerson().getLastName()
                        : "Préstamo directo");
                row.createCell(2).setCellValue(c.getReservation() != null
                        ? c.getReservation().getItem().getName() : "-");
                row.createCell(3).setCellValue(c.getCheckOutDate().toString());
                row.createCell(4).setCellValue(c.getDueDate().toString());
                row.createCell(5).setCellValue(c.getCheckInDate() != null
                        ? c.getCheckInDate().toString() : "Pendiente");
                row.createCell(6).setCellValue(c.getStatus().name());
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new BusinessException("Error generating Excel", e);
        }
    }

    // PDF de sanciones
    public byte[] exportPenaltiesPdf(LocalDateTime from, LocalDateTime to) {
        validateDateRange(from, to);
        List<Penalty> penalties = (List<Penalty>) penaltyRepository.findAll();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Sanciones"));
            document.add(new Paragraph("Generado: " + LocalDateTime.now()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            Stream.of("ID", "Estudiante", "Tipo", "Motivo", "Estado")
                    .forEach(h -> {
                        PdfPCell cell = new PdfPCell(new Phrase(h));
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    });

            for (Penalty p : penalties) {
                table.addCell(p.getPenaltyId().toString());
                table.addCell(p.getStudent().getPerson().getFirstName()
                        + " " + p.getStudent().getPerson().getLastName());
                table.addCell(p.getPenaltyType().getName());
                table.addCell(p.getReason());
                table.addCell(p.getPenaltyStatus().name());
            }

            document.add(table);
            document.close();
            return out.toByteArray();

        } catch (DocumentException e) {
            throw new BusinessException("Error generating PDF", e);
        }
    }

    // Excel de sanciones
    public byte[] exportPenaltiesExcel(LocalDateTime from, LocalDateTime to) {
        validateDateRange(from, to);
        List<Penalty> penalties = (List<Penalty>) penaltyRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sanciones");

            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Estudiante", "Tipo", "Motivo",
                    "Fecha Inicio", "Fecha Fin", "Estado"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowNum = 1;
            for (Penalty p : penalties) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getPenaltyId().toString());
                row.createCell(1).setCellValue(p.getStudent().getPerson().getFirstName()
                        + " " + p.getStudent().getPerson().getLastName());
                row.createCell(2).setCellValue(p.getPenaltyType().getName());
                row.createCell(3).setCellValue(p.getReason());
                row.createCell(4).setCellValue(p.getStartDate().toString());
                row.createCell(5).setCellValue(p.getEndDate() != null
                        ? p.getEndDate().toString() : "Activa");
                row.createCell(6).setCellValue(p.getPenaltyStatus().name());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new BusinessException("Error generating Excel", e);
        }
    }

    private void validateDateRange(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new BusinessException("Date range is required");
        }
        if (from.isAfter(to)) {
            throw new BusinessException("Start date must be before end date");
        }
        if (ChronoUnit.MONTHS.between(from, to) > 6) {
            throw new BusinessException("Date range cannot exceed 6 months");
        }
    }
}
