package com.research2.api.domain.kafka.repository;


import com.research2.api.domain.kafka.dto.req.ProcessedEventDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcessedEventRepository {

    void kafkaInsert(ProcessedEventDto processedEventDto);

    int existsById(@Param("eventId") String eventId);
}




