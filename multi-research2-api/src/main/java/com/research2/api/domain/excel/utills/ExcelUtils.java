package com.research2.api.domain.excel.utills;

import com.research2.api.domain.global.util.excel.ExcelColumnName;
import com.research2.api.domain.global.util.excel.ExcelSupport;
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

    //There is MAX_ROW, which indicates the total number of data to be drawn per sheet.
    // There are variables for SXSSFWorkbook and HttpServletResponse that will be used in the connection step in the controller.

    private static final int MAX_ROW = 5000;
    private SXSSFWorkbook workbook;
    private HttpServletResponse response;


    //This method initializes the variables of the utility class.
    @Override
    public void connect(HttpServletResponse response) {
        workbook = new SXSSFWorkbook(-1);
        this.response = response;
    }


    //This method is called from the controller to draw an Excel spreadsheet.
    //It delegates the work to the internal getWorkBook method and clears the received list.
    @Override
    public void draw(int sheetNum, Class<?> clazz, List<?> data) {
        try {
            getWorkBook(sheetNum, clazz, findHeaderNames(clazz), data);

        } catch (IOException | IllegalAccessException e) {
            log.error("Excel Download Error Message = {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    //This method is called from the controller to ultimately download the Excel file.
//Additionally, any resources used will be returned.
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


    //This method calls methods that draw the sheet's header and body, and periodically flushes them. There's not much difference from Part 1, but the process for creating the sheetName variable is slightly different.
    private SXSSFWorkbook getWorkBook(int sheetNum, Class<?> clazz, List<String> headerNames,
                                      List<?> data) throws IllegalAccessException, IOException {
        //MAX_ROW per sheet
        String sheetName = "Device List" + (sheetNum + 1);

        SXSSFSheet sheet =
                ObjectUtils.isEmpty(this.workbook.getSheet(sheetName)) ? this.workbook.createSheet(
                        sheetName) : workbook.getSheet(sheetName);

        Row row = null;

        row = sheet.createRow(0);
        createHeaders(row, headerNames);
        // Create body only when data is present
        if (!data.isEmpty()) {
            createBody(clazz, data, sheet);
        }

        // Periodic flush progress
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
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);       // Horizontal center alignment
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // selo gaunde jeonglyeol // Vertically center aligned


        // setting border
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);

        // ground color
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


    //The createBody method draws the contents of an Excel file.
//This method draws the data and allows you to observe the periodic flushing process.
    private void createBody(Class<?> clazz, List<?> data, Sheet sheet)
            throws IllegalAccessException {
        CellStyle bodyCellStyle = this.workbook.createCellStyle();
        bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);       // Horizontal center alignment
        bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Vertically center aligned
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
