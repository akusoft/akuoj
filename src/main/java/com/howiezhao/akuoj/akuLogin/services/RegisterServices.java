package com.howiezhao.akuoj.akuLogin.services;

import com.howiezhao.akuoj.akuLogin.dao.User;

import java.util.Map;

/**
 * @author LiMing
 * @since 2020/5/11 17:20
 **/
public interface RegisterServices {


    Map<String,Object> register(User user);

    int getActivationStatus(int userId,String code);

    Map<String,Object> forget(User user);

    Map<String,Object> sendKaptcha(String email);

}
