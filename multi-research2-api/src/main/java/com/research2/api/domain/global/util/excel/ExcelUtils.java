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

    //한 시트당 그려줄 데이터의 총 갯수를 의미하는 MAX_ROW가 있습니다.
    //컨트롤러에서 연결 단계에서 사용할 SXSSFWorkbook의 변수와 HttpServletResponse 변수가 있습니다.
    private static final int MAX_ROW = 5000;
    private SXSSFWorkbook workbook;
    private HttpServletResponse response;


    //해당 메서드는 유틸 클래스의 변수를 초기화해주는 역할을 수행합니다.
    @Override
    public void connect(HttpServletResponse response) {
        workbook = new SXSSFWorkbook(-1);
        this.response = response;
    }


    //해당 메서드는 컨트롤러에서 엑셀을 그려주기 위해 호출되는 메서드입니다.
    //내부 메서드인 getWorkBook 메서드에게 수행할 내용을 위임하고, 받아온 list를 clear하게 됩니다.
    @Override
    public void draw(int sheetNum, Class<?> clazz, List<?> data) {
        try {
            getWorkBook(sheetNum, clazz, findHeaderNames(clazz), data);

        } catch (IOException | IllegalAccessException e) {
            log.error("Excel Download Error Message = {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    //해당 메서드는 컨트롤러에서 최종적으로 엑셀을 다운로드하기 위해 호출되는 메서드입니다.
    //추가적으로 사용한 자원을 반납하게 됩니다.
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

    //해당 메서드는 시트의 헤더와 바디를 그려주는 메서드를 호출하고 주기적으로 flush를 진행하고 있습니다. 1편과 크게 차이점은 없지만 sheetName 변수를 만드는 과정이 살짝 다릅니다.
    private SXSSFWorkbook getWorkBook(int sheetNum, Class<?> clazz, List<String> headerNames,
                                      List<?> data) throws IllegalAccessException, IOException {
        // 각 시트 당 MAX_ROW 개씩
        String sheetName = "주문 목록" + (sheetNum + 1);

        SXSSFSheet sheet =
                ObjectUtils.isEmpty(this.workbook.getSheet(sheetName)) ? this.workbook.createSheet(
                        sheetName) : workbook.getSheet(sheetName);

        Row row = null;

        row = sheet.createRow(0);
        createHeaders(row, headerNames);
        // 데이터가 있을 때만 바디를 생성
        if (!data.isEmpty()) {
            createBody(clazz, data, sheet);
        }

        // 주기적인 flush 진행
        sheet.flushRows(MAX_ROW);
        return this.workbook;
    }

    //해당 메서드의 역할은 각 시트당 헤더를 그려주기 위한 메서드입니다.
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
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);       // 가로 가운데 정렬
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 가운데 정렬

        // 테두리 설정
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);

        // 배경 설정
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

    //createBody 메서드는 엑셀의 내용을 그려주는 메서드입니다.
    //해당 메서드는 데이터를 그려주며 주기적으로 flush하는 과정을 살펴볼 수 있습니다.
    private void createBody(Class<?> clazz, List<?> data, Sheet sheet)
            throws IllegalAccessException {
        CellStyle bodyCellStyle = this.workbook.createCellStyle();
        bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);       // 가로 가운데 정렬
        bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 가운데 정렬
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
     * 엑셀의 헤더 명칭을 찾는 로직
     */
    private List<String> findHeaderNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumnName.class))
                .map(field -> field.getAnnotation(ExcelColumnName.class).name())
                .collect(Collectors.toList());
    }

    /**
     * 데이터의 값을 추출하는 메서드
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
