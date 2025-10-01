package com.research1.api.domain.admin.repository;


import com.research1.api.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer>, AdminRepositoryCustom {
    Optional<Admin> findByUsername(String username);


}
