package com.research1.api.global.util;


import com.research1.api.domain.admin.entity.Admin;
import com.research1.api.global.exception.error.ErrorCodes;
import com.research1.api.global.exception.CustomException;
import com.research1.api.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoByToken {
    private final AdminRepository adminRepository;

    public Admin getAdminInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return adminRepository.findByUsername(userDetails.getUsername()).filter(admin -> admin.getDelFl() == 0).orElseThrow(() -> new CustomException(ErrorCodes.UserErrorCode.ADMIN_CAN_NOT_FIND));
        }
        return null;
    }
}
