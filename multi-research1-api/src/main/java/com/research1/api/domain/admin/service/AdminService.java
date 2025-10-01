package com.research1.api.domain.admin.service;


import com.research1.api.domain.admin.dto.res.AdminListResponseDto;
import com.research1.api.global.dto.req.CustomPaginationDto;
import com.research1.api.global.security.jwt.Token;
import com.research1.api.domain.admin.dto.req.AdminLoginDto;
import com.research1.api.domain.admin.dto.req.CreateAdminDto;
import com.research1.api.domain.admin.dto.req.UpdateAdminDto;
import com.research1.api.domain.admin.dto.res.AdminResponseDto;
import org.springframework.http.ResponseEntity;


public interface AdminService {

    ResponseEntity<Token> authAdminUser(AdminLoginDto adminLoginDto);

    int createAdmin(CreateAdminDto createAdminDto);

    AdminResponseDto getAdminInfo(int adminId);

    AdminResponseDto loginAdminInfo();

    int updateAdmin(UpdateAdminDto updateAdminDto);

    void deleteAdmin(int adminId);

    AdminListResponseDto getAdminList(CustomPaginationDto customPaginationDto);

}
