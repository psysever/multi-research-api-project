package com.research1.api.domain.file.service;


import com.research1.api.domain.file.dto.req.FilesDeleteDto;
import com.research1.api.domain.file.dto.req.FilesUploadDto;
import com.research1.api.domain.file.entity.Files;


import java.io.IOException;


public interface FilesService {

    Files getFile(int fileId);


    int uploadFile(FilesUploadDto fileUploadDto)
            throws IOException;


    void deleteFile(FilesDeleteDto filesDeleteDto);
}
