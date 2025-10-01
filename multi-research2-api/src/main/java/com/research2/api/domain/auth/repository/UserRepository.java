package com.research2.api.domain.auth.repository;


import com.research2.api.domain.auth.dto.req.LoginReqDto;
import com.research2.api.domain.auth.dto.res.UserInfoResDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    UserInfoResDto authUser(LoginReqDto LoginReqDto);
    
    UserInfoResDto findByUsername(String identifier);


}




