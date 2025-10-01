package com.research2.api.domain.excel.service;


import com.research2.api.domain.excel.repository.ExcelRepository;
import com.research2.api.domain.excel.utills.ExcelUtils;
import com.research2.api.domain.fcm_push_message.entity.DeviceInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service("ExcelServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ExcelService {
    private static final int MAX_ROW = 5000;
    private final ExcelRepository excelRepository;
    private final ExcelUtils excelUtils;

    public void excelDownload(
            HttpServletResponse response) {
        final int pageSize = MAX_ROW;
        int total = excelRepository.findAllDeviceCount();
        excelUtils.connect(response);


        //The header is printed even when there is no data
        if (total == 0) {
            excelUtils.draw(0, DeviceInfo.class, new ArrayList<>());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            excelUtils.download("device list-" + formattedDate);
            return;
        }

        // Divide drawing into multiple sheets (pages)
        int sheetIndex = 0;
        for (int offset = 0; offset < total; offset += pageSize) {


            List<DeviceInfo> rawList = excelRepository.findAllDevice();

            excelUtils.draw(sheetIndex, DeviceInfo.class, rawList);
            sheetIndex++;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        excelUtils.download("device list-" + formattedDate);
    }


}

