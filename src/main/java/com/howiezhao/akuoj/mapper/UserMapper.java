package com.howiezhao.akuoj.mapper;

import com.howiezhao.akuoj.dao.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User login(@Param("name") String name, @Param("password")String password);
}
