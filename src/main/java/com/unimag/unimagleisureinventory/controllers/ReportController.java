package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/checkouts")
    public ResponseEntity<byte[]> checkoutReport(
            @RequestParam String format,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        byte[] data = format.equals("pdf")
                ? reportService.exportCheckOutsPdf(from, to)
                : reportService.exportCheckOutsExcel(from, to);
        String contentType = format.equals("pdf") ? "application/pdf"
                : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(data);
    }

    @GetMapping("/penalties")
    public ResponseEntity<byte[]> penaltyReport(
            @RequestParam String format,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        byte[] data = format.equals("pdf")
                ? reportService.exportPenaltiesPdf(from, to)
                : reportService.exportPenaltiesExcel(from, to);
        String contentType = format.equals("pdf") ? "application/pdf"
                : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(data);
    }
}
