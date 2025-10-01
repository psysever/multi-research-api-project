package com.research2.api.domain.global.util.excel;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ExcelSupport {

    void connect(HttpServletResponse response);

    void draw(int sheetNum, Class<?> clazz, List<?> data);

    void download(String fileName);

}
