package com.research1.api.domain.admin.service;


import com.research1.api.global.security.jwt.JwtTokenProvider;
import com.research1.api.global.security.jwt.Token;
import com.research1.api.domain.admin.entity.Admin;
import com.research1.api.domain.admin.enums.AdminStatusTypeEnum;
import com.research1.api.domain.admin.enums.AdminTypeEnum;
import com.research1.api.domain.admin.dto.req.AdminLoginDto;
import com.research1.api.domain.admin.dto.req.CreateAdminDto;
import com.research1.api.domain.admin.dto.req.UpdateAdminDto;
import com.research1.api.global.dto.req.CustomPaginationDto;
import com.research1.api.domain.admin.dto.res.AdminListResponseDto;
import com.research1.api.domain.admin.dto.res.AdminResponseDto;
import com.research1.api.global.exception.error.ErrorCodes;
import com.research1.api.global.exception.CustomException;
import com.research1.api.domain.admin.repository.AdminRepository;
import com.research1.api.global.util.UserInfoByToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service("adminsServiceImpl")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService, UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoByToken userInfoByToken;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .filter(a -> a.getDelFl() == 0)
                .orElseThrow(() -> new CustomException(ErrorCodes.UserErrorCode.ADMIN_CAN_NOT_FIND));
        if (AdminStatusTypeEnum.REQUEST_ADMIN.equals(admin.getUserStatusType())) {
            throw new CustomException(ErrorCodes.UserErrorCode.ADMIN_CAN_NOT_ACCESS);
        } else {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(admin.getUsername(), admin.getPassword(), authorities);
        }
    }

    @Transactional
    public ResponseEntity<Token> authAdminUser(AdminLoginDto adminLoginDto) {
        // 1. 유효한 admin 조회 (삭제 안 된 경우만 허용)
        Admin admin = adminRepository.findByUsername(adminLoginDto.getUsername())
                .filter(a -> a.getDelFl() == 0
                        && a.getUserStatusType() == AdminStatusTypeEnum.APPROVAL_ADMIN)
                .orElseThrow(() -> new CustomException(ErrorCodes.UserErrorCode.ADMIN_CAN_NOT_FIND));

        if (!passwordEncoder.matches(adminLoginDto.getPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCodes.UserErrorCode.PASSWORD_DOES_NOT_MATCHED);
        }
        // 2. 로그인 시간 업데이트 (JPA dirty checking)
        admin.setLastAccessedAt(LocalDateTime.now());

        // 3. Spring Security 인증 수행
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(adminLoginDto.getUsername(),
                        adminLoginDto.getPassword());
        authenticationToken.setDetails("ROLE_ADMIN");

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // 4. JWT 토큰 발급
        Token token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(token);

    }


    //관리자 생성
    @Transactional
    public int createAdmin(CreateAdminDto createAdminDto) {
        try {
            String hashedPassword = passwordEncoder.encode(createAdminDto.getPassword());
            Admin admin = Admin.builder()
                    .username(createAdminDto.getUsername())
                    .password(hashedPassword)
                    .name(createAdminDto.getName())
                    .userType(AdminTypeEnum.ADMIN)
                    .userStatusType(AdminStatusTypeEnum.REQUEST_ADMIN)
                    .build();

            Admin savedAdmin = adminRepository.save(admin);
            return savedAdmin.getAdminId();
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    //관리자 회원 상세 정보
    public AdminResponseDto getAdminInfo(int adminId) {
        Admin admin = adminRepository.findById(adminId).filter(a -> a.getDelFl() == 0)
                .orElseThrow(() -> new CustomException(ErrorCodes.UserErrorCode.ADMIN_CAN_NOT_FIND));
        return AdminResponseDto.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .name(admin.getName())
                .lastAccessedAt(admin.getLastAccessedAt())
                .userType(admin.getUserType())
                .userStatusType(admin.getUserStatusType())
                .managePermission(admin.getManagePermission())
                .userPermission(admin.getUserPermission())
                .companyKindPermission(admin.getCompanyKindPermission())
                .dashboardPermission(admin.getDashboardPermission())
                .pointPermission(admin.getPointPermission())
                .gamePermission(admin.getGamePermission())
                .livePermission(admin.getLivePermission())
                .exchangePermission(admin.getExchangePermission())
                .givePointPermission(admin.getGivePointPermission())
                .postPermission(admin.getPostPermission())
                .createYmd(admin.getCreateYmd())
                .build();
    }

    //로그인한 관리자 정보
    public AdminResponseDto loginAdminInfo() {
        Admin admin = userInfoByToken.getAdminInfo();
        return AdminResponseDto.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .name(admin.getName())
                .lastAccessedAt(admin.getLastAccessedAt())
                .userType(admin.getUserType())
                .userStatusType(admin.getUserStatusType())
                .managePermission(admin.getManagePermission())
                .userPermission(admin.getUserPermission())
                .companyKindPermission(admin.getCompanyKindPermission())
                .dashboardPermission(admin.getDashboardPermission())
                .pointPermission(admin.getPointPermission())
                .gamePermission(admin.getGamePermission())
                .livePermission(admin.getLivePermission())
                .exchangePermission(admin.getExchangePermission())
                .givePointPermission(admin.getGivePointPermission())
                .postPermission(admin.getPostPermission())
                .createYmd(admin.getCreateYmd())
                .build();
    }


    //관리자 정보 변경
    @Transactional
    public int updateAdmin(UpdateAdminDto updateAdminDto) {
        try {
            Admin adminUser = userInfoByToken.getAdminInfo();
            adminUser.setUpdateId(adminUser.getAdminId());
            adminUser.setUpdateYmd(LocalDateTime.now());
            if (updateAdminDto.getPassword() != null) {
                String hashedPassword = passwordEncoder.encode(updateAdminDto.getPassword());
                updateAdminDto.setPassword(hashedPassword);
            }
            // null이 아닌 값만 업데이트
            if (updateAdminDto.getName() != null) {
                adminUser.setName(updateAdminDto.getName());
            }
//            if (updateAdminDto.getGameId() != null) {
//                adminUser.setGameId(updateAdminDto.getGameId());
//            }
            if (updateAdminDto.getUserStatusType() != null) {
                adminUser.setUserStatusType(updateAdminDto.getUserStatusType());
            }
            if (updateAdminDto.getManagePermission() != null) {
                adminUser.setManagePermission(updateAdminDto.getManagePermission());
            }
            if (updateAdminDto.getUserPermission() != null) {
                adminUser.setUserPermission(updateAdminDto.getUserPermission());
            }
            if (updateAdminDto.getCompanyKindPermission() != null) {
                adminUser.setCompanyKindPermission(updateAdminDto.getCompanyKindPermission());
            }
            if (updateAdminDto.getDashboardPermission() != null) {
                adminUser.setDashboardPermission(updateAdminDto.getDashboardPermission());
            }
            if (updateAdminDto.getPointPermission() != null) {
                adminUser.setPointPermission(updateAdminDto.getPointPermission());
            }
            if (updateAdminDto.getExchangePermission() != null) {
                adminUser.setExchangePermission(updateAdminDto.getExchangePermission());
            }
            if (updateAdminDto.getGivePointPermission() != null) {
                adminUser.setGivePointPermission(updateAdminDto.getGivePointPermission());
            }
            if (updateAdminDto.getGamePermission() != null) {
                adminUser.setGamePermission(updateAdminDto.getGamePermission());
            }
            if (updateAdminDto.getLivePermission() != null) {
                adminUser.setLivePermission(updateAdminDto.getLivePermission());
            }
            if (updateAdminDto.getPostPermission() != null) {
                adminUser.setPostPermission(updateAdminDto.getPostPermission());
            }

            return adminUser.getAdminId();
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    //관리자 삭제
    @Transactional
    public void deleteAdmin(int adminId) {
        try {
            adminRepository.deleteById(adminId);
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public AdminListResponseDto getAdminList(CustomPaginationDto customPaginationDto) {
        List<Admin> results = adminRepository.searchAdmins(customPaginationDto.getSearchKeyword(),
                customPaginationDto.getPageNo(), customPaginationDto.getPageSize());
        long total = adminRepository.countAdmins(customPaginationDto.getSearchKeyword());

        return AdminListResponseDto.builder()
                .adminList(results)
                .totalCnt((int) total)
                .build();
    }


}
