package com.research1.api.domain.admin.repository;

import com.research1.api.domain.admin.entity.Admin;

import java.util.List;


public interface AdminRepositoryCustom {

    List<Admin> searchAdmins(String keyword, int offset, int limit);

    long countAdmins(String keyword);
}
