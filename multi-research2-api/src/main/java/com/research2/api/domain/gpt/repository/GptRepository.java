package com.research2.api.domain.gpt.repository;


import com.research2.api.domain.gpt.dto.req.CreateAiReportDto;
import org.apache.ibatis.annotations.Mapper;


import java.util.Optional;


@Mapper
public interface GptRepository {

    Optional<?> userReport(int userId);

    void createAiReport(CreateAiReportDto createAiReportDto);


}
