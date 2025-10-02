package com.research1.api.domain.jpa.repository;


import com.research1.api.domain.jpa.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer>, AdminRepositoryCustom {
    Optional<Admin> findByUsername(String username);


}
