package com.research2.api.domain.auth.repository;


import com.research2.api.domain.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface AuthRepositoryWithRedis extends CrudRepository<Token, String> {

    Optional<Token> findByRefreshToken(String refreshToken);

}