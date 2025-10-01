package com.research2.api.domain.excel.service;


import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {
    void excelDownload(HttpServletResponse response);

}
