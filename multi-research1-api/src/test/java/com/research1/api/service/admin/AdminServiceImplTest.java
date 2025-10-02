package com.research1.api.service.admin;


import com.research1.api.domain.jpa.service.AdminServiceImpl;
import com.research1.api.global.security.jwt.JwtTokenProvider;
import com.research1.api.domain.jpa.entity.Admin;
import com.research1.api.domain.jpa.enums.AdminStatusTypeEnum;
import com.research1.api.domain.jpa.enums.AdminTypeEnum;
import com.research1.api.domain.jpa.dto.req.CreateAdminDto;
import com.research1.api.domain.jpa.dto.res.AdminResponseDto;
import com.research1.api.global.exception.CustomException;
import com.research1.api.domain.jpa.repository.AdminRepository;
import com.research1.api.global.util.UserInfoByToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.research1.api.global.exception.error.ErrorCodes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private UserInfoByToken userInfoByToken;
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private JwtTokenProvider jwtTokenProvider;
    private AdminServiceImpl adminService;


    @BeforeEach
    void setUp() {
        adminRepository = mock(AdminRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userInfoByToken = mock(UserInfoByToken.class);
        authenticationManagerBuilder = mock(AuthenticationManagerBuilder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);

        adminService = new AdminServiceImpl(adminRepository, passwordEncoder, userInfoByToken,
                authenticationManagerBuilder, jwtTokenProvider);
    }

    @Test
    void createAdmin_shouldSaveAdminAndReturnId() {
        // given
        CreateAdminDto dto = new CreateAdminDto("admin123", "password123", "관리자");

        String hashedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(hashedPassword);

        Admin savedAdmin = Admin.builder()
                .adminId(43)
                .username("admin123")
                .password(hashedPassword)
                .name("관리자")
                .userType(AdminTypeEnum.ADMIN)
                .userStatusType(AdminStatusTypeEnum.REQUEST_ADMIN)
                .build();

        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);

        // when
        int returnedId = adminService.createAdmin(dto);

        // then
        assertThat(returnedId).isEqualTo(43);

        // 캡처해서 검증
        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(captor.capture());

        Admin adminSaved = captor.getValue();
        assertThat(adminSaved.getUsername()).isEqualTo("admin123");
        assertThat(adminSaved.getPassword()).isEqualTo("encodedPassword123");
        assertThat(adminSaved.getUserType()).isEqualTo(AdminTypeEnum.ADMIN);
    }


    @Test
    void getAdmin_shouldReturnAdmin_whenAdminExists() {
        // given
        int adminId = 27;
        Admin admin = Admin.builder()
                .adminId(adminId)
                .username("admin")
                .userType(AdminTypeEnum.ADMIN)
                .userStatusType(AdminStatusTypeEnum.REQUEST_ADMIN)
                .build();

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        // when
        AdminResponseDto foundAdmin = adminService.getAdminInfo(adminId);

        // then
        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getAdminId()).isEqualTo(adminId);
        assertThat(foundAdmin.getUsername()).isEqualTo("admin");
    }

    @Test
    void getAdmin_shouldThrowException_whenAdminNotFound() {
        // given
        int adminId = 999;
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // when + then
        CustomException exception = assertThrows(CustomException.class, () -> {
            adminService.getAdminInfo(adminId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCodes.UserErrorCode.ADMIN_CAN_NOT_FIND);
    }


}