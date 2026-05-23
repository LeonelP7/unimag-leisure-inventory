package com.unimag.unimagleisureinventory.services;

import java.time.LocalDateTime;

public interface ReportService {

    byte[] exportCheckOutsPdf(LocalDateTime from, LocalDateTime to);
    byte[] exportCheckOutsExcel(LocalDateTime from, LocalDateTime to);
    byte[] exportPenaltiesPdf(LocalDateTime from, LocalDateTime to);
    byte[] exportPenaltiesExcel(LocalDateTime from, LocalDateTime to);
}
