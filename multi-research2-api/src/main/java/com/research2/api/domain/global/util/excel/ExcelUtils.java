package com.research2.api.domain.global.util.excel;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExcelUtils implements ExcelSupport {

    // MAX_ROW indicates the maximum number of rows to be drawn per sheet.
// There are variables for SXSSFWorkbook and HttpServletResponse
// that will be used in the connection step in the controller.
    private static final int MAX_ROW = 5000;
    private SXSSFWorkbook workbook;
    private HttpServletResponse response;


    // Initialize streaming workbook
    @Override
    public void connect(HttpServletResponse response) {
        workbook = new SXSSFWorkbook(-1);
        this.response = response;
    }


    // Draw Excel sheet with data chunk
    @Override
    public void draw(int sheetNum, Class<?> clazz, List<?> data) {
        try {
            getWorkBook(sheetNum, clazz, findHeaderNames(clazz), data);

        } catch (IOException | IllegalAccessException e) {
            log.error("Excel Download Error Message = {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // Stream to client and cleanup
    @Override
    public void download(String fileName) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // 파일 이름을 URL 인코딩
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition",
                "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Create sheet and flush periodically
    private SXSSFWorkbook getWorkBook(int sheetNum, Class<?> clazz, List<String> headerNames,
                                      List<?> data) throws IllegalAccessException, IOException {
        // 각 시트 당 MAX_ROW 개씩
        String sheetName = "Order List" + (sheetNum + 1);

        SXSSFSheet sheet =
                ObjectUtils.isEmpty(this.workbook.getSheet(sheetName)) ? this.workbook.createSheet(
                        sheetName) : workbook.getSheet(sheetName);

        Row row = null;

        row = sheet.createRow(0);
        createHeaders(row, headerNames);
        // exist data
        if (!data.isEmpty()) {
            createBody(clazz, data, sheet);
        }

        // After writing rows to sheet
        sheet.flushRows(MAX_ROW);
        return this.workbook;
    }


    //The role of this method is to draw a header for each sheet.
    private void createHeaders(Row row, List<String> headerNames) {
        /**
         * header font style
         */
        Font font = this.workbook.createFont();
        font.setFontHeightInPoints((short) 11);  // Set a standard font size
        font.setColor(IndexedColors.BLACK.getIndex());  // Use IndexedColors instead of hardcoded values

        /**
         * header cell style
         */
        CellStyle headerCellStyle = this.workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);       // horizontally centered
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // vertically center aligned

        // set border
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);

        // foreground color
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(font);

        Cell cell;
        for (int i = 0, size = headerNames.size(); i < size; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerCellStyle);
            cell.setCellValue(headerNames.get(i));
            row.getSheet().setColumnWidth(i, 256 * 30);
            row.setHeightInPoints(25);  // Adjust the height value as needed

        }
    }

    // The createBody method draws the contents of an Excel file.
// This method writes the data and performs periodic flushing.
    private void createBody(Class<?> clazz, List<?> data, Sheet sheet)
            throws IllegalAccessException {
        CellStyle bodyCellStyle = this.workbook.createCellStyle();
        bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);       // horizontally centered
        bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // vertically center aligned
        int startRow = 0;
        Row row;
        for (Object o : data) {
            List<Object> fields = findFieldValue(clazz, o);
            row = sheet.createRow(++startRow);

            Cell cell;
            for (int i = 0, fieldSize = fields.size(); i < fieldSize; i++) {
                cell = row.createCell(i);
                cell.setCellValue(String.valueOf(fields.get(i)));
                cell.setCellStyle(bodyCellStyle);
            }
        }
    }

    /**
     * Logic for finding Excel header names
     */

    private List<String> findHeaderNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumnName.class))
                .map(field -> field.getAnnotation(ExcelColumnName.class).name())
                .collect(Collectors.toList());
    }

    /**
     * Method for extracting data values
     */
    private List<Object> findFieldValue(Class<?> clazz, Object obj) throws IllegalAccessException {
        List<Object> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            result.add(field.get(obj));
        }
        return result;
    }

}
