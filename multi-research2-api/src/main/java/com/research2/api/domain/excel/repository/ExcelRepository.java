package com.research2.api.domain.excel.repository;


import com.research2.api.domain.fcm_push_message.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ExcelRepository {

    List<DeviceInfo> findAllDevice();

    int findAllDeviceCount();


}
