package com.howiezhao.akuoj.akuLogin.mapper;

import com.howiezhao.akuoj.akuLogin.dao.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: LiMing
 * @since: 2020/5/11 10:33
 **/
@Mapper
public interface UserMapper {

    User selectUserById(@Param("userId") int userId);

    User selectUserByUserName(@Param("username") String username);

    User selectUserByEmail(@Param("email") String email);

    void insertUser(User user);

    void updateStatus(@Param("userId") int userId,@Param("status") int status);

    void updateHeaderUrl(@Param("userId") int userId,@Param("headerUrl") String headerUrl);

    void updatePassword(@Param("userId") int userId, @Param("password") String password);

}
