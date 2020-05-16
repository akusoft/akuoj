package com.howiezhao.akuoj.akuLogin.services;

import com.howiezhao.akuoj.akuLogin.dao.LoginTicket;
import com.howiezhao.akuoj.akuLogin.dao.User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: LiMing
 * @since: 2020/5/11 10:31
 **/

public interface UserServices {

    User selectUserById(int userId);

    Map<String,Object> login(String username, String password, int expiredTime);

    LoginTicket findLoginTicket(String ticket);

    void logout(String ticket);

    void uploadHeaderUrl(int userId,String headerUrl);

    void updatepassword(int userId,String password);



}
